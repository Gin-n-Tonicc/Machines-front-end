package com.machines.machines_front_end.dtos.auth;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenBodyDTO {
    private String accessToken;
}
