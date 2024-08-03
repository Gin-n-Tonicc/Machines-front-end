package com.machines.machines_front_end.dtos.response;

import com.machines.machines_front_end.dtos.CompanyDTO;
import com.machines.machines_front_end.dtos.File;
import com.machines.machines_front_end.dtos.auth.PublicUserDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CompanyResponseDTO extends CompanyDTO {
    private PublicUserDTO owner;
    private CityResponseDTO city;
    private File mainPicture;
    private Set<File> pictures;
}
