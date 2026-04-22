package com.learning.backend.api.bootstrap;

import com.learning.backend.domain.user.UserAccount;
import com.learning.backend.domain.user.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Seeds a dev admin user (disabled outside {@code dev} profile).
 */
@Component
@Profile("dev")
public class DevUsersInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DevUsersInitializer.class);

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public DevUsersInitializer(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userAccountRepository.existsByUsername("admin")) {
            return;
        }
        UserAccount admin = new UserAccount(
                "admin",
                passwordEncoder.encode("adminadmin"),
                Set.of("USER", "ADMIN")
        );
        userAccountRepository.save(admin);
        log.info("Seeded dev admin user (username=admin). Change password before any real deployment.");
    }
}
