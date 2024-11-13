package com.github.elvennurse.elven_finance_manager.security;

import com.github.elvennurse.elven_finance_manager.model.User;
import com.github.elvennurse.elven_finance_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Завантажує користувача за username або email.
     *
     * @param usernameOrEmail Username або Email користувача.
     * @return Об'єкт CustomUserDetails.
     * @throws UsernameNotFoundException якщо користувач не знайдений.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail)
                );

        return CustomUserDetails.build(user);
    }

    /**
     * Завантажує користувача за ID.
     *
     * @param id Ідентифікатор користувача.
     * @return Об'єкт CustomUserDetails.
     * @throws UsernameNotFoundException якщо користувач не знайдений.
     */
    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id: " + id)
        );

        return CustomUserDetails.build(user);
    }
}
