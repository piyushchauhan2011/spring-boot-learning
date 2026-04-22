package com.learning.backend.api.i18n;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/locale")
public class LocaleController {

    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public LocaleController(MessageSource messageSource, LocaleResolver localeResolver) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @GetMapping("/greeting")
    public String greeting(HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        return messageSource.getMessage("api.greeting", null, locale);
    }
}
