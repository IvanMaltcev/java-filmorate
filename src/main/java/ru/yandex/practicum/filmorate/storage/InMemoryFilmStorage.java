package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int filmId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    public int incrementAndGetId() {
        filmId++;
        return filmId;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(incrementAndGetId());
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        verificationFilmId(film.getId());
        films.put(film.getId(), film);
        log.info("Информация о фильме с id {} обновлена", film.getId());
        return film;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

    @Override
    public void verificationFilmId(int filmId) {
        if (!films.containsKey(filmId)) {
            log.info("Фильм с id {} не найден", filmId);
            throw new NotFoundException("Не верный id фильма.");
        }
    }
}
