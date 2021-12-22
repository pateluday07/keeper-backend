package com.keeper.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoteDTO {

    private Long id;
    private String title;
    private String description;
}
