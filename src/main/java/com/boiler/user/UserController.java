package com.boiler.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public UserDto create(@RequestBody UserDto request){
        return userService.create(request);
    }

    @GetMapping("/profile/{id}")
    public UserDto viewProfile(@PathVariable("id") String id){
        return userService.getUser(id);
    }

    @PutMapping("/profile/{id}/edit")
    public UserDto edit(@PathVariable("id") String id, @RequestBody UserDto request){
        return userService.update(id, request);
    }

    @DeleteMapping("/profile/{id}/delete")
    public void delete(@PathVariable("id") String id){
         userService.delete(id);
    }
}
