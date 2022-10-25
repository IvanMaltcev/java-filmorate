package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmStorage filmStorage;
    private Film film;
    private BindingResult error;
    private Validator validator;

    @BeforeEach
    public void beforeEach() {

        filmStorage = new InMemoryFilmStorage();
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();

        film = new Film (
                0,
                "Test film",
                "Test film description",
                LocalDate.of(2012,2,12),
                220
        );

        error = new DataBinder(film).getBindingResult();
    }

    @Test
    void addNewFilm() {

        final Film newFilm = filmStorage.addFilm(film, error);

        assertNotNull(newFilm, "Фильм не найден.");
        assertEquals(film, newFilm, "Фильмы не совпадают.");

        final List<Film> films = filmStorage.getAllFilms();

        assertNotNull(films, "Фильмы не возвращаются.");
        assertEquals(1, films.size(), "Неверное количество фильмов.");
        assertEquals(film, films.get(0), "Фильмы не совпадают.");
    }

    @Test
    void updateNewFilm() {

        final Film newFilm = filmStorage.addFilm(film, error);
        newFilm.setDescription("New description of test film");
        final Film updateFilm = filmStorage.updateFilm(newFilm);

        final List<Film> films = filmStorage.getAllFilms();

        assertNotNull(films, "Фильмы не возвращаются.");
        assertEquals(1, films.size(), "Неверное количество фильмов.");
        assertEquals(updateFilm, films.get(0), "Фильмы не совпадают.");
    }

    @Test
    public void shouldReturnExceptionWhenNameIsBlank() {
        final Film newFilm = filmStorage.addFilm(film, error);
        newFilm.setName("");
        Set<ConstraintViolation<Film>> validates = validator.validate(newFilm);
        assertTrue(validates.size() > 0);
        String message = validates.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()).get(0);

        assertEquals("Название фильма не может быть пустым.", message);
    }

    @Test
    public void shouldReturnExceptionWhenDescriptionLengthIsExceeded() {
        final Film newFilm = filmStorage.addFilm(film, error);
        newFilm.setDescription("Описание фильма превышает максимальную заданную длину, что приводит к ошибке." +
                "Чтобы избежать этого необходимо уменьшить количество символов в описании, " +
                "сделать описание более сжатым, но при этом не потерять суть.");
        Set<ConstraintViolation<Film>> validates = validator.validate(newFilm);
        assertTrue(validates.size() > 0);
        String message = validates.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()).get(0);

        assertEquals("Максимальная длина описания — 200 символов.", message);
    }

    @Test
    public void shouldReturnExceptionWhenIncorrectReleaseDate() {
        final Film newFilm = filmStorage.addFilm(film, error);
        newFilm.setReleaseDate(LocalDate.of(1895,12,25));
        Set<ConstraintViolation<Film>> validates = validator.validate(newFilm);
        assertTrue(validates.size() > 0);
        String message = validates.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()).get(0);
        assertEquals("Дата релиза должна быть не раньше 28 декабря 1895 года.", message);
    }

    @Test
    public void shouldReturnExceptionWhenDurationIsNegative() {
        final Film newFilm = filmStorage.addFilm(film, error);
        newFilm.setDuration(-100);
        Set<ConstraintViolation<Film>> validates = validator.validate(newFilm);
        assertTrue(validates.size() > 0);
        String message = validates.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()).get(0);

        assertEquals("Продолжительность фильма должна быть положительной.", message);
    }

    @Test
    public void shouldReturnExceptionWhenIdNotFound() {
        final Film newFilm = filmStorage.addFilm(film, error);
        newFilm.setId(2);
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> {
                    final Film updateFilm = filmStorage.updateFilm(newFilm);
                });
        assertEquals("Не верный id фильма.", exception.getMessage());
    }

}