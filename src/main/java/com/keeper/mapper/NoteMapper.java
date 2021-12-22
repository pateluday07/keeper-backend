package com.keeper.mapper;

import com.keeper.dto.NoteDTO;
import com.keeper.entity.Note;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    Note toEntity(NoteDTO noteDTO);

    NoteDTO toDto(Note note);
}
