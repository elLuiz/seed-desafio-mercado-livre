package br.com.ecommerce.domain.model.user;

public class UserValidationConstants {
    private UserValidationConstants() {}

    public static final String PASSWORD_EXPRESSION = "^(?=.*[#$>?+\\-@])(?=.*\\d)(?=.*[A-ZÀ-ÖØ-ö])[A-Za-zÀ-ÖØ-öø-ÿ\\d#$>?+\\-@]{6,16}$";
    public static final String NAME_EXPRESSION = "^[A-Za-zÀ-ÖØ-öø-ÿ]+( [A-Za-zÀ-ÖØ-öø-ÿ]+){0,10}$";
}
