package com.madeeasy.service;

import static org.junit.jupiter.api.Assertions.*;

import com.madeeasy.entity.MovieEntity;
import com.madeeasy.error.MovieNotFoundException;
import com.madeeasy.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    public MovieServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setId(1L);
        movieEntity.setName("Test Movie");
        movieEntity.setReleaseDate(LocalDate.of(2023, Month.MAY, 17));

        when(movieRepository.save(movieEntity)).thenReturn(movieEntity);

        MovieEntity savedMovie = movieService.save(movieEntity);

        assertNotNull(savedMovie);
        assertEquals("Test Movie", savedMovie.getName());
        assertEquals("2023-05-17", String.valueOf(savedMovie.getReleaseDate()));

        verify(movieRepository, times(1)).save(movieEntity);
    }

    @Test
    void testFindById() {
        Long id = 1L;
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setId(id);
        movieEntity.setName("Test Movie");
        movieEntity.setReleaseDate(LocalDate.of(2023, Month.MAY, 17));

        when(movieRepository.findById(id)).thenReturn(Optional.of(movieEntity));

        MovieEntity foundMovie = movieService.findById(id);

        assertNotNull(foundMovie);
        assertEquals(id, foundMovie.getId());
        assertEquals("Test Movie", foundMovie.getName());
        assertEquals("2023-05-17", String.valueOf(foundMovie.getReleaseDate()));

        verify(movieRepository, times(1)).findById(id);
    }

    @Test
    void testFindByIdNotFound() {
        Long id = 1L;

        when(movieRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundException.class, () -> movieService.findById(id));

        verify(movieRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteMovie() {
        Long id = 1L;

        movieService.deleteMovie(id);

        verify(movieRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetAllMovies() {
        List<MovieEntity> movies = new ArrayList<>();
        MovieEntity movie1 = new MovieEntity();
        movie1.setId(1L);
        movie1.setName("Movie 1");
        movie1.setReleaseDate(LocalDate.of(2023, Month.MAY, 17));
        MovieEntity movie2 = new MovieEntity();
        movie2.setId(2L);
        movie2.setName("Movie 2");
        movie2.setReleaseDate(LocalDate.of(2023, Month.MAY, 17));
        movies.add(movie1);
        movies.add(movie2);

        when(movieRepository.findAll()).thenReturn(movies);

        List<MovieEntity> allMovies = movieService.getAllMovies();

        assertNotNull(allMovies);
        assertEquals(2, allMovies.size());

        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void testUpdateMovie() {
        Long id = 1L;
        MovieEntity existingMovie = new MovieEntity();
        existingMovie.setId(id);
        existingMovie.setName("Existing Movie");
        existingMovie.setReleaseDate(LocalDate.of(2023, Month.MAY, 17));

        MovieEntity updatedMovie = new MovieEntity();
        updatedMovie.setId(1L);
        updatedMovie.setName("Updated Movie");
        updatedMovie.setReleaseDate(LocalDate.of(2023, Month.MAY, 17));

        when(movieRepository.findById(id)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(existingMovie);

        MovieEntity result = movieService.updateMovie(id, updatedMovie);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Updated Movie", result.getName());
        assertEquals(LocalDate.parse("2023-05-17"), result.getReleaseDate());

        verify(movieRepository, times(1)).findById(id);
        verify(movieRepository, times(1)).save(existingMovie);
    }
    @Test
    void testPartialUpdateMovie() {
        Long id = 1L;
        MovieEntity existingMovie = new MovieEntity();
        existingMovie.setId(id);
        existingMovie.setName("Existing Movie");
        existingMovie.setReleaseDate(LocalDate.parse("2023-05-17"));

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "Updated Movie");
        updates.put("releaseDate", "2023-05-18");

        when(movieRepository.findById(id)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(existingMovie)).thenReturn(existingMovie);

        MovieEntity result = movieService.partialUpdateMovie(id, updates);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Updated Movie", result.getName());
        assertEquals(LocalDate.parse("2023-05-18"), result.getReleaseDate());

        verify(movieRepository, times(1)).findById(id);
        verify(movieRepository, times(1)).save(existingMovie);
    }
}
