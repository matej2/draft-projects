package com.example.expense_tracker.service;


import com.example.expense_tracker.domain.dto.AuthenticationRequest;
import com.example.expense_tracker.domain.dto.AuthenticationResponse;
import com.example.expense_tracker.domain.dto.RegisterRequest;
import com.example.expense_tracker.domain.entity.Role;
import com.example.expense_tracker.domain.entity.Token;
import com.example.expense_tracker.domain.entity.TokenType;
import com.example.expense_tracker.domain.entity.User;
import com.example.expense_tracker.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .firstname(request.firstName())
                .lastname(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();

        if (userDetailService.findByEmail(request.email()) != null) {
            return new AuthenticationResponse(null);
        }
        userDetailService.addUser(user);

        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(
                jwtToken
        );
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
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
