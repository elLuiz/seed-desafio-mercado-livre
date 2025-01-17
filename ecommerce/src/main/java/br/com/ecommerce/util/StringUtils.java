package br.com.ecommerce.util;

import org.springframework.util.Assert;

public class StringUtils {
    private StringUtils() {}

    public static boolean isNullOrEmpty(String literal) {
        return literal == null || literal.isBlank();
    }

    public static boolean greaterThan(String literal, int limit) {
        Assert.notNull(literal, "string.must.not.be.empty");
        return literal.length() > limit;
    }
}