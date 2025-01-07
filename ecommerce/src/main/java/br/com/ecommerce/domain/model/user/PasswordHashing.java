package br.com.ecommerce.domain.model.user;

public interface PasswordHashing {
    String hash(String plainText);
    boolean matches(String plainText, Password password);
}
