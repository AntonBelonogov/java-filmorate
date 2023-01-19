package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {

    private int id;

    @EqualsAndHashCode.Exclude
    @NotBlank(message = "Название не может быть пустым.")
    private String name;
    @EqualsAndHashCode.Exclude
    @Size(max = 200, message = "Максимальная длина описания - 200 символов.")
    private String description;
    @EqualsAndHashCode.Exclude
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    @ReleaseDate
    private LocalDate releaseDate;
    @EqualsAndHashCode.Exclude
    @Positive(message = "Значение не может быть отрицательной.")
    private int duration;

}
