package com.evergent.client;

import jakarta.enterprise.context.Dependent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Dependent
public class FaultyServiceResponse implements IServiceResponse {
    private String message;


}
