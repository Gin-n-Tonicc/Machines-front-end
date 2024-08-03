package com.machines.machines_front_end.dtos;

import lombok.*;

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

