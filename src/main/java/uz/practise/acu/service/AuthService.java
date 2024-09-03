package uz.practise.acu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.practise.acu.domain.entity.user.UserEntity;
import uz.practise.acu.domain.request.UserLoginRequest;
import uz.practise.acu.domain.request.UserRequest;
import uz.practise.acu.domain.response.AuthenticationResponse;
import uz.practise.acu.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("user not found with this email -> " + email));
    }


    public AuthenticationResponse register(UserRequest request) {
        UserEntity user = UserEntity.builder()
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();

        repository.save(user);
        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(token).build();
    }

    public AuthenticationResponse authenticate(UserLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserEntity user = repository.findByEmail(request.getEmail()).orElseThrow();

        String token = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(token).build();
    }
}
