package com.beechannel.media.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.util.RedisCacheStore;
import com.beechannel.base.util.StringUtil;
import com.beechannel.media.domain.po.Category;
import com.beechannel.media.service.CategoryService;
import com.beechannel.media.mapper.CategoryMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

import java.util.List;

import static com.beechannel.media.constant.StoreSpaceKey.CATEGORY_SPACE;

/**
* @author eotouch
* @description 针对表【category】的数据库操作Service实现
* @createDate 2023-11-27 22:15:05
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    @Resource
    private CategoryMapper categoryMapper;

    @Resource(name = "RedisCacheStore")
    private RedisCacheStore redisStore;

    /**
     * @description get category list
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2023-11-27 22:32
     */
    @Override
    public RestResponse getCategoryList() {
        String categories = redisStore.get(CATEGORY_SPACE);
        List<Category> categoryList;
        if(!StringUtils.hasText(categories)){
            categoryList = categoryMapper.selectList(new LambdaQueryWrapper<>());
            String toJson = JSON.toJSONString(categoryList);
            redisStore.set(CATEGORY_SPACE, toJson, 12 * 60 * 60);
        }else {
            categoryList = JSON.parseObject(categories, List.class);
        }
        return RestResponse.success(categoryList);
    }
}




