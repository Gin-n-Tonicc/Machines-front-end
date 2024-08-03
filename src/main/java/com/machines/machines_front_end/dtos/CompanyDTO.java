package com.machines.machines_front_end.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyDTO extends BaseDTO {
    private UUID id;
    private String name;
    private String eik;
    private String phoneNumber;
    private String fax;
    private String address;
    private String website;
    private String description;
}
