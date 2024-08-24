package com.boiler.recipe.recipe;

import com.boiler.recipe.Recipe;
import com.boiler.recipe.RecipeDto;
import com.boiler.recipe.WebIntegrationTest;
import com.boiler.user.User;
import com.boiler.user.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class RecipeControllerTest extends WebIntegrationTest {

    @Test
    void shouldCreateRecipe() throws Exception {
        var user = userRepository.save(User.builder()
                .uid(String.valueOf(UUID.randomUUID()))
                .name("John")
                .email("john@email.com")
                .password("John13Not@Password)")
                .build());

        var request = new RecipeDto(UUID.randomUUID().toString(), "Crispy chicken",
                "Just make a chicken crispy and fry.", user.getUid());

        mockMvc.perform(post("/api/recipes/create")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(equalTo(request.name()))))
                .andExpect(jsonPath("$.description", is(equalTo(request.description()))))
                .andExpect(jsonPath("$.userId", equalTo(request.userId())));
    }

    @Test
    void shouldFindRecipe() throws Exception {
        var user = userRepository.save(User.builder()
                .uid(String.valueOf(UUID.randomUUID()))
                .name("John")
                .email("john@email.com")
                .password("John13Not@Password)")
                .build());

        var recipe = recipeRepository.save(Recipe.builder()
                .uid(UUID.randomUUID().toString())
                .name("Crispy chicken")
                .description("Just make the chicken crispy and fry.")
                .user(user)
                .build());

        mockMvc.perform(get("/api/recipes/{id}", recipe.getUid())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.name", equalTo(recipe.getName())));
    }

    @Test
    void shouldUpdateRecipe() throws Exception {
        var user = userRepository.save(User.builder()
                .uid(String.valueOf(UUID.randomUUID()))
                .name("John")
                .email("john@email.com")
                .password("John13Not@Password)")
                .build());

        var recipe = recipeRepository.save(Recipe.builder()
                .uid(UUID.randomUUID().toString())
                .name("Crispy chicken")
                .description("Just make the chicken crispy and fry.")
                .user(user)
                .build());


        var updateRequest = new RecipeDto(recipe.getUid(), "Crispy chicken",
                "Just make the chicken crispy and fry.", user.getUid());

        var updatedBody = mockMvc.perform(put("/api/recipes/{id}/edit", updateRequest.id())
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
    void shouldDeleteRecipe() throws Exception {
        var user = userRepository.save(User.builder()
                .uid(String.valueOf(UUID.randomUUID()))
                .name("John")
                .email("john@email.com")
                .password("John13Not@Password)")
                .build());

        var recipe = recipeRepository.save(Recipe.builder()
                .uid(UUID.randomUUID().toString())
                .name("Crispy chicken")
                .description("Just make the chicken crispy and fry.")
                .user(user)
                .build());

        mockMvc.perform(delete("/api/recipes/{id}/delete", recipe.getUid())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
