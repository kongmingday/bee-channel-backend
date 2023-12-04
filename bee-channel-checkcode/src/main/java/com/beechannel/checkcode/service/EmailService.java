package com.beechannel.checkcode.service;

/**
 * @author eotouch
 * @date 2022/12/24
 */
public interface EmailService {

    void sendSimpleMail(String from, String to, String subject, String text);
}
