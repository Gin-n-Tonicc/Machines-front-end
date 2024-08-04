package com.machines.machines_front_end.dtos.auth;

import com.machines.machines_front_end.enums.Role;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicUserDTO {
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private Role role;
}
