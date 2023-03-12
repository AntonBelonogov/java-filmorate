package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Genre {
    private int id;
    @EqualsAndHashCode.Exclude
    private String name;
    public Genre() {
    }
}
