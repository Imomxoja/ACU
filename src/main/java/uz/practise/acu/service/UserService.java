package uz.practise.acu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.practise.acu.domain.entity.mapper.UserEntityMapper;
import uz.practise.acu.domain.entity.user.Role;
import uz.practise.acu.domain.entity.user.UserEntity;
import uz.practise.acu.domain.response.BaseResponse;
import uz.practise.acu.domain.response.UserResponse;
import uz.practise.acu.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserEntityMapper mapper;

    public BaseResponse<UserResponse> giveRankForUser(double rank, UUID userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        double average = 0d;

        if (user.isPresent()) {
            user.get().getRanks().add(rank);

            for (Double v : user.get().getRanks()) {
                average += v;
            }
            average = average / user.get().getRanks().size();
            user.get().setAverageRating(average);

            if (user.get().getRanks().size() == 5 && user.get().getAverageRating() >= 4) {
                user.get().setRole(Role.PROFESSIONAL);
            }
            userRepository.save(user.get());

            return BaseResponse.<UserResponse>builder()
                    .message("User got ranked successfully!")
                    .status(200)
                    .build();
        }

        return BaseResponse.<UserResponse>builder()
                .message("User not found")
                .status(400)
                .build();
    }
}
