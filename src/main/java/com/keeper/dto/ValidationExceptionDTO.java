package com.keeper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValidationExceptionDTO {

    private Integer status;
    private String message;
}
