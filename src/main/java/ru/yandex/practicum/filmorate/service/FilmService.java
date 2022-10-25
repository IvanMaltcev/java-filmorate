package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film, BindingResult error) {
        return filmStorage.addFilm(film, error);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(int filmId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new NotFoundException("Не верный id фильма.");
        }
        return filmStorage.getFilms().get(filmId);
    }

    public List<Film> findPopularFilms(Integer count) {
        return filmStorage.getAllFilms().stream().sorted((film1, film2) -> {
            int comp = Integer.compare(film1.getLikes().size(), film2.getLikes().size());
            return comp * -1;
        }).limit(count).collect(Collectors.toList());
    }

    public void addLike(int filmId, int userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new NotFoundException("Не верный id фильма.");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Не верный id пользователя.");
        }
        Film film = filmStorage.getFilms().get(filmId);
        film.getLikes().add(userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new NotFoundException("Не верный id фильма.");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Не верный id пользователя.");
        }
        Film film = filmStorage.getFilms().get(filmId);
        film.getLikes().remove(userId);
    }
}
