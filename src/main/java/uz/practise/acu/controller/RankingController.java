package uz.practise.acu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import uz.practise.acu.domain.response.BaseResponse;
import uz.practise.acu.domain.response.UserResponse;
import uz.practise.acu.service.RecipeService;
import uz.practise.acu.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/content/ranking")
public class RankingController {
    private final RecipeService recipeService;
    private final UserService userService;

    @GetMapping("/get-users-for-ranking")
    public BaseResponse<List<UserResponse>> getUsersForRanking() {
        return recipeService.getUsersForRanking();
    }

    @PostMapping("/give-rank")
    public BaseResponse<UserResponse> giveRank(@Param("rank") double rank, @Param("userId")UUID userId) {
        BaseResponse<UserResponse> response = userService.giveRankForUser(rank, userId);
        getUsersForRanking();
        return response;
    }
}
