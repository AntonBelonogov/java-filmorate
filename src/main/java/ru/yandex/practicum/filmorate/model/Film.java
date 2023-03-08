package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
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
    @EqualsAndHashCode.Exclude
    private List<Genre> genres;
    @EqualsAndHashCode.Exclude
    private Mpa mpa;


    @JsonIgnoreProperties("usersLikes")
    private final Set<Integer> usersLikes = new HashSet<>();

    public Integer getLikesCount() {
        return usersLikes.size();
    }
}
