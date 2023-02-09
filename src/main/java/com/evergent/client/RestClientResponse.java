package com.evergent.client;

import jakarta.enterprise.context.Dependent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Dependent
public class RestClientResponse {

    private String code;
    private IServiceResponse response;

}
