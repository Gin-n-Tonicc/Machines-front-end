package com.machines.machines_front_end.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class File extends BaseEntity {
    private String name;
    private String type;
    private String path;
    private Long size;
}

