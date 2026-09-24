package com.matej2.budget_lens.service;


import com.matej2.budget_lens.domain.dto.AuthenticationRequest;
import com.matej2.budget_lens.domain.dto.AuthenticationResponse;
import com.matej2.budget_lens.domain.dto.RegisterRequest;
import com.matej2.budget_lens.domain.entity.Role;
import com.matej2.budget_lens.domain.entity.Token;
import com.matej2.budget_lens.domain.entity.TokenType;
import com.matej2.budget_lens.domain.entity.User;
import com.matej2.budget_lens.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public void register(RegisterRequest request) {
        log.info("Register request for user {} received", request.email());

        User user = User.builder()
                .firstname(request.firstName())
                .lastname(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        if (userDetailService.findByEmail(request.email()) != null) {
            return;
        }
        userDetailService.addUser(user);
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.debug("Authenticating request for user {}", request.email());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User user = userDetailService.findByEmail(request.email());
        String jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return new AuthenticationResponse(
                jwtToken
        );
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .registeredUser(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
