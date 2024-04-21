package com.xcoder.tasklist.service;

import java.util.Properties;

import com.xcoder.tasklist.domain.MailType;
import com.xcoder.tasklist.domain.user.User;

public interface MailService {
    void sendEmail(User user, MailType type, Properties params);
}
