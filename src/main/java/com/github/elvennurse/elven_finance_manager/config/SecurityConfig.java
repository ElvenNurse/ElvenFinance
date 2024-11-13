package com.github.elvennurse.elven_finance_manager.config;

import com.github.elvennurse.elven_finance_manager.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Налаштування фільтрів безпеки за допомогою SecurityFilterChain.
     * Це замінює WebSecurityConfigurerAdapter у Spring Security 6.
     *
     * @param http Об'єкт HttpSecurity для налаштування безпеки HTTP.
     * @return SecurityFilterChain з налаштованими правилами безпеки.
     * @throws Exception Якщо виникає помилка під час налаштування.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Вимкнення CSRF для простоти (розгляньте можливість увімкнення у продакшн)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Налаштування авторизації запитів
            .authorizeHttpRequests(auth -> auth
                // Дозволити доступ без автентифікації до цих ендпоінтів
                .requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Всі інші запити потребують автентифікації
                .anyRequest().authenticated()
            )
            
            // Налаштування формового логіну
            .formLogin(form -> form
                // URL для обробки логіну
                .loginProcessingUrl("/api/auth/login")
                
                // Обробник успішного логіну
                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write("{\"success\": true, \"message\": \"Login successful\"}");
                })
                
                // Обробник невдалого логіну
                .failureHandler((request, response, exception) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write("{\"success\": false, \"message\": \"Invalid username or password\"}");
                })
                
                .permitAll()
            )
            
            // Налаштування виходу з системи
            .logout(logout -> logout
                // URL для виходу
                .logoutUrl("/api/auth/logout")
                
                // Обробник успішного виходу
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpStatus.OK.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write("{\"success\": true, \"message\": \"Logout successful\"}");
                })
                
                .permitAll()
            )
            
            // Вказуємо, що використовуємо наш CustomUserDetailsService
            .userDetailsService(customUserDetailsService);

        return http.build();
    }

    /**
     * Бін для PasswordEncoder. Використовується для хешування паролів.
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Бін для AuthenticationManager, необхідний для автентифікації користувачів.
     *
     * @param authenticationConfiguration Конфігурація автентифікації.
     * @return AuthenticationManager
     * @throws Exception Якщо виникає помилка під час створення AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
