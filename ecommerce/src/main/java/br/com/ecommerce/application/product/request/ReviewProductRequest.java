package br.com.ecommerce.application.product.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewProductRequest(@NotNull(message = "rating.must.not.be.null")
                                   @Min(value = 1, message = "rating.minimum.must.not.be.lesser.than.one")
                                   @Max(value = 5, message = "rating.maximum.must.not.be.greater.than.five") Integer rating,
                                   @NotBlank(message = "title.must.not.be.empty")
                                   @Size(max = 255, message = "title.must.not.exceed.255.characters") String title,
                                   @NotBlank(message = "description.must.not.be.empty")
                                   @Size(max = 500, message = "description.must.not.exceed.500.characters") String description) {
}