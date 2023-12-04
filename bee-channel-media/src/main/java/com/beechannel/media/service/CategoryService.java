package com.beechannel.media.service;

import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.domain.po.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author eotouch
* @description 针对表【category】的数据库操作Service
* @createDate 2023-11-27 22:15:05
*/
public interface CategoryService extends IService<Category> {

    /**
     * @description get category list
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2023-11-27 22:32
     */
    RestResponse getCategoryList();
}
