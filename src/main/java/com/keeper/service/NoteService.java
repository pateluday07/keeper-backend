package com.keeper.service;

import com.keeper.dto.NoteDTO;

import java.util.List;

public interface NoteService {

    NoteDTO save(NoteDTO note);

    NoteDTO update(NoteDTO note);

    List<NoteDTO> getAll();

    NoteDTO getById(Long id);

    void deleteById(Long id);
}
