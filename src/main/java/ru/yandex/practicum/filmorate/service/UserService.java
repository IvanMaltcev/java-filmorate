package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User createUser(User user, BindingResult error) {
        return userStorage.createUser(user, error);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUser(int userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Не верный id пользователя.");
        }
        return userStorage.getUsers().get(userId);
    }

    public List<User> showFriends(int userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Не верный id пользователя.");
        }
        List<User> friends = new ArrayList<>();
        User user = userStorage.getUsers().get(userId);
        for (User friend : userStorage.getUsers().values()) {
            if (user.getFriends().contains(friend.getId())) {
                friends.add(friend);
            }
        }
        return friends;
    }

    public List<User> showCommonFriends(int userId, int otherUserId) {
        if (!userStorage.getUsers().containsKey(userId) && !userStorage.getUsers().containsKey(otherUserId)) {
            throw new NotFoundException("Не верный id пользователя.");
        }
        List<User> commonFriends = new ArrayList<>();
        User user = userStorage.getUsers().get(userId);
        User otherUser = userStorage.getUsers().get(otherUserId);
        for (Integer id : user.getFriends()) {
            if (otherUser.getFriends().contains(id)) {
                commonFriends.add(userStorage.getUsers().get(id));
            }
        }
        return commonFriends;
    }

    public void addNewFriend(int userId, int friendId) {
        if (!userStorage.getUsers().containsKey(userId) || !userStorage.getUsers().containsKey(friendId)) {
            throw new NotFoundException("Не верный id пользователя.");
        }
        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        if (!userStorage.getUsers().containsKey(userId) && !userStorage.getUsers().containsKey(friendId)) {
            throw new NotFoundException("Не верный id пользователя.");
        }
        User user = userStorage.getUsers().get(userId);
        User friend = userStorage.getUsers().get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

}
