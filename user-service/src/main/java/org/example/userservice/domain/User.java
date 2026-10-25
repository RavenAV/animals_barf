package org.example.userservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(
                        name = "idx_users_email",
                        columnList = "email"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "first_name",
            nullable = false,
            length = 100
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            length = 100
    )
    private String lastName;

    @Column(
            name = "middle_name",
            length = 100
    )
    private String middleName;

    @Column(
            nullable = false,
            unique = true,
            length = 255
    )
    private String email;

    /**
     * Здесь хранится НЕ пароль,
     * а его BCrypt hash.
     */
    @Column(
            name = "password_hash",
            nullable = false
    )
    private String passwordHash;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = {
                    @UniqueConstraint(
                            name = "uk_user_roles",
                            columnNames = {"user_id", "role_id"}
                    )
            }
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}