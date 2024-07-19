package com.machines.machines_front_end.dtos.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse implements Serializable {
    private String accessToken;
    private String refreshToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PublicUserDTO user;
}