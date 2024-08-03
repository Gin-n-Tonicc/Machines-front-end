package com.machines.machines_front_end.dtos.request;

import com.machines.machines_front_end.dtos.OfferDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OfferRequestDTO extends OfferDTO {
    private UUID ownerId;
    private UUID cityId;
    private UUID subcategoryId;
    private UUID mainPictureId;
    private Set<UUID> pictureIds;
}
