package com.boiler.user;

public record UserDto(
        String id,
        String name,
        String email,
        String password
){}
