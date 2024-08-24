package com.boiler.recipe.user;

import com.boiler.recipe.WebIntegrationTest;
import com.boiler.user.User;
import com.boiler.user.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends WebIntegrationTest {

    @Test
    void shouldCreateUser() throws Exception {
        var request = new UserDto(null, "Test" + UUID.randomUUID(), "email", "password");

        var body = mockMvc.perform(post("/user/create")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", equalTo(request.id())))
                .andExpect(jsonPath("$.name", equalTo(request.name())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var entityId = objectMapper.readValue(body, UserDto.class).id();
        var persistedUser = userRepository.findUserByUid(entityId).orElseThrow();

        assertThat(persistedUser.getName(), equalTo(request.name()));
        assertThat(persistedUser.getEmail(), equalTo(request.email()));
        assertThat(persistedUser.getPassword(), equalTo(request.password()));
        assertThat(persistedUser.getCreatedAt(), is(notNullValue()));
        assertThat(persistedUser.getUpdatedAt(), is(notNullValue()));
    }

    @Test
    void shouldFindUser() throws Exception {
        var person = userRepository.save(User.builder()
                .uid(UUID.randomUUID().toString())
                .name("Test")
                .email("test@email.com")
                .password("password")
                .build());

        mockMvc.perform(get("/user/profile/{id}", person.getUid()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", equalTo(person.getName())));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        var person = userRepository.save(User.builder()
                .uid(UUID.randomUUID().toString())
                .name("Test")
                .email("test@email.com")
                .password("password")
                .build());

        var updateRequest = new UserDto(null, "Test1", "email152@email.com", "password123");

        var updatedBody = mockMvc.perform(put("/user/profile/{id}/edit", person.getUid())
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var updatedPerson = objectMapper.readValue(updatedBody, UserDto.class);

        assertThat(updatedPerson.name(), equalTo(updateRequest.name()));
    }

    @Test
    @Transactional
    void shouldDeleteUser() throws Exception {
        var person = userRepository.save(User.builder()
                .uid(UUID.randomUUID().toString())
                .name("Test")
                .build());

        var body = mockMvc.perform(delete("/user/profile/{id}/delete", person.getUid())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

    }
}