package com.boiler.recipe;

public record RecipeDto(
        String id,
        String name,
        String description,
        String userId
) {
}
