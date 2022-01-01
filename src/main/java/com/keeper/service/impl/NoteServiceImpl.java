package com.keeper.service.impl;

import com.keeper.dto.NoteDTO;
import com.keeper.entity.Note;
import com.keeper.mapper.NoteMapper;
import com.keeper.repository.NoteRepository;
import com.keeper.service.NoteService;
import com.keeper.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.keeper.constant.MessagePropertyConstant.*;
import static com.keeper.util.ExceptionUtil.badRequestException;
import static com.keeper.util.ExceptionUtil.notFoundException;

@Service
@Log4j2
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final MessageSourceUtil messageSourceUtil;
    private final NoteMapper noteMapper;

    @Override
    @Transactional
    public void save(NoteDTO note) {
        log.info("Note to save: {}", note);
        if (note.getId() != null)
            throw badRequestException(messageSourceUtil.getMessage(NOTE_ID_NOT_NULL));
        Note savedNote = noteRepository.save(noteMapper.toEntity(note));
        log.info("Saved Note: {}", savedNote);
    }

    @Override
    @Transactional
    public void update(NoteDTO note) {
        log.info("Note to update: {}", note);
        if (note.getId() == null)
            throw badRequestException(messageSourceUtil.getMessage(NOTE_ID_NULL));
        if (!noteRepository.existsById(note.getId()))
            throw notFoundException(messageSourceUtil.getMessage(NOTE_NOT_FOUND) + note.getId());
        Note updatedNote = noteRepository.save(noteMapper.toEntity(note));
        log.info("Updated Note: {}", updatedNote);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteDTO> getAll() {
        log.info("Get all Notes");
        return noteRepository
                .findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(noteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public NoteDTO getById(Long id) {
        log.info("Get Note by id: {}", id);
        Note note = getNoteEntityById(id);
        log.info("Note: {} for the id: {}", note, id);
        return noteMapper.toDto(note);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.info("Delete Note by id: {}", id);
        Note note = getNoteEntityById(id);
        noteRepository.delete(note);
        log.info("Note successfully deleted for the id: {}", id);
    }

    private Note getNoteEntityById(Long id) {
        return noteRepository
                .findById(id)
                .orElseThrow(() -> notFoundException(messageSourceUtil.getMessage(NOTE_NOT_FOUND) + id));
    }
}
