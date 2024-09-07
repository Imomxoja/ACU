package uz.practise.acu.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.practise.acu.domain.request.RecipeRequest;
import uz.practise.acu.domain.response.BaseResponse;
import uz.practise.acu.domain.response.RecipeResponse;
import uz.practise.acu.service.RecipeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping("/add")
    public BaseResponse<RecipeResponse> addRecipe(@RequestParam("image") MultipartFile file,
                                                  @ModelAttribute RecipeRequest recipeRequest,
                                                  HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        return recipeService.save(recipeRequest, file, userId);
    }

    @GetMapping("/get-all")
    public BaseResponse<List<RecipeResponse>> getAllRecipe() {
        return recipeService.getAll();
    }

    @GetMapping("/saved-recipes")
    public BaseResponse<RecipeResponse> savedRecipes(HttpSession session, @RequestParam UUID recipeId) {
        UUID userId = (UUID) session.getAttribute("userId");
        if (userId == null) {
            return BaseResponse.<RecipeResponse>builder()
                    .message("user not found")
                    .status(400)
                    .build();
        }

        return recipeService.saveALikedRecipe(userId, recipeId);
    }

    @PatchMapping("/liked")
    public BaseResponse<RecipeResponse> liked(HttpSession session, @RequestParam UUID recipeId) {
        UUID userId = (UUID) session.getAttribute("userId");

        return recipeService.pushLike(userId, recipeId);
    }

    @PatchMapping("/commenting")
    public BaseResponse<RecipeResponse> commenting(
            HttpSession session, @RequestParam UUID recipeId, @RequestParam String comment) {
        UUID userId = (UUID) session.getAttribute("userId");
        return recipeService.addComment(userId, recipeId, comment);
    }

    @GetMapping("/search")
    public BaseResponse<List<RecipeResponse>> search(@RequestParam String keyword) {
        return recipeService.findByAKeyword(keyword);
    }

    @DeleteMapping("/delete")
    public BaseResponse<RecipeResponse> delete(@RequestParam UUID recipeId) {
        return recipeService.delete(recipeId);
    }

    @GetMapping("/give-saved-recipes")
    public BaseResponse<List<RecipeResponse>> giveSavedRecipes(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        return recipeService.giveAllInSaved(userId);
    }

    @PatchMapping("/update")
    public BaseResponse<RecipeResponse> update(@ModelAttribute RecipeRequest recipeRequest,
                                               @Param("recipeId") UUID recipeId) {
       return recipeService.update(recipeRequest, recipeId);
    }

}
