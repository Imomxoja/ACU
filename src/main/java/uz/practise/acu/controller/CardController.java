package uz.practise.acu.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import uz.practise.acu.domain.request.card.CardRequest;
import uz.practise.acu.domain.response.BaseResponse;
import uz.practise.acu.domain.response.CardResponse;
import uz.practise.acu.service.CardService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {
    private final CardService cardService;

    @PostMapping("/create")
    public BaseResponse<CardResponse> createCard(HttpSession session, @ModelAttribute CardRequest cardRequest) {
        UUID userId = (UUID) session.getAttribute("userId");
        return cardService.createCard(userId, cardRequest);
    }

    @PostMapping("/transaction")
    public BaseResponse<CardResponse> transaction(HttpSession session,
                                                  @Param("contentId") UUID id) {
        UUID userId = (UUID) session.getAttribute("userId");

        return cardService.makeTransaction(userId, id);
    }

    @DeleteMapping("/remove")
    public BaseResponse<CardResponse> remove(@Param("cardId") UUID cardId) {
        return cardService.remove(cardId);
    }



}
