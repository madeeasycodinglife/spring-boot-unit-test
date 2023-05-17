package com.madeeasy.controller;

import com.madeeasy.entity.MovieEntity;
import com.madeeasy.service.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("/create")
    public ResponseEntity<?> createMovie(@RequestBody MovieEntity movieEntity,
                                         HttpServletRequest request
    ) {
        MovieEntity savedMovie = movieService.save(movieEntity);
        HttpHeaders headers = new HttpHeaders();
        String replaceUri = ServletUriComponentsBuilder
                .fromRequestUri(request)
                .toUriString()
                .replace("/create", "/" + savedMovie.getId());

        URI uri = URI.create(replaceUri);
        headers.setLocation(uri);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .headers(headers)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        MovieEntity findById = movieService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MovieEntity>> getAllMovies() {
        List<MovieEntity> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @PutMapping("/{id}")
    public MovieEntity updateMovie(@PathVariable Long id, @RequestBody MovieEntity updatedMovie) {
        return movieService.updateMovie(id, updatedMovie);
    }

    @PatchMapping("/{id}")
    public MovieEntity partialUpdateMovie(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return movieService.partialUpdateMovie(id, updates);
    }
}
