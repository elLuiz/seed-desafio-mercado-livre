package br.com.ecommerce.application.i18n;

import br.com.ecommerce.util.Translator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Configuration
class AcceptLanguageHeaderTranslator implements Translator {
    private final ReloadableResourceBundleMessageSource messageSource;

    public AcceptLanguageHeaderTranslator(ReloadableResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public String translate(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

    @Bean
    public LocaleResolver localeResolver() {
        var resolver = new AcceptLanguageResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        return resolver;
    }
}
