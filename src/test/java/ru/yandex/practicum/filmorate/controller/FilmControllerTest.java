package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.util.NestedServletException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    private static final String URN = "/films";

    private static ObjectMapper mapper;
    private StringWriter writer;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mvc;

    @BeforeAll
    static void beforeAll() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }
    @BeforeEach
    void beforeEach() {
        writer = new StringWriter();
    }
    @AfterEach
    void afterEach() throws Exception {
        deleteFilmSetRequest();
    }

    //GET
    @Test
    void shouldReturnEmptyFilmSetWhenNoFilmsWereAdded() throws Exception {
        assertEquals("[]", getFilmSetRequest().getContentAsString());
    }

    //POST
    @Test
    void shouldReturnFilmSetWhenFilmWasAdded() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        postFilmRequest(writer.toString());

        writer = new StringWriter();
        film.setId(1);
        mapper.writeValue(writer, Set.of(film));
        assertEquals(writer.toString(), getFilmSetRequest().getContentAsString());
    }

    @Test
    void shouldReturnStatusCode400WhenAddingFilmWithEmptyName() throws Exception {
        Film film = new Film("", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        assertEquals(400, postFilmRequest(writer.toString()).getStatus());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingFilmWithLongDescription() throws Exception {
        Film film = new Film("Name", "DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        assertEquals(400, postFilmRequest(writer.toString()).getStatus());
    }

    @Test
    void shoulThrowNestedServletExceptionWhenAddingFilmWithReleaseDateBefore28_10_1985() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(1894, 1, 1), 120);
        mapper.writeValue(writer, film);
        assertThrows(NestedServletException.class, () -> postFilmRequest(writer.toString()));
    }

    @Test
    void shouldReturnStatusCode400WhenAddingFilmWithNegativeDuration() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), -1);
        mapper.writeValue(writer, film);
        assertEquals(400, postFilmRequest(writer.toString()).getStatus());
    }

    //PUT
    @Test
    void shouldReturnUpdatedFilmSetWhenFilmWasUpdated() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        postFilmRequest(writer.toString());

        writer = new StringWriter();
        film.setId(1);
        mapper.writeValue(writer, Set.of(film));
        assertEquals(writer.toString(), getFilmSetRequest().getContentAsString());

        Film updatedFilm = new Film(1, "Name", "Updated Description", LocalDate.of(2000, 1, 1), 120);
        writer = new StringWriter();
        mapper.writeValue(writer, updatedFilm);
        putFilmRequest(writer.toString());

        writer = new StringWriter();
        mapper.writeValue(writer, Set.of(updatedFilm));
        assertEquals(writer.toString(), getFilmSetRequest().getContentAsString());
    }

    @Test
    void shouldThrowNestedServletExceptionWhenUpdatingNonExistentFilm() throws Exception {
        Film film = new Film(1, "Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        assertThrows(NestedServletException.class, () -> putFilmRequest(writer.toString()));
    }

    //sending requests
    private MockHttpServletResponse getFilmSetRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get(URN);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
    private MockHttpServletResponse postFilmRequest(String film) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post(URN).
                contentType(MediaType.APPLICATION_JSON).content(film);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
    private MockHttpServletResponse putFilmRequest(String film) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(URN).
                contentType(MediaType.APPLICATION_JSON).content(film);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
    private MockHttpServletResponse deleteFilmSetRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete(URN);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
}