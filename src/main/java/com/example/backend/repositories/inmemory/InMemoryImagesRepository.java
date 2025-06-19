package com.example.backend.repositories.inmemory;

import com.example.backend.models.Images;
import com.example.backend.repositories.ImagesRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
@Primary
public class InMemoryImagesRepository extends InMemoryRepository<Images, String> implements ImagesRepository {
    
    public InMemoryImagesRepository() {
        super(Images::getId);
    }
    
    @Override
    public Boolean deleteAllImages() {
        storage.clear();
        return true;
    }
    
    @Override
    public <S extends Images> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }
        if (entity.getCreatedDate() == null) {
            entity.setCreatedDate(LocalDateTime.now());
        }
        return super.save(entity);
    }
}