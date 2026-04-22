package com.learning.backend.api.auth;

import com.learning.backend.api.security.JwtService;
import com.learning.backend.common.api.LoginRequest;
import com.learning.backend.common.api.RegisterRequest;
import com.learning.backend.common.api.TokenResponse;
import com.learning.backend.domain.user.UserAccount;
import com.learning.backend.domain.user.UserAccountRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final WelcomeEmailService welcomeEmailService;

    public AuthService(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            WelcomeEmailService welcomeEmailService
    ) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.welcomeEmailService = welcomeEmailService;
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (userAccountRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already taken");
        }
        Set<String> roles = new HashSet<>();
        roles.add("USER");
        UserAccount account = new UserAccount(
                request.username(),
                passwordEncoder.encode(request.password()),
                roles
        );
        userAccountRepository.save(account);
        welcomeEmailService.sendWelcomeAsync(account.getUsername());
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(a -> a.startsWith("ROLE_") ? a.substring("ROLE_".length()) : a)
                .toList();
        String token = jwtService.createToken(authentication.getName(), roles);
        return new TokenResponse(token, "Bearer", jwtService.getExpirationSeconds());
    }
}
