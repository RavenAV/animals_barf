package com.animalbarf.auth.services;

import com.animalbarf.apicontracts.user.CreateUserDto;
import com.animalbarf.apicontracts.user.UserAuthDto;
import com.animalbarf.apicontracts.user.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", path = "/users")
public interface UserService {
    @PostMapping
    UserDto create(@RequestBody CreateUserDto request);

    @GetMapping("/internal/auth")
    UserAuthDto getForAuthentication(@RequestParam String login);
}
