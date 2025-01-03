package br.com.ecommerce.application.i18n;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageConfiguration {
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        var messageSource= new ReloadableResourceBundleMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setBasename("classpath:locale/messages");
        return messageSource;
    }
}