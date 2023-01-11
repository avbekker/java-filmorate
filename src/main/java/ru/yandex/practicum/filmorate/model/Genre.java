package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    private int id;
    private String name;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Genre genre = (Genre) obj;
        return id == genre.getId();
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        return prime * Objects.hash(id);
    }
}
