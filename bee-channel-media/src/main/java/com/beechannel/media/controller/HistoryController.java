package com.beechannel.media.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.media.domain.dto.AddHistory;
import com.beechannel.media.domain.po.History;
import com.beechannel.media.service.HistoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description the history of the use's action with video
 * @Author eotouch
 * @Date 2024/02/13 17:50
 * @Version 1.0
 */
@RestController
@RequestMapping("/history")
public class HistoryController {

    @Resource
    private HistoryService historyService;

    @PostMapping
    public void insertOrUpdateHistory(@RequestBody AddHistory addHistory){
        historyService.historyProcess(addHistory);
    }

    @GetMapping
    public RestResponse getHistoryPage(PageParams pageParams){
        return historyService.getHistoryPage(pageParams);
    }
}
