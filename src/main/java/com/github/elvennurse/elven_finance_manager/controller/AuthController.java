package com.github.elvennurse.elven_finance_manager.controller;

import com.github.elvennurse.elven_finance_manager.dto.*;
import com.github.elvennurse.elven_finance_manager.model.User;
import com.github.elvennurse.elven_finance_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    /**
     * Endpoint для реєстрації нового користувача.
     * @param signUpRequest Об'єкт запиту для реєстрації.
     * @return ApiResponse з повідомленням про успіх або помилку.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Username is already taken!"));
        }

        if(userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Email Address already in use!"));
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());

        userService.registerUser(user);

        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }

    /**
     * Endpoint для аутентифікації користувача (логіну).
     * @param loginRequest Об'єкт запиту для логіну.
     * @return ApiResponse з повідомленням про успіх або помилку.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsernameOrEmail(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok(new ApiResponse(true, "Login successful"));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(false, "Invalid username or password"));
        }
    }

    /**
     * Endpoint для виходу користувача (лог-ауту).
     * @return ApiResponse з повідомленням про успіх.
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ApiResponse(true, "Logout successful"));
    }
}
