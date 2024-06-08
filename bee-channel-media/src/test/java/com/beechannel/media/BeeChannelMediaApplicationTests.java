package com.beechannel.media;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beechannel.media.constant.DeriveType;
import com.beechannel.media.constant.FavoriteType;
import com.beechannel.media.constant.SimilarityCalculateType;
import com.beechannel.media.domain.po.LikeList;
import com.beechannel.media.domain.po.Video;
import com.beechannel.media.service.LikeListService;
import com.beechannel.media.service.RecommendService;
import com.beechannel.media.service.VideoService;
import com.google.common.collect.HashBasedTable;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@SpringBootTest
@Slf4j
class BeeChannelMediaApplicationTests {

    @Resource
    private LikeListService likeListService;

    @Resource
    private RecommendService recommendService;

    @Resource
    private VideoService videoService;


    @Test
    void recommendApiTest(){
        List<Video> list = videoService.list();
        list.forEach(item -> {
            item.setCoverPath("/bee-channel/image/" + item.getId() + ".png");
            item.setCategoryId(item.getAuthorId().intValue());
        });
        videoService.saveOrUpdateBatch(list);
    }

    @Test
    void contextLoads() {

        LambdaQueryWrapper<LikeList> videoLikeListWrapper = new LambdaQueryWrapper<>();
        videoLikeListWrapper.eq(LikeList::getDeriveType, DeriveType.VIDEO.getCode());
        videoLikeListWrapper.eq(LikeList::getFavoriteType, FavoriteType.LIKE.getCode());

        List<LikeList> likeListResult = likeListService.list(videoLikeListWrapper);

        // user-item
        // get the user like list group by user id
        Map<Long, List<LikeList>> collectGroupByUserId = likeListResult.stream()
                .collect(Collectors.groupingBy(LikeList::getUserFromId));

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


        List<Long> targetLikeList = collectGroupByUserId.get(1L).stream()
                .map(LikeList::getDeriveId)
                .collect(Collectors.toList());

        calculateSimilarity(allDeriveIdList, likeListResult, overall);

        normalization(allDeriveIdList, overall);

        recommend(allDeriveIdList, targetLikeList, overall);
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
                overall.put(x, y, previous + Math.log1p(collect.size()));
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


    private void recommend(
            List<Long> allDeriveIdList,
            List<Long> userLikeList,
            HashBasedTable<Long, Long, Double> overall
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
        System.out.println(entryList);
    }

}
