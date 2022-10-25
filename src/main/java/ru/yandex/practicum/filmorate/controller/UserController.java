package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user, BindingResult error) {
        return userService.createUser(user, error);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> showFriends(@PathVariable int id) {
        return userService.showFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> showCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.showCommonFriends(id, otherId);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addNewFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addNewFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriend(id, friendId);
    }
}
