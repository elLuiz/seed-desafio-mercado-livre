package br.com.ecommerce.domain.model.user;

public interface Hashing {
    String hash(String plainText);
}
