package com.boiler.recipe;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/recipes/")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping("create")
    public RecipeDto create(@RequestBody RecipeDto request){
        return recipeService.create(request);
    }

    @GetMapping
    public Page<Recipe> getRecipes(@RequestParam int page, @RequestParam int size){
        return recipeService.getRecipes(page, size);
    }

    @GetMapping({"{id}"})
    public RecipeDto view(@PathVariable("id") String id){
        return recipeService.getRecipe(id);
    }

    @PutMapping("{id}/edit")
    public RecipeDto edit(@PathVariable("id") String id, @RequestBody RecipeDto updateRequest){
        return recipeService.update(id, updateRequest);
    }

    @DeleteMapping("{id}/delete")
    public String delete(@PathVariable("id") String id){
        recipeService.delete(id);
        return "redirect:/";
    }
}
