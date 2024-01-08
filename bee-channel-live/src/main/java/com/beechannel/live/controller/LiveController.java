package com.beechannel.live.controller;

import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.live.domain.dto.SRSRequestParams;
import com.beechannel.live.service.LiveService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description live service
 * @Author eotouch
 * @Date 2024/01/04 21:43
 * @Version 1.0
 */
@RestController
@RequestMapping("/process")
public class LiveController {

    @Resource
    private LiveService liveService;

    @PostMapping("/initCheck")
    public int liveInitCheck(@RequestBody SRSRequestParams srsRequestParams){
        return liveService.liveInitCheck(srsRequestParams);
    }

    @PostMapping("/stopProcess")
    public int stopProcess(@RequestBody SRSRequestParams srsRequestParams){
        return liveService.stopProcess(srsRequestParams);
    }

    @GetMapping("/license")
    public RestResponse getPersonalLicense(){
        return liveService.getPersonalLicense();
    }

    @PostMapping("/license")
    public RestResponse applyLicense(){
        return liveService.addLicense();
    }

    @DeleteMapping("/license")
    public RestResponse cancelLicense(){
        return liveService.deleteLicense();
    }
}
