package org.example.userservice.domain;

import jakarta.persistence.*;
import lombok.*;

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
}