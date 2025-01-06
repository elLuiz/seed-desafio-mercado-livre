package br.com.ecommerce.domain.model.user;

public interface PasswordHashing {
    String hash(String plainText);
}
