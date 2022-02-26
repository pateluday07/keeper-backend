package com.keeper.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.keeper.dto.NoteDTO;
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

import static com.keeper.constant.MessagePropertyConstant.NOTE_ID_NOT_NULL;
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
    private MessageSourceUtil messageSourceUtil;

    @LocalServerPort
    private int port;
    private List<NoteDTO> noteDTOS;

    @BeforeAll
    static void init() {
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

    @Test
    void ifNoteIdNotNull_whenSaveNote_thenThrowBadRequestException() {
        NoteDTO noteDTO = noteDTOS.get(0);
        noteDTO.setId(1L);
        ResponseEntity<JsonNode> exception = REST_TEMPLATE.postForEntity(URL + port + API_PREFIX,
                new HttpEntity<>(noteDTO, HTTP_HEADERS), JsonNode.class);
        assertTrue(exception.hasBody());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST.value(), Objects.requireNonNull(exception.getBody()).get(EXCEPTION_STATUS_KEY).asInt());
        assertEquals(API_PREFIX, exception.getBody().get(EXCEPTION_PATH_KEY).asText());
        assertEquals(messageSourceUtil.getMessage(NOTE_ID_NOT_NULL), exception.getBody().get(EXCEPTION_MESSAGE_KEY).asText());
    }

    @Test
    void whenNotesNotAvailable_thenListShouldBeEmpty() {
        ResponseEntity<List<NoteDTO>> response = REST_TEMPLATE.exchange(URL + port + API_PREFIX, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertTrue(Objects.requireNonNull(response.getBody()).isEmpty());
    }

    @Test
    void whenNotesAvailable_thenListShouldNotBeEmpty() {
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
