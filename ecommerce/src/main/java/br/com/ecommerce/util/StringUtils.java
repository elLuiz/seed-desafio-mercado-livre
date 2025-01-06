package br.com.ecommerce.util;

public class StringUtils {
    private StringUtils() {}

    public static boolean isNullOrEmpty(String literal) {
        return literal == null || literal.isBlank();
    }
}
