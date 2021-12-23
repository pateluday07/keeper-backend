package com.keeper.controller;

import com.keeper.dto.NoteDTO;
import com.keeper.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.keeper.constant.MessagePropertyConstant.NOTE_ID_NULL_VALIDATION;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteDTO> save(@Valid @RequestBody NoteDTO noteDTO) {
        log.info("Note to save: {}", noteDTO);
        return ResponseEntity.ok(noteService.save(noteDTO));
    }

    @PutMapping
    public ResponseEntity<NoteDTO> update(@Valid @RequestBody NoteDTO noteDTO) {
        log.info("Note to update: {}", noteDTO);
        return ResponseEntity.ok(noteService.update(noteDTO));
    }

    @GetMapping
    public ResponseEntity<List<NoteDTO>> getAll() {
        log.info("Get all Notes");
        return ResponseEntity.ok(noteService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> getById(@NotNull(message = "{" + NOTE_ID_NULL_VALIDATION + "}") @PathVariable Long id) {
        log.info("Get Note by id: {}", id);
        return ResponseEntity.ok(noteService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@NotNull(message = "{" + NOTE_ID_NULL_VALIDATION + "}") @PathVariable Long id) {
        log.info("Delete Note by id: {}", id);
        noteService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
