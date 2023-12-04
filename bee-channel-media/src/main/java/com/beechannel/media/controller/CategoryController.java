package com.beechannel.media.controller;

import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.util.RedisCacheStore;
import com.beechannel.media.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.beechannel.media.constant.StoreSpaceKey.CATEGORY_SPACE;

/**
 * @Description category service
 * @Author eotouch
 * @Date 2023/11/27 22:15
 * @Version 1.0
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping
    public RestResponse getCategoryList(){
        return categoryService.getCategoryList();
    }

}
