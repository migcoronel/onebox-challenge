package com.example.challenge.configuration.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {

    private final LocalDateTime timestamp = LocalDateTime.now(ZoneOffset.UTC);
    private String code;
    private String message;
    private String details;
    private Collection<String> errors;
    private int subcode;

}
