package com.machines.machines_front_end.dtos.response;

import com.machines.machines_front_end.dtos.File;
import com.machines.machines_front_end.dtos.OfferDTO;
import com.machines.machines_front_end.dtos.auth.PublicUserDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class OfferResponseDTO extends OfferDTO {
    private PublicUserDTO owner;
    private CityResponseDTO city;
    private SubcategoryResponseDTO subcategory;
    private File mainPicture;
    private Set<File> pictures;
}
