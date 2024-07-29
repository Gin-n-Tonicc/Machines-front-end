package com.machines.machines_front_end.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.machines.machines_front_end.dtos.CountryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CountryResponseDTO extends CountryDTO {
    private UUID id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<RegionResponseDTO> regions;

    public void setRegions(List<RegionResponseDTO> regions) {
        if (regions == null) {
            this.regions = new ArrayList<>();
            return;
        }

        this.regions = regions.stream().filter(x -> x.getDeletedAt() == null).toList();
    }
}
