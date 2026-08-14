package com.animalbarf.auth.domain;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Set<Role> roles;
}
