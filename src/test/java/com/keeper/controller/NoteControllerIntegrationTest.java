package com.keeper.controller;

import com.keeper.dto.NoteDTO;
import com.keeper.service.NoteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class NoteControllerIntegrationTest {

    private static final String API_PREFIX = "/api/notes/";
    private static final String URL = "http://localhost:";
    private static final TestRestTemplate testRestTemplate = new TestRestTemplate();

    private static List<NoteDTO> noteDTOS;

    @Autowired
    private NoteService noteService;

    @LocalServerPort
    private int port;

    @BeforeAll
    static void prepareNoteDTO() {
        NoteDTO noteOne = new NoteDTO();
        noteOne.setTitle("One Title");
        noteOne.setDescription("One Description");

        NoteDTO noteTwo = new NoteDTO();
        noteTwo.setTitle("Two Title");
        noteTwo.setDescription("Two Description");

        noteDTOS = Arrays.asList(noteTwo, noteTwo);
    }

    @Test
    void ifNoteIdNotNull_whenSaveNote_thenThrowBadRequestException(){

    }

    @Test
    void whenNotesNotAvailable_thenListShouldBeEmpty() {
        ResponseEntity<List<NoteDTO>> response = testRestTemplate.exchange(URL + port + API_PREFIX, HttpMethod.GET, null,
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
        ResponseEntity<List<NoteDTO>> response = testRestTemplate.exchange(URL + port + API_PREFIX, HttpMethod.GET, null,
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
