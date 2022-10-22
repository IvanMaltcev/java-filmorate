package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.services.ReleaseDateValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
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

}
