package com.machines.machines_front_end.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.machines.machines_front_end.dtos.SubcategoryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SubcategoryResponseDTO extends SubcategoryDTO {
    private UUID id;
    private UUID categoryId;

    @JsonIgnore
    private LocalDateTime deletedAt;
}
