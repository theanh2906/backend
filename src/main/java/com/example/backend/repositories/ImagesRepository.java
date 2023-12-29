package com.example.backend.repositories;

import com.example.backend.models.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends JpaRepository<Images, String> {
    @Query(value = "DELETE FROM images", nativeQuery = true)
    Boolean deleteAllImages();
}
