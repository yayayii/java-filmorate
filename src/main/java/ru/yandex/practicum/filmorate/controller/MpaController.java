package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.validation.MpaValidator;

import java.util.Collection;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaValidator mpaValidator;
    private final MpaService mpaService;

    @GetMapping("/{id}")
    public Mpa getMpa(@PathVariable int id) {
        mpaValidator.validateMpaIds(id);
        return mpaService.getMpa(id);
    }
    @GetMapping
    public Collection<Mpa> getMpas() {
        return mpaService.getMpas().values();
    }
}
