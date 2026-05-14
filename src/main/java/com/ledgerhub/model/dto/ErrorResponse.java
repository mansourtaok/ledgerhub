package com.ledgerhub.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    @JsonProperty("error-code")
    private final String errorCode;

    @JsonProperty("error-message")
    private final String errorMessage;
}
