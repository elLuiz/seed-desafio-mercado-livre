package br.com.ecommerce.application.i18n;

import java.util.Locale;

public class LocaleUtil {
    private LocaleUtil() {}

    public static Locale convertLocaleFromString(String input) {
        if (input != null && !input.isBlank()) {
            String[] splitLanguage = input.split("-");
            if (splitLanguage.length >= 2) {
                String language = splitLanguage[0];
                String country = splitLanguage[1];

                return new Locale(language, country);
            }
        }
        return Locale.getDefault();
    }
}
