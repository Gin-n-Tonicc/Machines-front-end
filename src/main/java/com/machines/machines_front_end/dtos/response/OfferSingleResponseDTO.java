package com.machines.machines_front_end.dtos.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OfferSingleResponseDTO extends OfferResponseDTO {
    private List<OfferResponseDTO> similarOffers;
}
