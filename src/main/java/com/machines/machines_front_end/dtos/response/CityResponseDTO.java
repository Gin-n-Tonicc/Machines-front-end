package com.machines.machines_front_end.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.machines.machines_front_end.dtos.CityDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CityResponseDTO extends CityDTO {
    private UUID id;
    private UUID regionId;

    @JsonIgnore
    private LocalDateTime deletedAt;
}
