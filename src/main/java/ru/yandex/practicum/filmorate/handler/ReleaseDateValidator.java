package ru.yandex.practicum.filmorate.handler;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateValid, LocalDate> {


    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date.isBefore(LocalDate.of(1895, 12, 28))) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Дата релиза должна быть не раньше 28 декабря 1895 года.")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
