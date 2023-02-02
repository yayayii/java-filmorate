package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.EntityDoesntExistException;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Set;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable int id) {
        Mpa mpa = mpaService.getMpa(id);
        if (mpa == null) {
            RuntimeException exception = new EntityDoesntExistException("Mpa with id=" + id + " doesn't exists.");
            log.warn(exception.getMessage());
            throw exception;
        }
        return mpa;
    }
    @GetMapping
    public Set<Mpa> getMpas() {
        return mpaService.getMpas();
    }
}
