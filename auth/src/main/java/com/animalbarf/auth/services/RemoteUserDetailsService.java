package com.animalbarf.auth.services;

import com.animalbarf.apicontracts.user.UserAuthDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class RemoteUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(@NonNull String login) throws UsernameNotFoundException {
        UserAuthDto authDto = userService.getForAuthentication(login);

        if (authDto == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Возвращаем стандартного пользователя Spring Security
        return User.withUsername(authDto.email())
                .password(authDto.passwordHash())
                .authorities(Collections.emptyList()) // Роли, если есть!
                .build();
    }
}
