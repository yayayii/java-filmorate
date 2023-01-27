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
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    private static final String URN = "/films";

    private static ObjectMapper mapper;
    private StringWriter writer;
    private Film film;

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
        film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120, Mpa.G, Set.of(Genre.COMEDY));
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
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        writer = new StringWriter();
        film.setId(1);
        mapper.writeValue(writer, film);
        assertEquals(writer.toString(), getRequest(URN + "/1").getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    void shouldReturnStatusCode404WhenGettingFilmWithWrongId() throws Exception {
        assertEquals(404, getRequest(URN + "/1").getStatus());
    }

    //POST
    @Test
    void shouldReturnFilmSetWhenFilmWasAdded() throws Exception {
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        writer = new StringWriter();
        film.setId(1);
        mapper.writeValue(writer, Set.of(film));
        assertEquals(writer.toString(), getRequest(URN).getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    void shouldReturnStatusCode500WhenAddingFilmWithEmptyName() throws Exception {
        film.setName("");
        mapper.writeValue(writer, film);
        assertEquals(500, postRequest(URN, writer.toString()).getStatus());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingFilmWithLongDescription() throws Exception {
        film.setDescription("DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription");
        mapper.writeValue(writer, film);
        assertEquals(500, postRequest(URN, writer.toString()).getStatus());
    }

    @Test
    void shouldReturnStatusCode400WhenAddingFilmWithReleaseDateBefore28_10_1985() throws Exception {
        film.setReleaseDate(LocalDate.of(1894, 1, 1));
        mapper.writeValue(writer, film);
        assertEquals(400, postRequest(URN, writer.toString()).getStatus());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingFilmWithNegativeDuration() throws Exception {
        film.setDuration(-1);
        mapper.writeValue(writer, film);
        assertEquals(500, postRequest(URN, writer.toString()).getStatus());
    }

    //PUT
    @Test
    void shouldReturnUpdatedFilmSetWhenFilmWasUpdated() throws Exception {
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        writer = new StringWriter();
        film.setId(1);
        mapper.writeValue(writer, Set.of(film));
        assertEquals(writer.toString(), getRequest(URN).getContentAsString(StandardCharsets.UTF_8));

        Film updatedFilm = new Film(1, "Name", "Updated Description", LocalDate.of(2000, 1, 1), 120, Mpa.G, Set.of(Genre.COMEDY));
        writer = new StringWriter();
        mapper.writeValue(writer, updatedFilm);
        putRequest(URN, writer.toString());

        writer = new StringWriter();
        mapper.writeValue(writer, Set.of(updatedFilm));
        assertEquals(writer.toString(), getRequest(URN).getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    void shouldReturnStatusCode404WhenUpdatingNonExistentFilm() throws Exception {
        film.setId(1);
        mapper.writeValue(writer, film);
        assertEquals(404, putRequest(URN, writer.toString()).getStatus());
    }

    //likes
    @Test
    void shouldReturnLikedUsersIdSetWhenAddingLikeToFilm() throws Exception {
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        User user = new User("email@qwe.ru", "login", "name", LocalDate.of(2000, 1, 1));
        writer = new StringWriter();
        mapper.writeValue(writer, user);
        postRequest("/users", writer.toString());

        putRequest(URN + "/1/like/1");

        film.setLikedUsersIds(Set.of(1));
        Set<Integer> actualSet = mapper.readValue(getRequest(URN + "/1/like").getContentAsString(StandardCharsets.UTF_8), new TypeReference<>(){});
        assertEquals(film.getLikedUsersIds(), actualSet);
    }

    @Test
    void shouldReturnStatusCode404WhenAddingLikeToFilmWithWrongId() throws Exception {
        assertEquals(404, putRequest(URN + "/1/like/1").getStatus());
    }

    @Test
    void shouldReturnStatusCode404WhenAddingLikeFromUserWithWrongId() throws Exception {
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        assertEquals(404, putRequest(URN + "/1/like/1").getStatus());
    }

    @Test
    void shouldRemoveLike() throws Exception {
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
        Set<Integer> actualSet = mapper.readValue(getRequest(URN + "/1/like").getContentAsString(StandardCharsets.UTF_8), new TypeReference<>(){});
        assertEquals(film.getLikedUsersIds(), actualSet);


        deleteRequest(URN + "/1/like/2");

        film.setLikedUsersIds(Set.of(1));
        actualSet = mapper.readValue(getRequest(URN + "/1/like").getContentAsString(StandardCharsets.UTF_8), new TypeReference<>(){});
        assertEquals(film.getLikedUsersIds(), actualSet);
    }

    @Test
    void shouldReturnStatusCode404WhenRemovingLikeFromFilmWithWrongId() throws Exception {
        assertEquals(404, deleteRequest(URN + "/1/like/1").getStatus());
    }

    @Test
    void shouldReturnStatusCode404WhenWhenRemovingLikeFromUserWithWrongId() throws Exception {
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        assertEquals(404, deleteRequest(URN + "/1/like/1").getStatus());
    }

    @Test
    void shouldReturnSetWithOneFilmWhenAddingOneFilmAndGettingPopularFilmsRequestWithNoParameters() throws Exception {
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());

        writer = new StringWriter();
        film.setId(1);
        mapper.writeValue(writer, Set.of(film));
        assertEquals(writer.toString(), getRequest(URN+"/popular").getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    void shouldReturnSetWithTenFilmsWhenAddingElevenFilmsAndGettingPopularFilmsRequestWithNoParameters() throws Exception {
        for (int i = 0; i < 11; i++) {
            writer = new StringWriter();
            mapper.writeValue(writer, film);
            postRequest(URN, writer.toString());
        }

        writer = new StringWriter();
        Set<Film> set = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120, Mpa.G, Set.of(Genre.COMEDY));
            film.setId(i+1);
            set.add(film);
        }
        mapper.writeValue(writer, set);

        assertEquals(writer.toString(), getRequest(URN+"/popular").getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    void shouldReturnSetWithOneFilmWhenAddingTwoFilmsAndGettingPopularFilmsRequestWithParameter1() throws Exception {
        for (int i = 0; i < 2; i++) {
            writer = new StringWriter();
            mapper.writeValue(writer, film);
            postRequest(URN, writer.toString());
        }

        writer = new StringWriter();
        film.setId(1);
        mapper.writeValue(writer, Set.of(film));
        assertEquals(writer.toString(), getRequest(URN+"/popular?count=1").getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    void shouldReturnOrderedFilmSetWhenGettingPopularFilmsRequest() throws Exception {
        Set<Film> expectedSet = new HashSet<>();

        film.setLikedUsersIds(Set.of(1, 2));
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());
        film.setId(1);
        expectedSet.add(film);

        film = new Film("Name", "Description", LocalDate.of(2000, 1, 1), 120, Mpa.G, Set.of(Genre.COMEDY));
        film.setLikedUsersIds(Set.of(1));
        writer = new StringWriter();
        mapper.writeValue(writer, film);
        postRequest(URN, writer.toString());
        film.setId(2);
        expectedSet.add(film);

        Set<Film> actualSet = mapper.readValue(getRequest(URN + "/popular").getContentAsString(StandardCharsets.UTF_8), new TypeReference<>(){});
        assertEquals(expectedSet, actualSet);
    }

    //mpa
    @Test
    void shouldReturnGWhenGettingMpaWithId1() throws Exception {
        mapper.writeValue(writer, Mpa.G);
        assertEquals(writer.toString(), getRequest("/mpa/1").getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    void shouldReturnStatusCode404WhenGettingMpaWithWrongId() throws Exception {
        assertEquals(404, getRequest("/mpa/999").getStatus());
    }

    @Test
    void shouldReturnAllMpas() throws Exception {
        mapper.writeValue(writer, Mpa.values());
        assertEquals(writer.toString(), getRequest("/mpa").getContentAsString(StandardCharsets.UTF_8));
    }

    //genres
    @Test
    void shouldReturnComedyWhenGettingGenreWithId1() throws Exception {
        mapper.writeValue(writer, Genre.COMEDY);
        assertEquals(writer.toString(), getRequest("/genres/1").getContentAsString(StandardCharsets.UTF_8));
    }

    @Test
    void shouldReturnStatusCode404WhenGettingGenreWithWrongId() throws Exception {
        assertEquals(404, getRequest("/genres/999").getStatus());
    }

    @Test
    void shouldReturnAllGenres() throws Exception {
        mapper.writeValue(writer, Genre.values());
        assertEquals(writer.toString(), getRequest("/genres").getContentAsString(StandardCharsets.UTF_8));
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