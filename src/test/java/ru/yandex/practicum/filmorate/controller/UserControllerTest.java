package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
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
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    private static final String URN = "/users";

    private static ObjectMapper mapper;
    private StringWriter writer;
    private User user;

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
        user = new User("email@qwe.ru", "login", "name", LocalDate.of(2000, 1, 1));
        writer = new StringWriter();
    }
    @AfterEach
    void afterEach() throws Exception {
        deleteRequest(URN);
        deleteRequest("/films");
    }

    //storage
    //GET
    @Test
    void shouldReturnEmptyUserSetWhenNoUsersWereAdded() throws Exception {
        assertEquals("[]", getRequest(URN).getContentAsString());
    }

    @Test
    void shouldReturnUserWhenUserWasAdded() throws Exception {
        mapper.writeValue(writer, user);
        postRequest(URN, writer.toString());

        writer = new StringWriter();
        user.setId(1);
        mapper.writeValue(writer, user);
        assertEquals(writer.toString(), getRequest(URN + "/1").getContentAsString());
    }

    @Test
    void shouldReturnStatusCode404WhenGettingUserWithWrongId() throws Exception {
        assertEquals(404, getRequest(URN + "/1").getStatus());
    }

    //POST
    @Test
    void shouldReturnUserSetWhenUserWasAdded() throws Exception {
        mapper.writeValue(writer, user);
        postRequest(URN, writer.toString());

        writer = new StringWriter();
        user.setId(1);
        mapper.writeValue(writer, Set.of(user));
        assertEquals(writer.toString(), getRequest(URN).getContentAsString());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserWithEmptyEmail() throws Exception {
        user.setEmail("");
        mapper.writeValue(writer, user);
        assertEquals(500, postRequest(URN, writer.toString()).getStatus());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserWithWrongEmail() throws Exception {
        user.setEmail("email");
        mapper.writeValue(writer, user);
        assertEquals(500, postRequest(URN, writer.toString()).getStatus());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserWithEmptyLogin() throws Exception {
        user.setLogin("");
        mapper.writeValue(writer, user);
        assertEquals(500, postRequest(URN, writer.toString()).getStatus());
    }

    @Test
    void shouldReturnStatusCode400WhenAddingUserWithWrongLogin() throws Exception {
        user.setLogin(" login ");
        mapper.writeValue(writer, user);
        assertEquals(400, postRequest(URN, writer.toString()).getStatus());
    }

    @Test
    void shouldReturnUserSetWithUserNameEqualsToHisLoginWhenAddingUserWithEmptyName() throws Exception {
        user.setName("");
        mapper.writeValue(writer, user);
        postRequest(URN, writer.toString());

        writer = new StringWriter();
        user.setId(1);
        user.setName(user.getLogin());
        mapper.writeValue(writer, Set.of(user));
        assertEquals(writer.toString(), getRequest(URN).getContentAsString());
    }

    @Test
    void shouldReturnStatusCode500WhenAddingUserWithWrongBirthday() throws Exception {
        user.setBirthday(LocalDate.of(2040, 1, 1));
        mapper.writeValue(writer, user);
        assertEquals(500, postRequest(URN, writer.toString()).getStatus());
    }

    //PUT
    @Test
    void shouldReturnUpdatedUserSetWhenUserWasUpdated() throws Exception {
        mapper.writeValue(writer, user);
        postRequest(URN, writer.toString());

        writer = new StringWriter();
        user.setId(1);
        mapper.writeValue(writer, Set.of(user));
        assertEquals(writer.toString(), getRequest(URN).getContentAsString());

        User updatedUser = new User(1, "email@qwe.ru", "login", "updated name", LocalDate.of(2000, 1, 1));
        writer = new StringWriter();
        mapper.writeValue(writer, updatedUser);
        putRequest(URN, writer.toString());

        writer = new StringWriter();
        mapper.writeValue(writer, Set.of(updatedUser));
        assertEquals(writer.toString(), getRequest(URN).getContentAsString());
    }

    @Test
    void shouldReturnStatusCode404WhenUpdatingNonExistentUser() throws Exception {
        user.setId(1);
        mapper.writeValue(writer, user);
        assertEquals(404, putRequest(URN, writer.toString()).getStatus());
    }

    //friends
    @Test
    void shouldAddFriends() throws Exception {
        mapper.writeValue(writer, user);
        postRequest(URN, writer.toString());

        user = new User("email@qwe.ru", "login", "name", LocalDate.of(2000, 1, 1));
        writer = new StringWriter();
        mapper.writeValue(writer, user);
        postRequest(URN, writer.toString());

        putRequest(URN + "/1/friends/2");

        user.setFriendsIds(Set.of(2));
        Set<User> actualSet = mapper.readValue(getRequest(URN + "/1/friends").getContentAsString(), new TypeReference<>(){});
        assertEquals(user.getFriendsIds(), actualSet.stream().map(User::getId).collect(Collectors.toSet()));

        user.setFriendsIds(Collections.emptySet());
        actualSet = mapper.readValue(getRequest(URN + "/2/friends").getContentAsString(), new TypeReference<>(){});
        assertEquals(user.getFriendsIds(), actualSet.stream().map(User::getId).collect(Collectors.toSet()));
    }

    @Test
    void shouldReturnStatusCode404WhenUserWithWrongIdAddingFriend() throws Exception {
        assertEquals(404, putRequest(URN + "/1/friends/2").getStatus());
    }

    @Test
    void shouldReturnStatusCode404WheAddingFriendWithWrongId() throws Exception {
        mapper.writeValue(writer, user);
        postRequest(URN, writer.toString());

        assertEquals(404, putRequest(URN + "/1/friends/2").getStatus());
    }

    @Test
    void shouldReturnUserSetWhenGettingFriends() throws Exception {
        mapper.writeValue(writer, user);
        postRequest(URN, writer.toString());

        User anotherUser = new User("email@qwe.ru", "login", "name", LocalDate.of(2000, 1, 1));
        writer = new StringWriter();
        mapper.writeValue(writer, anotherUser);
        postRequest(URN, writer.toString());

        putRequest(URN + "/1/friends/2");

        anotherUser.setId(2);
        writer = new StringWriter();
        mapper.writeValue(writer, Set.of(anotherUser));
        assertEquals(writer.toString(), getRequest(URN + "/1/friends").getContentAsString());
    }

    @Test
    void shouldReturnStatusCode404WhenGettingFriendsFromUserWithWrongId() throws Exception {
        assertEquals(404, getRequest(URN + "/1/friends").getStatus());
    }

    @Test
    void shouldReturnEmptySetWhenGettingMutualFriendsFromUsersWithNoMutualFriends() throws Exception {
        mapper.writeValue(writer, user);
        postRequest(URN, writer.toString());

        user = new User("email@qwe.ru", "login", "name", LocalDate.of(2000, 1, 1));
        writer = new StringWriter();
        mapper.writeValue(writer, user);
        postRequest(URN, writer.toString());

        assertEquals("[]", getRequest(URN + "/1/friends/common/2").getContentAsString());
    }

    @Test
    void shouldReturnSetWithMutualFriendsWhenGettingMutualFriendsFromUsersWithMutualFriends() throws Exception {
        for (int i = 0; i < 3; i++) {
            writer = new StringWriter();
            mapper.writeValue(writer, user);
            postRequest(URN, writer.toString());
        }
        user.setId(3);
        user.setFriendsIds(Set.of(1, 2));

        putRequest(URN + "/1/friends/3");
        putRequest(URN + "/2/friends/3");

        Set<User> expectedSet = Set.of(user);
        Set<User> actualSet = mapper.readValue(getRequest(URN + "/1/friends/common/2").getContentAsString(), new TypeReference<>(){});
        assertEquals(expectedSet, actualSet);
    }

    @Test
    void shouldReturnStatusCode404WhenGettingMutualFriendsFromUserWithWrongId() throws Exception {
        assertEquals(404, getRequest(URN + "/1/friends/common/2").getStatus());
    }

    @Test
    void shouldReturnStatusCode404WhenGettingMutualFriendsFromAnotherUserWithWrongId() throws Exception {
        mapper.writeValue(writer, user);
        postRequest(URN, writer.toString());

        assertEquals(404, getRequest(URN + "/1/friends/common/2").getStatus());
    }

    @Test
    void shouldDeleteFriend() throws Exception {
        for (int i = 0; i < 2; i++) {
            mapper.writeValue(writer, user);
            postRequest(URN, writer.toString());
        }

        putRequest(URN + "/1/friends/2");

        Set<User> actualSet = mapper.readValue(getRequest(URN + "/1/friends").getContentAsString(), new TypeReference<>(){});
        assertEquals(Set.of(2), actualSet.stream().map(User::getId).collect(Collectors.toSet()));

        deleteRequest(URN + "/1/friends/2");

        actualSet = mapper.readValue(getRequest(URN + "/1/friends").getContentAsString(), new TypeReference<>(){});
        assertEquals(Collections.emptySet(),actualSet.stream().map(User::getId).collect(Collectors.toSet()));
    }

    @Test
    void shouldReturnStatusCode404WhenDeletingFriendFromUserWithWrongId() throws Exception {
        assertEquals(404, deleteRequest(URN + "/1/friends/2").getStatus());
    }

    @Test
    void shouldReturnStatusCode404WhenDeletingFriendFromAnotherUserWithWrongId() throws Exception {
        mapper.writeValue(writer, user);
        postRequest(URN, writer.toString());

        assertEquals(404, deleteRequest(URN + "/1/friends/2").getStatus());
    }

    //sending requests
    private MockHttpServletResponse getRequest(String urn) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get(urn);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
    private MockHttpServletResponse postRequest(String urn, String user) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.post(urn).
                contentType(MediaType.APPLICATION_JSON).content(user);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
    private MockHttpServletResponse putRequest(String urn) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(urn);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
    private MockHttpServletResponse putRequest(String urn, String user) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.put(urn).
                contentType(MediaType.APPLICATION_JSON).content(user);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
    private MockHttpServletResponse deleteRequest(String urn) throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete(urn);
        MvcResult result = mvc.perform(request).andReturn();
        return result.getResponse();
    }
}