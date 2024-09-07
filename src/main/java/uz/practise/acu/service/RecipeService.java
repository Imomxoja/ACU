package uz.practise.acu.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.practise.acu.domain.entity.RecipeEntity;
import uz.practise.acu.domain.entity.mapper.RecipeEntityMapper;
import uz.practise.acu.domain.entity.reaction.RankingEntity;
import uz.practise.acu.domain.entity.user.UserEntity;
import uz.practise.acu.domain.request.RecipeRequest;
import uz.practise.acu.domain.response.BaseResponse;
import uz.practise.acu.domain.response.RecipeResponse;
import uz.practise.acu.repository.RankingRepository;
import uz.practise.acu.repository.RecipeRepository;
import uz.practise.acu.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final RankingRepository rankingRepository;
    private final RecipeEntityMapper mapper;

    public BaseResponse<RecipeResponse> save(RecipeRequest recipeRequest, MultipartFile file, UUID userId) {
        RecipeEntity recipe;
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            return BaseResponse.<RecipeResponse>builder()
                    .message("user not found")
                    .status(400)
                    .build();
        }
        try {
            RecipeEntity entity = mapper.toEntity(recipeRequest, file);
            entity.setUser(userEntity.get());
            recipe = recipeRepository.save(entity);
        } catch (Exception e) {
            return BaseResponse.<RecipeResponse>builder()
                    .message("something went wrong")
                    .status(400)
                    .build();
        }

        RecipeResponse response = mapper.toResponse(recipe);
        return BaseResponse.<RecipeResponse>builder()
                .data(response)
                .message("successfully saved")
                .status(200)
                .build();
    }

    public BaseResponse<List<RecipeResponse>> getAll() {
        List<RecipeEntity> recipes = recipeRepository.findAllByIsDeleted();
        List<RecipeResponse> all = new ArrayList<>();

        for (RecipeEntity recipe : recipes) {
            all.add(mapper.toResponse(recipe));
        }
        return BaseResponse.<List<RecipeResponse>>builder()
                .data(all)
                .build();
    }

    public BaseResponse<RecipeResponse> saveALikedRecipe(UUID userId, UUID recipeId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        Optional<RecipeEntity> recipe = recipeRepository.findById(recipeId);

        if (user.isPresent() && recipe.isPresent()) {
            UserEntity userEntity = user.get();
            RecipeEntity recipeEntity = recipe.get();
            RecipeEntity[] temp = new RecipeEntity[1];

            userEntity.getSavedRecipes().forEach(
                    recipeEntity1 -> {
                        if (recipeEntity1.getId().equals(recipeId)) {
                            temp[0] = recipeEntity1;
                        }
                    }
            );

            if (temp[0] != null) {
                return BaseResponse.<RecipeResponse>builder()
                        .message("recipe already exists")
                        .status(400)
                        .build();
            }
            userEntity.getSavedRecipes().add(recipeEntity);
            userRepository.save(userEntity);
            return BaseResponse.<RecipeResponse>builder()
                    .message("recipe saved successfully")
                    .status(200)
                    .build();
        }
        return BaseResponse.<RecipeResponse>builder()
                .message("user or recipe not found")
                .status(400)
                .build();
    }

    public BaseResponse<RecipeResponse> pushLike(UUID userId, UUID recipeId) {
        if (userId == null) {
            return BaseResponse.<RecipeResponse>builder()
                    .message("user not found")
                    .status(400)
                    .build();
        }

        Optional<RecipeEntity> recipe = recipeRepository.findById(recipeId);
        if (recipe.isPresent()) {
            recipe.get().getRanking().setLikes(recipe.get().getRanking().getLikes()+1);
            recipeRepository.save(recipe.get());
        }
        return BaseResponse.<RecipeResponse>builder()
                .message("recipe not found")
                .status(400)
                .build();
    }

    public BaseResponse<RecipeResponse> addComment(UUID userId, UUID recipeId, String comment) {
        if (comment.isEmpty() || comment.isBlank()) {
            return BaseResponse.<RecipeResponse>builder()
                    .message("comment can not be empty")
                    .status(400)
                    .build();
        }

        Optional<UserEntity> user = userRepository.findById(userId);
        Optional<RecipeEntity> recipe = recipeRepository.findById(recipeId);

        if (user.isEmpty() || recipe.isEmpty()) {
            return BaseResponse.<RecipeResponse>builder()
                    .message("user or recipe not found")
                    .status(400)
                    .build();
        }

        UserEntity userEntity = user.get();
        RecipeEntity recipeEntity = recipe.get();

        RankingEntity ranking = new RankingEntity();
        ranking.getComments().add(comment);
        ranking.setAuthor(userEntity);
        ranking.setCreatedAt(LocalDateTime.now());
        ranking.setRecipe(recipeEntity);
        rankingRepository.save(ranking);
        recipeRepository.save(recipeEntity);

        return BaseResponse.<RecipeResponse>builder()
                .status(200)
                .build();
    }

    public BaseResponse<List<RecipeResponse>> findByAKeyword(String keyword) {
        List<RecipeEntity> partialMatched = recipeRepository.findByKeyword(keyword);
        List<RecipeResponse> all = new ArrayList<>();
        for (RecipeEntity recipe : partialMatched) {
            all.add(mapper.toResponse(recipe));
        }
        return BaseResponse.<List<RecipeResponse>>builder()
                .data(all)
                .status(200)
                .build();
    }


    public BaseResponse<RecipeResponse> delete(UUID recipeId) {
        Optional<RecipeEntity> recipe = recipeRepository.findById(recipeId);
        if (recipe.isPresent()) {
            recipe.get().setIsNotDeleted(false);
            recipeRepository.save(recipe.get());
            return BaseResponse.<RecipeResponse>builder()
                    .message("recipe deleted successfully")
                    .status(200)
                    .build();
        }

        return BaseResponse.<RecipeResponse>builder()
                .message("recipe not found")
                .status(400)
                .build();
    }


    public BaseResponse<List<RecipeResponse>> giveAllInSaved(UUID userId) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        List<RecipeResponse> all = new ArrayList<>();


        if (userEntity.isPresent()) {
            List<RecipeEntity> recipes = userRepository.giveAllInSaved(userId);

            for (RecipeEntity recipe : recipes) {
                all.add(mapper.toResponse(recipe));
            }
            String message = all.isEmpty() ?
                    "you have no any saved recipes" : "recipes you saved";

            return BaseResponse.<List<RecipeResponse>>builder()
                    .data(all)
                    .message(message)
                    .status(200)
                    .build();
        }

        return BaseResponse.<List<RecipeResponse>>builder()
                .message("user not found")
                .status(400)
                .build();
    }


    @SneakyThrows
    public BaseResponse<RecipeResponse> update(RecipeRequest recipeRequest, UUID recipeId) {
        Optional<RecipeEntity> recipeOpt = recipeRepository.findById(recipeId);
        if (recipeOpt.isPresent()) {
            RecipeEntity recipe = recipeOpt.get();

            Optional.ofNullable(recipeRequest.getDescription()).ifPresent(recipe::setDescription);
            Optional.ofNullable(recipeRequest.getImage()).ifPresent(image -> {
                try {
                    recipe.setImage(image.getBytes());
                } catch (IOException e) {
                    throw new RuntimeException("Error setting image", e);
                }
            });
            Optional.ofNullable(recipeRequest.getName()).ifPresent(recipe::setName);
            Optional.ofNullable(recipeRequest.getCategory()).ifPresent(recipe::setCategory);

            recipeRepository.save(recipe);
            return BaseResponse.<RecipeResponse>builder()
                    .message("Recipe updated successfully")
                    .status(200)
                    .build();
        }
        return BaseResponse.<RecipeResponse>builder()
                .message("Recipe not found")
                .status(400)
                .build();
    }
}
