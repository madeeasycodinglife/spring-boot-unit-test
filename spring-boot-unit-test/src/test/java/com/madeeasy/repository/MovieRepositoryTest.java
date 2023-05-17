package com.madeeasy.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.madeeasy.entity.MovieEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Optional;

/**
 * =================== Database Cleanup After the Test ===================
 *
 * The @DataJpaTest meta-annotation contains the @Transactional annotation. This ensures our test execution is
 * wrapped with a transaction that gets rolled back after the test. The rollback happens for both successful test
 * cases as well as failures.
 *
 * Hence, we don't have to clean up our tests, and every test starts with empty tables (except we initialize data with
 * our migration scripts)
 */

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindById() {
        // Create a movie entity and persist it to the database
        MovieEntity movie = new MovieEntity();
        movie.setName("Test Movie");
        movie.setReleaseDate(LocalDate.now());
        /**
         * entityManager.persist(movie) is used to persist the MovieEntity object named "Test Movie" into the database.
         * This ensures that the object will be saved and available for retrieval or further manipulation.
         */
        entityManager.persist(movie);
        /**
         *     The flush() method ensures that the changes made to managed entities are immediately written to the database,
         *     enforcing consistency and integrity constraints defined at the database level.
         *     Without calling flush(), if there are any constraints, such as unique key constraints or foreign key constraints,
         *     violations may not be detected until the transaction commits, potentially leading to data integrity issues.
         */
        entityManager.flush();

        // Retrieve the movie by ID using the repository
        Optional<MovieEntity> foundMovie = movieRepository.findById(movie.getId());

        assertTrue(foundMovie.isPresent());
        assertEquals(movie.getName(), foundMovie.get().getName());
        assertEquals(movie.getReleaseDate(), foundMovie.get().getReleaseDate());
    }

    @Test
    void testFindByNameNativeQuery() {
        // Create a movie entity and persist it to the database
        MovieEntity movie = new MovieEntity();
        movie.setName("Test Movie");
        movie.setReleaseDate(LocalDate.now());
        entityManager.persist(movie);
        entityManager.flush();

        // Retrieve the movie by name using the native query method in the repository
        MovieEntity foundMovie = movieRepository.findByNameNativeQuery(movie.getName());

        assertNotNull(foundMovie);
        assertEquals(movie.getName(), foundMovie.getName());
        assertEquals(movie.getReleaseDate(), foundMovie.getReleaseDate());
    }

    @Test
    void testFindByReleaseDate() {
        // Create movie entities with different release dates and persist them to the database
        MovieEntity movie1 = new MovieEntity();
        movie1.setName("Movie 1");
        movie1.setReleaseDate(LocalDate.now().minusDays(1));
        entityManager.persist(movie1);

        MovieEntity movie2 = new MovieEntity();
        movie2.setName("Movie 2");
        movie2.setReleaseDate(LocalDate.now());
        entityManager.persist(movie2);
        //-----------------------------------------------------
        entityManager.flush();
        //-----------------------------------------------------
        // Retrieve the movies by release date using the repository
        MovieEntity foundMovie1 = movieRepository.findByReleaseDate(movie1.getReleaseDate());
        System.out.println("foundMovie1 = " + foundMovie1);
        MovieEntity foundMovie2 = movieRepository.findByReleaseDate(movie2.getReleaseDate());
        System.out.println("foundMovie2 = " + foundMovie2);
        assertNotNull(foundMovie1);
        assertNotNull(foundMovie2);
        assertEquals(movie1.getName(), foundMovie1.getName());
        assertEquals(movie1.getReleaseDate(), foundMovie1.getReleaseDate());
        assertEquals(movie2.getName(), foundMovie2.getName());
        assertEquals(movie2.getReleaseDate(), foundMovie2.getReleaseDate());
    }
}
