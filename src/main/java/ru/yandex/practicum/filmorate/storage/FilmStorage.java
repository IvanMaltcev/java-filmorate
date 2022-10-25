package ru.yandex.practicum.filmorate.storage;

import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    List<Film> getAllFilms();

    Film addFilm(Film film, BindingResult error);

    Film updateFilm(Film film);

    Map<Integer, Film> getFilms();
}
