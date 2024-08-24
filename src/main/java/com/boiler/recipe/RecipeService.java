package com.boiler.recipe;

import com.boiler.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository){
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public RecipeDto create(RecipeDto request){
        var user = userRepository.findUserByUid(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return convertRecipe(recipeRepository.save(Recipe.builder()
                .uid(UUID.randomUUID().toString())
                .name(request.name())
                .description(request.description())
                .user(user)
                .build()));
    }

    @Transactional
    public RecipeDto update(String uid, RecipeDto request){
        var recipe = getRequiredRecipe(uid);

        recipe.setName(request.name());
        recipe.setDescription(request.description());
        return convertRecipe(recipeRepository.save(recipe));
    }

    private RecipeDto convertRecipe(Recipe recipe){
        return new RecipeDto(recipe.getUid(), recipe.getName(), recipe.getDescription(), recipe.getUser().getUid());
    }

    public RecipeDto getRecipe(String uid) {
        return recipeRepository.findRecipeByUid(uid)
                .map(this::convertRecipe)
                .orElseThrow();
    }

    public Page<Recipe> getRecipes(int page, int size){
        return recipeRepository.findAll(PageRequest.of(page, size));
    }

    private Recipe getRequiredRecipe(String uid){
        return recipeRepository.findRecipeByUid(uid)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    @Transactional
    public void delete(String uid){
        recipeRepository.deleteByUid(uid);
    }

}
