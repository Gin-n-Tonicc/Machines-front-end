package com.machines.machines_front_end.dtos.response;

import com.machines.machines_front_end.dtos.CategoryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CategoryResponseDTO extends CategoryDTO {
    private UUID id;
    private List<SubcategoryResponseDTO> subcategories;

    public void setSubcategories(List<SubcategoryResponseDTO> subcategories) {
        this.subcategories = subcategories.stream().filter(x -> x.getDeletedAt() == null).toList();
    }

}
