package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    private static final String SERVER_URI = "http://localhost:8080/users";

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
    void shouldReturnEmptyUserSetWhenNoUsersWereAdded() {
        response = getRequest();
        assertEquals("[]", response.body());
    }

    //POST
    @Test
    void shouldReturnUserSetWhenUserWasAdded() throws IOException {
        User user = new User(1, "email@", "login", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        postRequest(writer.toString());

        writer = new StringWriter();
        response = getRequest();
        mapper.writeValue(writer, Set.of(user));
        assertEquals(writer.toString(), response.body());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserThatWasAlreadyAdded() throws IOException {
        User user = new User(1, "email@", "login", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        postRequest(writer.toString());

        writer = new StringWriter();
        mapper.writeValue(writer, user);
        response = postRequest(writer.toString());
        assertEquals(500, response.statusCode());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserWithEmptyEmail() throws IOException {
        User user = new User(1, "", "login", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        response = postRequest(writer.toString());
        assertEquals(500, response.statusCode());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserWithWrongEmail() throws IOException {
        User user = new User(1, "email", "login", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        response = postRequest(writer.toString());
        assertEquals(500, response.statusCode());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserWithEmptyLogin() throws IOException {
        User user = new User(1, "email@", "", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        response = postRequest(writer.toString());
        assertEquals(500, response.statusCode());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserWithWrongLogin() throws IOException {
        User user = new User(1, "email@", " login ", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        response = postRequest(writer.toString());
        assertEquals(500, response.statusCode());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserWithWrongBirthday() throws IOException {
        User user = new User(1, "email@", "login", "name", LocalDate.of(2040, 1, 1));
        mapper.writeValue(writer, user);
        response = postRequest(writer.toString());
        assertEquals(500, response.statusCode());
    }

    //PUT
    @Test
    void shouldReturnUpdatedUserSetWhenUserWasUpdated() throws IOException {
        User user = new User(1, "email@", "login", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        postRequest(writer.toString());

        writer = new StringWriter();
        response = getRequest();
        mapper.writeValue(writer, Set.of(user));
        assertEquals(writer.toString(), response.body());

        User updatedUser = new User(1, "email@", "login", "updated name", LocalDate.of(2000, 1, 1));
        writer = new StringWriter();
        mapper.writeValue(writer, updatedUser);
        putRequest(writer.toString());

        writer = new StringWriter();
        response = getRequest();
        mapper.writeValue(writer, Set.of(updatedUser));
        assertEquals(writer.toString(), response.body());
    }

    @Test
    void shouldReturnStatusCode500WhenUpdatingNonExistentUser() throws IOException {
        User user = new User(1, "email@", "login", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
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

    private HttpResponse<String> postRequest(String user) {
        client = HttpClient.newHttpClient();
        this.uri = URI.create(SERVER_URI);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(user);
        request = HttpRequest.newBuilder().uri(this.uri).header("Content-Type", "application/json").POST(body).build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }
    }

    private HttpResponse<String> putRequest(String user) {
        client = HttpClient.newHttpClient();
        this.uri = URI.create(SERVER_URI);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(user);
        request = HttpRequest.newBuilder().uri(this.uri).header("Content-Type", "application/json").PUT(body).build();
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException();
        }
    }
}
