package com.keeper.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.keeper.constant.MessagePropertyConstant.NOTE_TITLE_BLANK;
import static com.keeper.constant.MessagePropertyConstant.NOTE_TITLE_LENGTH;

@Getter
@Setter
@ToString
public class NoteDTO {

    private Long id;
    @NotBlank(message = "{" + NOTE_TITLE_BLANK + "}")
    @Size(max = 255, message = "{" + NOTE_TITLE_LENGTH + "}")
    private String title;
    private String description;
}
