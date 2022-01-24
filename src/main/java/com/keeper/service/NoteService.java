package com.keeper.service;

import com.keeper.dto.NoteDTO;

import java.util.List;

public interface NoteService {

    void save(NoteDTO note);

    void update(NoteDTO note);

    List<NoteDTO> getAll();

    NoteDTO getById(Long id);

    void deleteById(Long id);

    Boolean isExistsById(Long id);
}
