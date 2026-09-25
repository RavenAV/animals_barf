package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.domain.User;
import org.example.userservice.dto.CreateUserDto;
import org.example.userservice.dto.UpdateUserDto;
import org.example.userservice.dto.UserAuthDto;
import org.example.userservice.dto.UserDto;
import org.example.userservice.exceptions.EmailAlreadyExistsException;
import org.example.userservice.exceptions.UserNotFoundException;
import org.example.userservice.repository.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Создание пользователя.
     */
    public UserDto create(CreateUserDto request) {
        String email = request
                .email()
                .trim()
                .toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("User with email already exists");
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .middleName(request.middleName())
                .email(email)
                // Пароль превращается в BCrypt hash
                .passwordHash(
                        passwordEncoder.encode(
                                request.password()
                        )
                )
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        return toUserDto(savedUser);
    }

    /**
     * Получение пользователя по ID.
     */
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {

        User user = findUser(id);

        return toUserDto(user);
    }

    /**
     * Получение списка пользователей.
     */
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {

        return userRepository
                .findAll()
                .stream()
                .map(this::toUserDto)
                .toList();
    }

    /**
     * Обновление пользователя.
     */
    public UserDto update(Long id, UpdateUserDto updateUserDto) {
        User user = findUser(id);

        if (updateUserDto.firstName() != null) {
            user.setFirstName(updateUserDto.firstName());
        }

        if (updateUserDto.lastName() != null) {
            user.setLastName(updateUserDto.lastName());
        }

        if (updateUserDto.middleName() != null) {
            user.setMiddleName(updateUserDto.middleName());
        }

        if (updateUserDto.email() != null) {

            String email = updateUserDto
                    .email()
                    .trim()
                    .toLowerCase();

            if (!email.equals(user.getEmail()) && userRepository.existsByEmail(email)) {
                throw new EmailAlreadyExistsException("Email already exists");
            }

            user.setEmail(email);
        }

        if (updateUserDto.enabled() != null) {
            user.setEnabled(updateUserDto.enabled());
        }

        return toUserDto(userRepository.save(user));
    }

    /**
     * Удаление пользователя.
     */
    public void delete(Long id) {
        User user = findUser(id);
        userRepository.delete(user);
    }

    /**
     * Метод специально для Auth Service.
     * <p>
     * Здесь возвращается passwordHash.
     */
    @Transactional(readOnly = true)
    public UserAuthDto getForAuthentication(String email) {
        User user = userRepository
                .findByEmailAndEnabledTrue(email.trim().toLowerCase())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return new UserAuthDto(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getEnabled()
        );
    }

    private User findUser(Long id) {

        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    private UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getEmail(),
                user.getEnabled()
        );
    }
}
