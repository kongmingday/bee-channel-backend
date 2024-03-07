package com.beechannel.order.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description pay service
 * @Author eotouch
 * @Date 2024/01/20 21:04
 * @Version 1.0
 */
public interface PayService {

    void notifyRequest(HttpServletResponse response, HttpServletRequest request);
}
