package br.com.ecommerce.application.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductQuestionRequest(@NotBlank(message = "question.must.not.be.empty")
                                     @Size(max = 300, message = "question.must.not.exceed.300.characters") String question) {}