package ru.yandex.practicum.filmorate.dao.interf;

import ru.yandex.practicum.filmorate.model.Film;

public interface LikesDbStorage {
    void setLike(Long userId, Film film);
    void deleteLike(Long userId, Film film);

}
