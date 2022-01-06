package com.keeper.controller;

import com.keeper.dto.NoteDTO;
import com.keeper.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<HttpStatus> save(@Valid @RequestBody NoteDTO noteDTO) {
        log.info("Note to save: {}", noteDTO);
        noteService.save(noteDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<HttpStatus> update(@Valid @RequestBody NoteDTO noteDTO) {
        log.info("Note to update: {}", noteDTO);
        noteService.update(noteDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<NoteDTO>> getAll() {
        log.info("Get all Notes");
        return ResponseEntity.ok(noteService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> getById(@PathVariable Long id) {
        log.info("Get Note by id: {}", id);
        return ResponseEntity.ok(noteService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        log.info("Delete Note by id: {}", id);
        noteService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<HttpStatus> isExistsById(@PathVariable Long id) {
        log.info("Is Note exists by id: {}", id);
        if (noteService.isExistsById(id))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
