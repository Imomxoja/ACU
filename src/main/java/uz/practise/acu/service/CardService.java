package uz.practise.acu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.practise.acu.domain.entity.CardEntity;
import uz.practise.acu.domain.entity.CourseEntity;
import uz.practise.acu.domain.entity.RecipeEntity;
import uz.practise.acu.domain.entity.mapper.CardEntityMapper;
import uz.practise.acu.domain.entity.user.UserEntity;
import uz.practise.acu.domain.request.card.CardRequest;
import uz.practise.acu.domain.response.BaseResponse;
import uz.practise.acu.domain.response.CardResponse;
import uz.practise.acu.repository.CardRepository;
import uz.practise.acu.repository.CourseRepository;
import uz.practise.acu.repository.RecipeRepository;
import uz.practise.acu.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final CourseRepository courseRepository;
    private final CardEntityMapper mapper;

    public BaseResponse<CardResponse> createCard(UUID userId, CardRequest cardRequest) {
        Optional<UserEntity> userEntity = userRepository.findById(userId);

        if (userEntity.isPresent()) {
            if (cardRepository.findByCardNumber(cardRequest.getCardNumber()).isEmpty()) {
                CardEntity card = mapper.toCardEntity(cardRequest);
                card.setBalance(10000D);
                card.setUser(userEntity.get());
                CardEntity saved = cardRepository.save(card);
                return BaseResponse.<CardResponse>builder()
                        .message("Your card has been added successfully")
                        .data(mapper.toCardResponse(saved))
                        .status(200)
                        .build();
            }
            return BaseResponse.<CardResponse>builder()
                    .message("Card already exists with this number")
                    .status(400)
                    .build();
        }

        return BaseResponse.<CardResponse>builder()
                .message("User not found")
                .status(400)
                .build();
    }

    public BaseResponse<CardResponse> makeTransaction(UUID userId, UUID id) {
        Optional<RecipeEntity> recipe = recipeRepository.findById(id);
        Optional<CourseEntity> course = courseRepository.findById(id);
        Optional<UserEntity> user = userRepository.findById(userId);

        if (recipe.isEmpty() && course.isEmpty())
            return BaseResponse.<CardResponse>builder()
                    .message("Content not found")
                    .status(400)
                    .build();
        if (user.isEmpty()) {
            return BaseResponse.<CardResponse>builder()
                    .message("User not found")
                    .status(400)
                    .build();
        }

        return (course.isPresent()) ?
                transactionProcess(user.get(), course.get().getUser(), course.get().getPrice()) :
                transactionProcess(user.get(), recipe.get().getUser(), recipe.get().getPrice());
    }

    private BaseResponse<CardResponse> transactionProcess(
            UserEntity sender, UserEntity receiver, Double price) {
        if (sender.getCard().getBalance() >= price) {
            sender.getCard().setBalance(sender.getCard().getBalance() - price);
            receiver.getCard().setBalance(receiver.getCard().getBalance() + price);
            userRepository.save(sender);
            userRepository.save(receiver);
            return BaseResponse.<CardResponse>builder()
                    .message("Successfully made")
                    .status(200)
                    .build();
        }
        return BaseResponse.<CardResponse>builder()
                .message("Balance not enough")
                .status(400)
                .build();
    }

    public BaseResponse<CardResponse> remove(UUID cardId) {
        cardRepository.findById(cardId).ifPresent(card -> {
            cardRepository.deleteById(cardId);
        });
        return BaseResponse.<CardResponse>builder()
                .message("Successfully removed")
                .status(200)
                .build();
    }
}
