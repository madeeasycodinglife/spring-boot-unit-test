package com.madeeasy.repository;

import com.madeeasy.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity,Long> {

    Optional<MovieEntity> findById(Long id);

    @Query(
            value = "select * from tbl_user as u where u.name = :name",
            nativeQuery = true
    )
    MovieEntity findByNameNativeQuery(@Param("name") String name);
    MovieEntity findByReleaseDate(LocalDate releaseDate);

}
