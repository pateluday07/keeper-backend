package com.keeper.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.keeper.dto.NoteDTO;
import com.keeper.repository.NoteRepository;
import com.keeper.service.NoteService;
import com.keeper.util.MessageSourceUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.keeper.constant.MessagePropertyConstant.*;
import static com.keeper.constants.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class NoteControllerIntegrationTest {

    private static final String API_PREFIX = "/api/notes/";
    private static final TestRestTemplate REST_TEMPLATE = new TestRestTemplate();
    private static final HttpHeaders HTTP_HEADERS = new HttpHeaders();

    @Autowired
    private NoteService noteService;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private MessageSourceUtil messageSourceUtil;

    @LocalServerPort
    private int port;
    private List<NoteDTO> noteDTOS;

    @BeforeAll
    static void initHttpHeaders() {
        HTTP_HEADERS.setContentType(MediaType.APPLICATION_JSON);
    }

    @BeforeEach
    void prepareNoteDTO() {
        NoteDTO noteOne = new NoteDTO();
        noteOne.setTitle("One Title");
        noteOne.setDescription("One Description");

        NoteDTO noteTwo = new NoteDTO();
        noteTwo.setTitle("Two Title");
        noteTwo.setDescription("Two Description");

        noteDTOS = Arrays.asList(noteTwo, noteTwo);
    }

    @BeforeEach
    void emptyNoteTable() {
        noteRepository.deleteAll();
    }

    @Test
    void ifNoteTitleNull_whenSaveNote_thenThrowBadRequestException() {
        NoteDTO noteDTO = noteDTOS.get(0);
        noteDTO.setTitle(null);
        ResponseEntity<JsonNode> response = REST_TEMPLATE.postForEntity(URL + port + API_PREFIX,
                new HttpEntity<>(noteDTO, HTTP_HEADERS), JsonNode.class);
        assertTrue(response.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(messageSourceUtil.getMessage(VAL_NOTE_TITLE_BLANK),
                Objects.requireNonNull(response.getBody()).get(EXCEPTION_MESSAGE_KEY).asText());
    }

    @Test
    void ifNoteTitleEmpty_whenSaveNote_thenThrowBadRequestException() {
        NoteDTO noteDTO = noteDTOS.get(0);
        noteDTO.setTitle("");
        ResponseEntity<JsonNode> response = REST_TEMPLATE.postForEntity(URL + port + API_PREFIX,
                new HttpEntity<>(noteDTO, HTTP_HEADERS), JsonNode.class);
        assertTrue(response.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(messageSourceUtil.getMessage(VAL_NOTE_TITLE_BLANK),
                Objects.requireNonNull(response.getBody()).get(EXCEPTION_MESSAGE_KEY).asText());
    }

    @Test
    void ifNoteTitleTooLong_whenSaveNote_thenThrowBadRequestException() {
        NoteDTO noteDTO = noteDTOS.get(1);
        noteDTO.setTitle(NOTE_TOO_LING_TITLE);
        ResponseEntity<JsonNode> response = REST_TEMPLATE.postForEntity(URL + port + API_PREFIX,
                new HttpEntity<>(noteDTO, HTTP_HEADERS), JsonNode.class);
        assertTrue(response.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(messageSourceUtil.getMessage(VAL_NOTE_TITLE_LENGTH),
                Objects.requireNonNull(response.getBody()).get(EXCEPTION_MESSAGE_KEY).asText());
    }

    @Test
    void ifNoteIdNotNull_whenSaveNote_thenThrowBadRequestException() {
        NoteDTO noteDTO = noteDTOS.get(0);
        noteDTO.setId(1L);
        ResponseEntity<JsonNode> response = REST_TEMPLATE.postForEntity(URL + port + API_PREFIX,
                new HttpEntity<>(noteDTO, HTTP_HEADERS), JsonNode.class);
        assertTrue(response.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), Objects.requireNonNull(response.getBody()).get(EXCEPTION_STATUS_KEY).asInt());
        assertEquals(API_PREFIX, response.getBody().get(EXCEPTION_PATH_KEY).asText());
        assertEquals(messageSourceUtil.getMessage(NOTE_ID_NOT_NULL), response.getBody().get(EXCEPTION_MESSAGE_KEY).asText());
    }

    @Test
    void ifNoteValid_whenSaveNote_thenStatusShouldBeCreated() {
        ResponseEntity<HttpStatus> response = REST_TEMPLATE.postForEntity(URL + port + API_PREFIX,
                new HttpEntity<>(noteDTOS.get(0), HTTP_HEADERS), HttpStatus.class);
        assertFalse(response.hasBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void ifNoteTitleNull_whenUpdateNote_thenThrowBadRequestException() {
        NoteDTO noteDTO = noteDTOS.get(0);
        noteDTO.setTitle(null);
        ResponseEntity<JsonNode> response = REST_TEMPLATE.exchange(URL + port + API_PREFIX, HttpMethod.PUT,
                new HttpEntity<>(noteDTO, HTTP_HEADERS), JsonNode.class);
        assertTrue(response.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(messageSourceUtil.getMessage(VAL_NOTE_TITLE_BLANK),
                Objects.requireNonNull(response.getBody()).get(EXCEPTION_MESSAGE_KEY).asText());
    }

    @Test
    void ifNoteTitleEmpty_whenUpdateNote_thenThrowBadRequestException() {
        NoteDTO noteDTO = noteDTOS.get(0);
        noteDTO.setTitle("");
        ResponseEntity<JsonNode> response = REST_TEMPLATE.exchange(URL + port + API_PREFIX, HttpMethod.PUT,
                new HttpEntity<>(noteDTO, HTTP_HEADERS), JsonNode.class);
        assertTrue(response.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(messageSourceUtil.getMessage(VAL_NOTE_TITLE_BLANK),
                Objects.requireNonNull(response.getBody()).get(EXCEPTION_MESSAGE_KEY).asText());
    }

    @Test
    void ifNoteIdNull_whenUpdateNote_thenThrowBadRequestException() {
        NoteDTO noteDTO = noteDTOS.get(0);
        ResponseEntity<JsonNode> response = REST_TEMPLATE.exchange(URL + port + API_PREFIX, HttpMethod.PUT,
                new HttpEntity<>(noteDTO, HTTP_HEADERS), JsonNode.class);
        assertTrue(response.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), Objects.requireNonNull(response.getBody()).get(EXCEPTION_STATUS_KEY).asInt());
        assertEquals(API_PREFIX, response.getBody().get(EXCEPTION_PATH_KEY).asText());
        assertEquals(messageSourceUtil.getMessage(NOTE_ID_NULL), response.getBody().get(EXCEPTION_MESSAGE_KEY).asText());
    }

    @Test
    void ifNoteNotFound_whenUpdateNote_thenThrowNotFoundException() {
        NoteDTO noteDTO = noteDTOS.get(0);
        noteDTO.setId(1L);
        ResponseEntity<JsonNode> response = REST_TEMPLATE.exchange(URL + port + API_PREFIX, HttpMethod.PUT,
                new HttpEntity<>(noteDTO, HTTP_HEADERS), JsonNode.class);
        assertTrue(response.hasBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND.value(), Objects.requireNonNull(response.getBody()).get(EXCEPTION_STATUS_KEY).asInt());
        assertEquals(API_PREFIX, response.getBody().get(EXCEPTION_PATH_KEY).asText());
        assertEquals(messageSourceUtil.getMessage(NOTE_NOT_FOUND) + noteDTO.getId(), response.getBody().get(EXCEPTION_MESSAGE_KEY).asText());
    }

    @Test
    void ifNoteValid_whenUpdateNote_thenReturnHttpStatusOkAndNoteShouldBeUpdated() {
        NoteDTO noteDTO = noteDTOS.get(0);
        noteService.save(noteDTO);
        noteDTO = noteService.getById(1L);
        noteDTO.setTitle("New Title");
        noteDTO.setDescription("My New Title");
        ResponseEntity<JsonNode> response = REST_TEMPLATE.exchange(URL + port + API_PREFIX, HttpMethod.PUT,
                new HttpEntity<>(noteDTO, HTTP_HEADERS), JsonNode.class);
        assertFalse(response.hasBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        NoteDTO updatedNote = noteService.getById(1L);
        assertEquals(noteDTO.getTitle(), updatedNote.getTitle());
        assertEquals(noteDTO.getDescription(), updatedNote.getDescription());
    }

    @Test
    void ifNotesUnAvailable_whenGetAllNotes_thenListShouldBeEmpty() {
        ResponseEntity<List<NoteDTO>> response = REST_TEMPLATE.exchange(URL + port + API_PREFIX, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    void ifNotesAvailable_whenGetAllNotes_thenListShouldNotBeEmpty() {
        noteService.save(noteDTOS.get(0));
        noteService.save(noteDTOS.get(1));
        ResponseEntity<List<NoteDTO>> response = REST_TEMPLATE.exchange(URL + port + API_PREFIX, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertFalse(Objects.requireNonNull(response.getBody()).isEmpty());
        assertEquals(2, response.getBody().size());
        assertNotNull(response.getBody().get(0).getId());
        assertNotNull(response.getBody().get(1).getId());
        assertEquals(noteDTOS.get(0).getTitle(), response.getBody().get(0).getTitle());
        assertEquals(noteDTOS.get(0).getDescription(), response.getBody().get(0).getDescription());
        assertEquals(noteDTOS.get(1).getTitle(), response.getBody().get(1).getTitle());
        assertEquals(noteDTOS.get(1).getDescription(), response.getBody().get(1).getDescription());
    }

}
