package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        deleteRequest(URN);
        deleteRequest("/users");
    }

    //storage
    //GET
    @Test
    void shouldReturnEmptyFilmSetWhenNoFilmsWereAdded() throws Exception {
        assertEquals("[]", getRequest(URN).getContentAsString());
    }

    @Test
    void shouldReturnFilmWhenFilmWasAdded() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        writer = new StringWriter();
        film.setId(1);
        mapper.writeValue(writer, film);
        assertEquals(writer.toString(), getRequest(URN + "/1").getContentAsString());
    }

    @Test
    void shouldReturnStatusCode404WhenGettingFilmWithWrongId() throws Exception {
        assertEquals(404, getRequest(URN + "/1").getStatus());
    }

    //POST
    @Test
    void shouldReturnFilmSetWhenFilmWasAdded() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        writer = new StringWriter();
        film.setId(1);
        mapper.writeValue(writer, Set.of(film));
        assertEquals(writer.toString(), getRequest(URN).getContentAsString());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingFilmWithEmptyName() throws Exception {
        Film film = new Film("", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        assertEquals(500, postRequest(URN, writer.toString()).getStatus());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingFilmWithLongDescription() throws Exception {
        Film film = new Film("Name", "DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        assertEquals(500, postRequest(URN, writer.toString()).getStatus());
    }

    @Test
    void shouldReturnStatusCode400WhenAddingFilmWithReleaseDateBefore28_10_1985() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(1894, 1, 1), 120);
        mapper.writeValue(writer, film);
        assertEquals(400, postRequest(URN, writer.toString()).getStatus());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingFilmWithNegativeDuration() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), -1);
        mapper.writeValue(writer, film);
        assertEquals(500, postRequest(URN, writer.toString()).getStatus());
    }

    //PUT
    @Test
    void shouldReturnUpdatedFilmSetWhenFilmWasUpdated() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        writer = new StringWriter();
        film.setId(1);
        mapper.writeValue(writer, Set.of(film));
        assertEquals(writer.toString(), getRequest(URN).getContentAsString());

        Film updatedFilm = new Film(1, "Name", "Updated Description", LocalDate.of(2000, 1, 1), 120);
        writer = new StringWriter();
        mapper.writeValue(writer, updatedFilm);
        putRequest(URN, writer.toString());

        writer = new StringWriter();
        mapper.writeValue(writer, Set.of(updatedFilm));
        assertEquals(writer.toString(), getRequest(URN).getContentAsString());
    }

    @Test
    void shouldReturnStatusCode404WhenUpdatingNonExistentFilm() throws Exception {
        Film film = new Film(1, "Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        assertEquals(404, putRequest(URN, writer.toString()).getStatus());
    }

    //likes
    @Test
    void shouldReturnLikedUsersIdSetWhenAddingLikeToFilm() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        User user = new User("email@qwe.ru", "login", "name", LocalDate.of(2000, 1, 1));
        writer = new StringWriter();
        mapper.writeValue(writer, user);
        postRequest("/users", writer.toString());

        putRequest(URN + "/1/like/1");

        film.setLikedUsersIds(Set.of(1));
        assertEquals(film.getLikedUsersIds(), mapper.readValue(getRequest(URN+"/1").getContentAsString(), Film.class).getLikedUsersIds());
    }

    @Test
    void shouldReturnStatusCode404WhenAddingLikeToFilmWithWrongId() throws Exception {
        assertEquals(404, putRequest(URN + "/1/like/1").getStatus());
    }

    @Test
    void shouldReturnStatusCode404WhenAddingLikeFromUserWithWrongId() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        assertEquals(404, putRequest(URN + "/1/like/1").getStatus());
    }

    @Test
    void shouldRemoveLike() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        User user = new User("email@qwe.ru", "login", "name", LocalDate.of(2000, 1, 1));
        writer = new StringWriter();
        mapper.writeValue(writer, user);
        postRequest("/users", writer.toString());

        putRequest(URN + "/1/like/1");

        writer = new StringWriter();
        mapper.writeValue(writer, user);
        postRequest("/users", writer.toString());

        putRequest(URN + "/1/like/2");

        film.setLikedUsersIds(Set.of(1, 2));
        assertEquals(film.getLikedUsersIds(), mapper.readValue(getRequest(URN+"/1").getContentAsString(), Film.class).getLikedUsersIds());

        deleteRequest(URN + "/1/like/2");

        film.setLikedUsersIds(Set.of(1));
        assertEquals(film.getLikedUsersIds(), mapper.readValue(getRequest(URN+"/1").getContentAsString(), Film.class).getLikedUsersIds());
    }

    @Test
    void shouldReturnStatusCode404WhenRemovingLikeFromFilmWithWrongId() throws Exception {
        assertEquals(404, deleteRequest(URN + "/1/like/1").getStatus());
    }

    @Test
    void shouldReturnStatusCode404WhenWhenRemovingLikeFromUserWithWrongId() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        assertEquals(404, deleteRequest(URN + "/1/like/1").getStatus());
    }

    @Test
    void shouldReturnSetWithOneFilmWhenAddingOneFilmAndGettingPopularFilmsRequestWithNoParameters() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        writer = new StringWriter();
        film.setId(1);
        mapper.writeValue(writer, Set.of(film));
        assertEquals(writer.toString(), getRequest(URN+"/popular").getContentAsString());
    }

    @Test
    void shouldReturnSetWithTenFilmsWhenAddingElevenFilmsAndGettingPopularFilmsRequestWithNoParameters() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
        for (int i = 0; i < 11; i++) {
            writer = new StringWriter();
            mapper.writeValue(writer, film);
            postRequest(URN, writer.toString());
        }

        writer = new StringWriter();
        Set<Film> set = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
            film.setId(i+1);
            set.add(film);
        }
        mapper.writeValue(writer, set);

        assertEquals(writer.toString(), getRequest(URN+"/popular").getContentAsString());
    }

    @Test
    void shouldReturnSetWithOneFilmWhenAddingTwoFilmsAndGettingPopularFilmsRequestWithParameter1() throws Exception {
        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
        for (int i = 0; i < 2; i++) {
            writer = new StringWriter();
            mapper.writeValue(writer, film);
            postRequest(URN, writer.toString());
        }

        writer = new StringWriter();
        film.setId(1);
        mapper.writeValue(writer, Set.of(film));
        assertEquals(writer.toString(), getRequest(URN+"/popular?count=1").getContentAsString());
    }

    @Test
    void shouldReturnOrderedFilmSetWhenGettingPopularFilmsRequest() throws Exception {
        Set<Film> expectedSet = new HashSet<>();

        Film film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
        film.setLikedUsersIds(Set.of(1, 2));
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());
        film.setId(1);
        expectedSet.add(film);

        film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120);
        film.setLikedUsersIds(Set.of(1));
        writer = new StringWriter();
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());
        film.setId(2);
        expectedSet.add(film);

        Set<Film> actualSet = mapper.readValue(getRequest(URN + "/popular").getContentAsString(), new TypeReference<>(){});
        assertEquals(expectedSet, actualSet);
    }

    //sending requests
    private MockHttpServletResponse getRequest(String urn) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get(urn);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
    private MockHttpServletResponse postRequest(String urn, String content) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post(urn).
                contentType(MediaType.APPLICATION_JSON).content(content);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
    private MockHttpServletResponse putRequest(String urn) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(urn);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
    private MockHttpServletResponse putRequest(String urn, String content) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(urn).
                contentType(MediaType.APPLICATION_JSON).content(content);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
    private MockHttpServletResponse deleteRequest(String urn) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete(urn);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
}