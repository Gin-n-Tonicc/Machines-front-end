package com.machines.machines_front_end.dtos.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CityAdminResponseDTO extends CityResponseDTO {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}