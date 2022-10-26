package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.handler.ReleaseDateValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private int id;
    @NotBlank (message = "Название фильма не может быть пустым.")
    private String name;
    @Size (max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;
    @ReleaseDateValid (message = "Дата релиза должна быть не раньше 28 декабря 1895 года.")
    private LocalDate releaseDate;
    @Positive (message = "Продолжительность фильма должна быть положительной.")
    private int duration;
    private Set<Integer> likes = new HashSet<>();

    public Film(int id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
