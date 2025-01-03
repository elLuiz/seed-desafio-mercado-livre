package br.com.ecommerce.application.i18n;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

public class AcceptLanguageResolver extends AcceptHeaderLocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String headerLanguage = request.getHeader("Accept-Language");
        return headerLanguage != null && !headerLanguage.isBlank() ? LocaleUtil.convertLocaleFromString(headerLanguage) : Locale.US;
    }
}
