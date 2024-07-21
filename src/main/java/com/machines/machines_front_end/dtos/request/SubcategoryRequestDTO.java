package com.machines.machines_front_end.dtos.request;

import com.machines.machines_front_end.dtos.SubcategoryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SubcategoryRequestDTO extends SubcategoryDTO {
    private UUID categoryId;
}
