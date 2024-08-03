package com.machines.machines_front_end.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryDTO extends BaseDTO {
    private String name;
}
