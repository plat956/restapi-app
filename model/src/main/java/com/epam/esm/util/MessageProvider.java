package com.epam.esm.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;


/**
 * The Message provider class.
 */
@Component
public class MessageProvider {

    private MessageSource messageSource;

    @Autowired
    public MessageProvider(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Get a message.
     *
     * @param code the message code
     * @return the message text
     */
    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}
