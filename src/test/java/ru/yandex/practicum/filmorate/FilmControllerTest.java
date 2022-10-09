package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController filmController;
    Film film;

    @BeforeEach
    public void beforeEach() {

        filmController = new FilmController();

        film = new Film (
                0,
                "Test film",
                "Test film description",
                LocalDate.of(2012,2,12),
                220
        );
    }

    @Test
    void addNewFilm() {

        final Film newFilm = filmController.addFilm(film);

        assertNotNull(newFilm, "Фильм не найден.");
        assertEquals(film, newFilm, "Фильмы не совпадают.");

        final List<Film> films = filmController.getAllFilms();

        assertNotNull(films, "Фильмы не возвращаются.");
        assertEquals(1, films.size(), "Неверное количество фильмов.");
        assertEquals(film, films.get(0), "Фильмы не совпадают.");
    }

    @Test
    void updateNewFilm() {

        final Film newFilm = filmController.addFilm(film);
        newFilm.setDescription("New description of test film");
        final Film updateFilm = filmController.updateFilm(newFilm);

        final List<Film> films = filmController.getAllFilms();

        assertNotNull(films, "Фильмы не возвращаются.");
        assertEquals(1, films.size(), "Неверное количество фильмов.");
        assertEquals(updateFilm, films.get(0), "Фильмы не совпадают.");
    }

    @Test
    public void shouldReturnExceptionWhenNameIsBlank() {
        final Film newFilm = filmController.addFilm(film);
        newFilm.setName("");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        final Film errorFilm = filmController.addFilm(newFilm);
                    }
                });
        assertEquals("Название фильма не может быть пустым.", exception.getMessage());
    }

    @Test
    public void shouldReturnExceptionWhenDescriptionLengthIsExceeded() {
        final Film newFilm = filmController.addFilm(film);
        newFilm.setDescription("Описание фильма превышает максимальную заданную длину, что приводит к ошибке." +
                "Чтобы избежать этого необходимо уменьшить количество символов в описании, " +
                "сделать описание более сжатым, но при этом не потерять суть.");
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        final Film errorFilm = filmController.addFilm(newFilm);
                    }
                });
        assertEquals("Максимальная длина описания — 200 символов.", exception.getMessage());
    }

    @Test
    public void shouldReturnExceptionWhenIncorrectReleaseDate() {
        final Film newFilm = filmController.addFilm(film);
        newFilm.setReleaseDate(LocalDate.of(1895,12,25));
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        final Film errorFilm = filmController.addFilm(newFilm);
                    }
                });
        assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года.", exception.getMessage());
    }

    @Test
    public void shouldReturnExceptionWhenDurationIsNegative() {
        final Film newFilm = filmController.addFilm(film);
        newFilm.setDuration(-100);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        final Film errorFilm = filmController.addFilm(newFilm);
                    }
                });
        assertEquals("Продолжительность фильма должна быть положительной.", exception.getMessage());
    }

    @Test
    public void shouldReturnExceptionWhenIdNoFound() {
        final Film newFilm = filmController.addFilm(film);
        newFilm.setId(2);
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        final Film updateFilm = filmController.updateFilm(newFilm);
                    }
                });
        assertEquals("Не верный id фильма.", exception.getMessage());
    }

}