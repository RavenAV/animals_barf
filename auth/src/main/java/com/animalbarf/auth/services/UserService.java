package com.animalbarf.auth.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", path = "/users")
public interface UserService {
    // todo обращение к мс пользователей
//    @PostMapping
//    UserDto create(@RequestBody CreateUserDto request);
}
