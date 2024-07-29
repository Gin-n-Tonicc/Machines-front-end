package com.machines.machines_front_end.dtos.request;

import com.machines.machines_front_end.dtos.RegionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RegionRequestDTO extends RegionDTO {
    private UUID countryId;
}
