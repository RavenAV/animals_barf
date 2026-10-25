package org.example.userservice.service;

import com.animalbarf.apicontracts.role.CreateRoleDto;
import com.animalbarf.apicontracts.role.RoleDto;
import com.animalbarf.apicontracts.role.UpdateRoleDto;
import com.animalbarf.apicontracts.user.UserRolesDto;
import lombok.RequiredArgsConstructor;
import org.example.userservice.domain.Role;
import org.example.userservice.domain.User;
import org.example.userservice.repository.IRoleRepository;
import org.example.userservice.repository.IUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {
    private final IRoleRepository roleRepository;
    private final IUserRepository userRepository;

    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RoleDto getRoleById(Long roleId) {
        return toResponse(findRole(roleId));
    }

    @Transactional
    public RoleDto createRole(CreateRoleDto request) {
        String name = normalizeName(request.name());

        if (roleRepository.existsByNameIgnoreCase(name)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Роль с таким названием уже существует"
            );
        }

        Role role = Role.builder()
                .name(name)
                .description(request.description())
                .build();

        return toResponse(roleRepository.save(role));
    }

    @Transactional
    public RoleDto updateRole(Long roleId, UpdateRoleDto request) {
        Role role = findRole(roleId);
        String name = normalizeName(request.name());

        roleRepository.findByNameIgnoreCase(name)
                .filter(existing -> !existing.getId().equals(roleId))
                .ifPresent(existing -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Роль с таким названием уже существует"
                    );
                });

        role.setName(name);
        role.setDescription(request.description());

        return toResponse(roleRepository.save(role));
    }

    @Transactional
    public void deleteRole(Long roleId) {
        Role role = findRole(roleId);

        // Сначала удаляем связь пользователя с ролью.
        List<User> users = userRepository.findAllByRoles_Id(roleId);

        for (User user : users) {
            user.getRoles().removeIf(
                    userRole -> userRole.getId().equals(roleId)
            );
        }

        // Затем удаляем саму роль.
        roleRepository.delete(role);
    }

    @Transactional
    public UserRolesDto getUserRoles(Long userId) {
        User user = findUser(userId);

        List<RoleDto> roles = user.getRoles()
                .stream()
                .map(this::toResponse)
                .toList();

        return new UserRolesDto(userId, roles);
    }

    @Transactional
    public UserRolesDto assignRole(Long userId, Long roleId) {
        User user = findUser(userId);
        Role role = findRole(roleId);

        boolean alreadyAssigned = user.getRoles()
                .stream()
                .anyMatch(existing -> existing.getId().equals(roleId));

        if (!alreadyAssigned) {
            // User.roles — владеющая сторона ManyToMany.
            user.getRoles().add(role);
        }

        return getUserRoles(userId);
    }

    @Transactional
    public UserRolesDto removeRole(Long userId, Long roleId) {
        User user = findUser(userId);
        Role role = findRole(roleId);

        user.getRoles().removeIf(
                existing -> existing.getId().equals(role.getId())
        );

        return getUserRoles(userId);
    }

    private Role findRole(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Роль не найдена: " + roleId
                ));
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Пользователь не найден: " + userId
                ));
    }

    private String normalizeName(String name) {
        return name.trim().toUpperCase(Locale.ROOT);
    }

    private RoleDto toResponse(Role role) {
        return new RoleDto(
                role.getId(),
                role.getName(),
                role.getDescription()
        );
    }
}
