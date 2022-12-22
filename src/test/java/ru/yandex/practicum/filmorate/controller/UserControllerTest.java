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
import ru.yandex.practicum.filmorate.model.User;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private static final String URN = "/users";

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
        deleteUserSetRequest();
    }

    //GET
    @Test
    void shouldReturnEmptyUserSetWhenNoUsersWereAdded() throws Exception {
        assertEquals("[]", getUserSetRequest().getContentAsString());
    }

    //POST
    @Test
    void shouldReturnUserSetWhenUserWasAdded() throws Exception {
        User user = new User("email@qwe.ru", "login", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        postUserRequest(writer.toString());

        writer = new StringWriter();
        user.setId(1);
        mapper.writeValue(writer, Set.of(user));
        assertEquals(writer.toString(), getUserSetRequest().getContentAsString());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserWithEmptyEmail() throws Exception {
        User user = new User("", "login", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        assertEquals(500, postUserRequest(writer.toString()).getStatus());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserWithWrongEmail() throws Exception {
        User user = new User("email", "login", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        assertEquals(500, postUserRequest(writer.toString()).getStatus());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserWithEmptyLogin() throws Exception {
        User user = new User("email@qwe.ru", "", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        assertEquals(500, postUserRequest(writer.toString()).getStatus());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserWithWrongLogin() throws Exception {
        User user = new User("email@", " login ", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        assertEquals(500, postUserRequest(writer.toString()).getStatus());
    }

    @Test
    void shouldReturnUserSetWithUserNameEqualsToHisLoginWhenAddingUserWithEmptyName() throws Exception {
        User user = new User("email@qwe.ru", "login", "", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        postUserRequest(writer.toString());

        writer = new StringWriter();
        user.setId(1);
        user.setName(user.getLogin());
        mapper.writeValue(writer, Set.of(user));
        assertEquals(writer.toString(), getUserSetRequest().getContentAsString());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserWithWrongBirthday() throws Exception {
        User user = new User("email@", "login", "name", LocalDate.of(2040, 1, 1));
        mapper.writeValue(writer, user);
        assertEquals(500, postUserRequest(writer.toString()).getStatus());
    }

    //PUT
    @Test
    void shouldReturnUpdatedUserSetWhenUserWasUpdated() throws Exception {
        User user = new User("email@qwe.ru", "login", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        postUserRequest(writer.toString());

        writer = new StringWriter();
        user.setId(1);
        mapper.writeValue(writer, Set.of(user));
        assertEquals(writer.toString(), getUserSetRequest().getContentAsString());

        User updatedUser = new User(1, "email@qwe.ru", "login", "updated name", LocalDate.of(2000, 1, 1));
        writer = new StringWriter();
        mapper.writeValue(writer, updatedUser);
        putUserRequest(writer.toString());

        writer = new StringWriter();
        mapper.writeValue(writer, Set.of(updatedUser));
        assertEquals(writer.toString(), getUserSetRequest().getContentAsString());
    }

    @Test
    void shouldReturnStatusCode500WhenUpdatingNonExistentUser() throws Exception {
        User user = new User(1, "email@", "login", "name", LocalDate.of(2000, 1, 1));
        mapper.writeValue(writer, user);
        assertEquals(500, putUserRequest(writer.toString()).getStatus());
    }

    //sending requests
    private MockHttpServletResponse getUserSetRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get(URN);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
    private MockHttpServletResponse postUserRequest(String user) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post(URN).
                contentType(MediaType.APPLICATION_JSON).content(user);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
    private MockHttpServletResponse putUserRequest(String user) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(URN).
                contentType(MediaType.APPLICATION_JSON).content(user);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
    private MockHttpServletResponse deleteUserSetRequest() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete(URN);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
}