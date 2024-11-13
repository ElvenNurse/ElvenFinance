package com.github.elvennurse.elven_finance_manager.config;

import com.github.elvennurse.elven_finance_manager.exception.ResourceNotFoundException;
import com.github.elvennurse.elven_finance_manager.model.Role;
import com.github.elvennurse.elven_finance_manager.model.User;
import com.github.elvennurse.elven_finance_manager.repository.RoleRepository;
import com.github.elvennurse.elven_finance_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        if (userRepository.findByUsernameOrEmail("user", "user@test.com").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setEmail("user@test.com");
            user.setPassword(passwordEncoder.encode("user"));
            Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new ResourceNotFoundException("Role not found."));
            user.setRoles(new HashSet<>(Set.of(userRole)));
            userRepository.save(user);
        }

        if (userRepository.findByUsernameOrEmail("admin", "admin@test.com").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@test.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow(() -> new ResourceNotFoundException("Role not found."));
            admin.setRoles(new HashSet<>(Set.of(adminRole)));
            userRepository.save(admin);
        }
    }
}
