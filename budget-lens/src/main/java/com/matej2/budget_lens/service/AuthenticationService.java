package com.matej2.budget_lens.service;


import com.matej2.budget_lens.domain.dto.request.AuthenticationRequest;
import com.matej2.budget_lens.domain.dto.request.RegisterRequest;
import com.matej2.budget_lens.domain.dto.response.AuthenticationResponse;
import com.matej2.budget_lens.domain.entity.Role;
import com.matej2.budget_lens.domain.entity.Token;
import com.matej2.budget_lens.domain.entity.TokenType;
import com.matej2.budget_lens.domain.entity.User;
import com.matej2.budget_lens.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticationService {
    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager userAuthenticationManager;
    private final AuthenticationManager clientAuthenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationService(
            UserDetailService userDetailService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            @Qualifier("userAuthenticationManager") AuthenticationManager userAuthenticationManager,
            @Qualifier("clientAuthenticationManager") AuthenticationManager clientAuthenticationManager,
            TokenRepository tokenRepository
    ) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userAuthenticationManager = userAuthenticationManager;
        this.clientAuthenticationManager = clientAuthenticationManager;
        this.tokenRepository = tokenRepository;
    }

    public void register(RegisterRequest request) {
        log.info("Register request for user {} received", request.email());

        clientAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password())
        );

        User user = User.builder()
                .firstname(request.firstName())
                .lastname(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        if (userDetailService.findByEmail(request.email()) != null) {
            throw new RuntimeException("User already exists");
        }
        userDetailService.addUser(user);
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.debug("Authenticating request for user {}", request.email());

        userAuthenticationManager.authenticate(
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
