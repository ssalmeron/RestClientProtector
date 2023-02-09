package com.evergent.client;

import jakarta.enterprise.context.Dependent;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Dependent

public class RestClientGenericError extends RuntimeException{
    private String code;
    private String ErrorDescription;


}
