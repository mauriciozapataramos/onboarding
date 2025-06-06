package com.jmzr.onboarding.service;

import com.jmzr.onboarding.model.ErrorResponse;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MessageService {

    private final MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public ErrorResponse getError(String key, Locale locale) {
        String code = messageSource.getMessage(key + ".code", null, locale);
        String message = messageSource.getMessage(key + ".message", null, locale);
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }
}
