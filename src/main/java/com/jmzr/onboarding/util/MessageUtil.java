package com.jmzr.onboarding.util;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MessageUtil {

    private final MessageSource messageSource;

    public MessageUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key) {
        return getMessage(key, Locale.getDefault());
    }

    public String getMessage(String key, Locale locale) {
        return messageSource.getMessage(key + ".message", null, locale);
    }

    public String getCode(String key) {
        return getCode(key, Locale.getDefault());
    }

    public String getCode(String key, Locale locale) {
        return messageSource.getMessage(key + ".code", null, locale);
    }
}
