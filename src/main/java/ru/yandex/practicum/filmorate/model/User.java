package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.StringWithoutSpaces;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class User {

    private int id;

    @Email(message = "Почта не соответсвует формату.")
    @NotNull(message = "Электронная почта не может быть пустой.")
    @EqualsAndHashCode.Exclude
    private String email;

    @NotBlank(message = "Поле логина не может быть пустым.")
    @StringWithoutSpaces
    @EqualsAndHashCode.Exclude
    private String login;

    @EqualsAndHashCode.Exclude
    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будуйщем.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    @EqualsAndHashCode.Exclude
    private LocalDate birthday;

    @EqualsAndHashCode.Exclude
    private final Set<Integer> friendsSet = new HashSet<>();

    public void addFriend(Integer id) {
        if (friendsSet.contains(id)) {
            throw new RuntimeException("Друг с id="+ id +"уже в списке");
        }
        friendsSet.add(id);
    }

    public void deleteFriend(Integer id) {
        if (!friendsSet.contains(id)) {
            throw new RuntimeException("Друга с id="+ id +"итак нет в списке");
        }
        friendsSet.remove(id);
    }
}
