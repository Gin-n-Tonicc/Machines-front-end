package com.machines.machines_front_end.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.machines.machines_front_end.dtos.RegionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RegionResponseDTO extends RegionDTO {
    private UUID id;
    private UUID countryId;
    private List<CityResponseDTO> cities;

    private LocalDateTime deletedAt;

    public void setCities(List<CityResponseDTO> cities) {
        this.cities = cities.stream().filter(x -> x.getDeletedAt() == null).toList();
    }
}
