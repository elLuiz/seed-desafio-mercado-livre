package br.com.ecommerce.application.common;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

public class Errors {
    @JsonProperty("errors")
    private Set<ErrorDescription> errorDescriptions;

    public void addError(ErrorDescription errorDescription) {
        if (this.errorDescriptions == null) {
            this.errorDescriptions = new HashSet<>();
        }
        this.errorDescriptions.add(errorDescription);
    }
}