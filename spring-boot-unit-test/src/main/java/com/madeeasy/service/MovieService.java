package com.madeeasy.service;

import com.madeeasy.entity.MovieEntity;
import com.madeeasy.error.MovieNotFoundException;
import com.madeeasy.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public MovieEntity save(MovieEntity movieEntity) {
        return movieRepository.save(movieEntity);
    }

    public MovieEntity findById(Long id) {
        Optional<MovieEntity> findById = movieRepository.findById(id);
        if (findById.isEmpty()) {
            throw new MovieNotFoundException("error!! movie not found");
        }
        return findById.get();
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public List<MovieEntity> getAllMovies() {
        return movieRepository.findAll();
    }

    public MovieEntity updateMovie(Long id, MovieEntity updatedMovie) {
        MovieEntity existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));

        existingMovie.setName(updatedMovie.getName());
        existingMovie.setReleaseDate(updatedMovie.getReleaseDate());

        return movieRepository.save(existingMovie);
    }

    public MovieEntity partialUpdateMovie(Long id, Map<String, Object> updates) {
        MovieEntity existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Perform partial updates based on the provided fields
            switch (key) {
                case "id" -> existingMovie.setId((Long) value);
                case "name" -> existingMovie.setName((String) value);
                case "releaseDate" -> {
                    if (value instanceof String) {
                        try {
                            existingMovie.setReleaseDate(LocalDate.parse((String) value));
                        } catch (DateTimeParseException e) {
                            // Handle the case where the provided date string is invalid
                            // You can throw an exception, log an error, or handle it in any other way that makes sense for your application
                        }
                    }

                    // Handle other fields as needed
                 /*   switch (key) {
                        case "id":
                            existingMovie.setId((Long) value);
                            break;
                        case "name":
                            existingMovie.setName((String) value);
                            break;
                        case "releaseDate":
                            existingMovie.setReleaseDate((LocalDate) value);
                            break;
                        // Handle other fields as needed*/
                }
            }
        }

        return movieRepository.save(existingMovie);
    }
}


