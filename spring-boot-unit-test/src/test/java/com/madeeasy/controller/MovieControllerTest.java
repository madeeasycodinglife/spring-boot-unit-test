package com.madeeasy.controller;

import com.madeeasy.entity.MovieEntity;
import com.madeeasy.service.MovieService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Test
    public void testCreateMovie() throws Exception {
        MovieEntity savedMovie = new MovieEntity();
        savedMovie.setId(1L);
        savedMovie.setName("Test Movie");
        savedMovie.setReleaseDate(LocalDate.of(2023, Month.MAY, 17));

        // Mock the movieService save method
        when(movieService.save(any(MovieEntity.class))).thenReturn(savedMovie);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders.post("/movies/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Test Movie\",\"releaseDate\":\"2023-05-17\"}")
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/movies/1"));
        verify(movieService).save(any(MovieEntity.class));
    }

    @Test
    public void testFindById() throws Exception {
        // Mock the movieService.findById() method
        when(movieService.findById(1L)).thenReturn(new MovieEntity());

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/movies/{id}", 1L))
                .andExpect(status().isOk());

        // Verify that the movieService.findById() method was called with the correct argument
        verify(movieService).findById(1L);
    }

    @Test
    public void testDeleteMovie() throws Exception {
        // Perform the DELETE request
        mockMvc.perform(MockMvcRequestBuilders.delete("/movies/{id}", 1L))
                .andExpect(status().isNoContent());

        // Verify that the movieService.deleteMovie() method was called with the correct argument
        verify(movieService).deleteMovie(1L);
    }

    @Test
    public void testGetAllMovies() throws Exception {
        // Create a sample list of MovieEntity
        List<MovieEntity> movies = new ArrayList<>();
        movies.add(new MovieEntity());
        movies.add(new MovieEntity());

        // Mock the movieService.getAllMovies() method
        when(movieService.getAllMovies()).thenReturn(movies);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/movies"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)));

        // Verify that the movieService.getAllMovies() method was called
        verify(movieService).getAllMovies();
    }

    @Test
    public void testUpdateMovie() throws Exception {
        MovieEntity savedMovie = new MovieEntity();
        savedMovie.setId(1L);
        savedMovie.setName("Test Movie");
        savedMovie.setReleaseDate(LocalDate.of(2023, Month.MAY, 17));

        // Mock the movieService.updateMovie() method
        when(movieService.updateMovie(eq(1L), any(MovieEntity.class))).thenReturn(savedMovie);

        // Perform the PUT request
        mockMvc.perform(MockMvcRequestBuilders.put("/movies/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Test Movie\",\"releaseDate\":\"2023-05-17\"}")
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Test Movie")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate", Matchers.is("2023-05-17")));

        // Verify that the movieService.updateMovie() method was called with the correct arguments
        verify(movieService).updateMovie(eq(1L), any(MovieEntity.class));
    }

    @Test
    public void testPartialUpdateMovie() throws Exception {
        // Mock the id and updates
        Long id = 1L;

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "Updated Movie");
        updates.put("releaseDate", "2023-05-17");

        // Mock the movieEntity
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setId(id);
        movieEntity.setName("Test Movie");
        movieEntity.setReleaseDate(LocalDate.of(2023, Month.MAY, 17));

        // Mock the movieService partialUpdateMovie method
        when(movieService.partialUpdateMovie(eq(id), anyMap())).thenReturn(movieEntity);

        // Perform the PATCH request
        mockMvc.perform(MockMvcRequestBuilders.patch("/movies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Movie\", \"releaseDate\": \"2023-05-17\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Movie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value("2023-05-17"));

        // Verify that the movieService partialUpdateMovie method was called with the id and updates
        verify(movieService, times(1)).partialUpdateMovie(eq(id), eq(updates));

    }

}
