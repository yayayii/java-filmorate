package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    private static final String SERVER_URI = "http://localhost:8080/films";

    private HttpClient client;
    private URI uri;
    private HttpRequest request;
    private HttpResponse<String> response;
    private static ObjectMapper mapper;
    private StringWriter writer;

    @BeforeAll
    static void beforeAll() {
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
    }

    @BeforeEach
    void beforeEach() {
        writer = new StringWriter();
        SpringApplication.run(FilmorateApplication.class);
    }

    //GET
    @Test
    void shouldReturnEmptyFilmsSetWhenNoFilmsWereAdded() {
        response = getRequest();
        assertEquals("[]", response.body());
    }

    //POST
    @Test
    void shouldReturnFilmsSetWhenFilmWasAdded() throws IOException {
        Film film = new Film(1, "Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        postRequest(writer.toString());

        writer = new StringWriter();
        response = getRequest();
        mapper.writeValue(writer, Set.of(film));
        assertEquals(writer.toString(), response.body());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingFilmThatWasAlreadyAdded() throws IOException {
        Film film = new Film(1, "Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        postRequest(writer.toString());

        writer = new StringWriter();
        mapper.writeValue(writer, film);
        response = postRequest(writer.toString());
        assertEquals(500, response.statusCode());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingFilmWithEmptyName() throws IOException {
        Film film = new Film(1, "", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        response = postRequest(writer.toString());
        assertEquals(500, response.statusCode());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingFilmWithLongDescription() throws IOException {
        Film film = new Film(1, "Name", "DescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescriptionDescription", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        response = postRequest(writer.toString());
        assertEquals(500, response.statusCode());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingFilmWithReleaseDateBefore28101985() throws IOException {
        Film film = new Film(1, "Name", "Description", LocalDate.of(1894, 1, 1), 120);
        mapper.writeValue(writer, film);
        response = postRequest(writer.toString());
        assertEquals(500, response.statusCode());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingFilmWithNegativeDuration() throws IOException {
        Film film = new Film(1, "Name", "Description", LocalDate.of(2000, 1, 1), -1);
        mapper.writeValue(writer, film);
        response = postRequest(writer.toString());
        assertEquals(500, response.statusCode());
    }

    //PUT
    @Test
    void shouldReturnUpdatedFilmsSetWhenFilmWasUpdated() throws IOException {
        Film film = new Film(1, "Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        postRequest(writer.toString());

        writer = new StringWriter();
        response = getRequest();
        mapper.writeValue(writer, Set.of(film));
        assertEquals(writer.toString(), response.body());

        Film updatedFilm = new Film(1, "Name", "Updated Description", LocalDate.of(2000, 1, 1), 120);
        writer = new StringWriter();
        mapper.writeValue(writer, updatedFilm);
        putRequest(writer.toString());

        writer = new StringWriter();
        response = getRequest();
        mapper.writeValue(writer, Set.of(updatedFilm));
        assertEquals(writer.toString(), response.body());
    }

    @Test
    void shouldReturnStatusCode500WhenUpdatingNonExistentFilm() throws IOException {
        Film film = new Film(1, "Name", "Description", LocalDate.of(2000, 1, 1), 120);
        mapper.writeValue(writer, film);
        response = putRequest(writer.toString());
        assertEquals(500, response.statusCode());
    }

    private HttpResponse<String> getRequest() {
        client = HttpClient.newHttpClient();
        this.uri = URI.create(SERVER_URI);
        request = HttpRequest.newBuilder().uri(this.uri).GET().build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpResponse<String> postRequest(String film) {
        client = HttpClient.newHttpClient();
        this.uri = URI.create(SERVER_URI);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(film);
        request = HttpRequest.newBuilder().uri(this.uri).header("Content-Type", "application/json").POST(body).build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }
    }

    private HttpResponse<String> putRequest(String film) {
        client = HttpClient.newHttpClient();
        this.uri = URI.create(SERVER_URI);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(film);
        request = HttpRequest.newBuilder().uri(this.uri).header("Content-Type", "application/json").PUT(body).build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }
    }
}
