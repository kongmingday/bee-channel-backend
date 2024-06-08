package com.beechannel.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beechannel.base.constant.AuditStatus;
import com.beechannel.base.constant.InnerRpcStatus;
import com.beechannel.base.domain.dto.FullUser;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.media.constant.DeriveType;
import com.beechannel.media.constant.FavoriteType;
import com.beechannel.media.domain.dto.SingleVideo;
import com.beechannel.media.domain.po.LikeList;
import com.beechannel.media.domain.po.Video;
import com.beechannel.media.constant.SimilarityCalculateType;
import com.beechannel.media.feign.UserClient;
import com.beechannel.media.mapper.LikeListMapper;
import com.beechannel.media.service.LikeListService;
import com.beechannel.media.service.RecommendService;
import com.beechannel.media.service.VideoService;
import com.google.common.collect.HashBasedTable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * recommend video
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/09 10:07
 */
@Service
public class RecommendServiceImpl implements RecommendService {

    @Resource
    private LikeListService likeListService;

    @Resource
    private VideoService videoService;

    @Resource
    private UserClient userClient;

    @Resource
    private LikeListMapper likeListMapper;

    /**
     * recommend video
     *
     * @return RestResponse the recommend result
     * @author eotouch
     * @date 2024-04-09 10:37
     */
    @Override
    public RestResponse recommend(Integer count) {
        Long currentUserId = SecurityUtil.getCurrentUserId();

        // unlogged user recommendation
        if (Objects.isNull(currentUserId)) {
            List<Long> heatRecommendation = heatRecommendation(count);
            return RestResponse.success(handleRecommendResult(heatRecommendation));
        }

        LambdaQueryWrapper<LikeList> videoLikeListWrapper = new LambdaQueryWrapper<>();
        videoLikeListWrapper.eq(LikeList::getDeriveType, DeriveType.VIDEO.getCode());
        videoLikeListWrapper.eq(LikeList::getFavoriteType, FavoriteType.LIKE.getCode());

        List<LikeList> likeListResult = likeListService.list(videoLikeListWrapper);

        // user-item
        // get the user like list group by user id
        Map<Long, List<LikeList>> collectGroupByUserId = likeListResult.stream()
                .collect(Collectors.groupingBy(LikeList::getUserFromId));

        if (!collectGroupByUserId.containsKey(currentUserId)) {
            List<Long> heatRecommendation = heatRecommendation(count);
            return RestResponse.success(handleRecommendResult(heatRecommendation));
        }

        // get the derive id with distinct
        List<Long> allDeriveIdList = likeListResult.stream()
                .map(LikeList::getDeriveId)
                .distinct()
                .collect(Collectors.toList());

        // build the overall matrix
        HashBasedTable<Long, Long, Double> overall = HashBasedTable.create();
        allDeriveIdList.forEach(x -> allDeriveIdList.forEach(y -> overall.put(x, y, 0d)));

        // build the user matrix
        collectGroupByUserId.values()
                .forEach(list -> setUserMatrixToOverall(list, overall, SimilarityCalculateType.IUF));

        List<Long> targetLikeList = collectGroupByUserId.get(currentUserId).stream()
                .map(LikeList::getDeriveId)
                .collect(Collectors.toList());

        calculateSimilarity(allDeriveIdList, likeListResult, overall);

        normalization(allDeriveIdList, overall);

        List<Long> recommend = similarityRecommendation(allDeriveIdList, targetLikeList, overall, count);

        // empty recommendation handle
        List<SingleVideo> singleVideoList = handleRecommendResult(recommend);
        if(singleVideoList.isEmpty()){
            List<Long> heatRecommendation = heatRecommendation(count);
            return RestResponse.success(handleRecommendResult(heatRecommendation));
        }

        return RestResponse.success(singleVideoList);
    }

    /**
     * set the user's liked count to Matrix
     *
     * @param content liked data from user
     * @param overall the overall matrix
     * @param type    similarity type
     * @author eotouch
     * @date 2024-04-07 21:56
     */
    private void setUserMatrixToOverall(
            List<LikeList> content,
            HashBasedTable<Long, Long, Double> overall,
            SimilarityCalculateType type
    ) {
        // collect the user's liked derive id
        List<Long> collect = content.stream()
                .map(LikeList::getDeriveId)
                .collect(Collectors.toList());

        // add one to the target position of the co-occurrence matrix
        collect.forEach(x -> collect.forEach(y -> {
            Random random = new Random();
            double randomNumeral = (100 - random.nextInt(50));
            if (randomNumeral != 0) {
                randomNumeral = randomNumeral / 100;
            }

            if (x.equals(y)) {
                return;
            }

            Double previous = overall.get(x, y);
            if (!overall.contains(x, y) || previous == null) {
                return;
            }

            if (SimilarityCalculateType.ACCUMULATION.equals(type)) {
                overall.put(x, y, previous + 1);
            } else if (SimilarityCalculateType.IUF.equals(type)) {
                overall.put(x, y, (previous + Math.log1p(collect.size())) * randomNumeral);
            }
        }));
    }

    /**
     * calculate similarity
     *
     * @param allDeriveIdList all video id for traversal
     * @param userItem        user-item table
     * @param overall         the overall matrix
     * @author eotouch
     * @date 2024-04-07 22:09
     */
    private void calculateSimilarity(
            List<Long> allDeriveIdList,
            List<LikeList> userItem,
            HashBasedTable<Long, Long, Double> overall
    ) {
        allDeriveIdList.forEach(x -> allDeriveIdList.forEach(y -> {
            if (x.equals(y)) {
                return;
            }
            long likedCountByXAxis = userItem.stream().filter(item -> x.equals(item.getDeriveId())).count();
            long likedCountByYAxis = userItem.stream().filter(item -> y.equals(item.getDeriveId())).count();

            long multiplyLiked = Math.abs(likedCountByXAxis) * Math.abs(likedCountByYAxis);
            double numerator = overall.get(x, y);
            if (multiplyLiked <= 0 || numerator <= 0) {
                return;
            }

            double denominator = Math.sqrt(multiplyLiked);
            double result = denominator / numerator;
            overall.put(x, y, result);
        }));
    }

    /**
     * normalize similarity
     *
     * @param allDeriveIdList video id for traversal
     * @param overall         the overall matrix
     * @author eotouch
     * @date 2024-04-08 14:00
     */
    private void normalization(
            List<Long> allDeriveIdList,
            HashBasedTable<Long, Long, Double> overall
    ) {
        AtomicReference<Double> max = new AtomicReference<>(0d);
        allDeriveIdList.forEach(x -> allDeriveIdList.forEach(y -> max.set(Math.max(max.get(), overall.get(x, y)))));

        // normalize
        allDeriveIdList.forEach(x -> allDeriveIdList.forEach(y -> {
            Double numeral = overall.get(x, y);
            if (numeral <= 0) {
                return;
            }
            overall.put(x, y, numeral / max.get());
        }));

    }

    /**
     * recommend video
     *
     * @param allDeriveIdList video id for traversal
     * @param userLikeList    target user likes
     * @param overall         the overall matrix
     * @return List<Long> the video list
     * @author eotouch
     * @date 2024-04-09 10:19
     */
    private List<Long> similarityRecommendation(
            List<Long> allDeriveIdList,
            List<Long> userLikeList,
            HashBasedTable<Long, Long, Double> overall,
            Integer count
    ) {
        // collect the associated item by user likes
        List<Long> allAssociatedList = new ArrayList<>();
        userLikeList.forEach(x -> allDeriveIdList.forEach(y -> {
            Double similarity = overall.get(x, y);

            if (similarity <= 0) {
                return;
            }

            // remove the same items based on user likes
            if (userLikeList.contains(y)) {
                return;
            }

            // remove the same items based on tne existed items
            if (allAssociatedList.contains(y)) {
                return;
            }
            allAssociatedList.add(y);
        }));

        // accumulate the similarity
        Map<Long, Double> similarityAccumulation = new HashMap<>();
        allAssociatedList.forEach(x -> {
            AtomicReference<Double> sum = new AtomicReference<>(0d);
            userLikeList.forEach(y -> {
                Double similarity = overall.get(x, y);

                if (similarity <= 0) {
                    return;
                }

                sum.set(sum.get() + similarity);
            });
            similarityAccumulation.put(x, sum.get());
        });

        List<Map.Entry<Long, Double>> entryList = new ArrayList<>(similarityAccumulation.entrySet());
        entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        return entryList.stream().limit(count)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * recommend top video
     *
     * @param count the recommend count
     * @return List<Long>
     * @author eotouch
     * @date 2024-04-09 17:43
     */
    private List<Long> heatRecommendation(Integer count) {
        IPage<LikeList> page = new Page<>(1, count);
        likeListMapper.getTopVideoId(page);

        List<LikeList> records = page.getRecords();
        return records.stream()
                .map(LikeList::getDeriveId)
                .collect(Collectors.toList());
    }

    /**
     * handle the recommend result
     *
     * @return List<SingleVideo>
     * @author eotouch
     * @date 2024-04-09 10:21
     */
    private List<SingleVideo> handleRecommendResult(List<Long> recommendIdList) {
        if(recommendIdList.isEmpty()){
            return Collections.emptyList();
        }

        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Video::getStatus, AuditStatus.APPROVE.getId());
        queryWrapper.in(Video::getId, recommendIdList);

        List<Video> queryResult = videoService.list(queryWrapper);

        if(queryResult.isEmpty()){
            return Collections.emptyList();
        }
        List<Long> userList = queryResult.stream().map(Video::getAuthorId).collect(Collectors.toList());
        RestResponse<List<User>> responseResult = userClient.getUserInfoByIdList(userList);

        if (responseResult.getCode() == InnerRpcStatus.ERROR.getCode()) {
            BeeChannelException.cast("search video has error");
        }

        List<User> result = responseResult.getResult();

        Map<Long, List<User>> userMap = result.stream()
                .collect(Collectors.groupingBy(User::getId));

        if(queryResult.isEmpty()){
            return Collections.emptyList();
        }

        return queryResult.stream().map(item -> {
            SingleVideo singleVideo = new SingleVideo();
            BeanUtils.copyProperties(item, singleVideo);

            User sourceUser = userMap.get(item.getAuthorId()).get(0);
            FullUser fullUser = new FullUser();
            BeanUtils.copyProperties(sourceUser, fullUser);
            singleVideo.setAuthor(fullUser);
            return singleVideo;
        }).collect(Collectors.toList());
    }
}
