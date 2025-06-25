package com.example.backend.repositories.inmemory;

import com.example.backend.models.Categories;
import com.example.backend.repositories.CategoriesRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Primary
public class InMemoryCategoriesRepository extends InMemoryRepository<Categories, String> implements CategoriesRepository {
    
    public InMemoryCategoriesRepository() {
        super(Categories::getId);
        
        // Initialize with some default categories
        Categories workCategory = Categories.builder()
                .id(UUID.randomUUID().toString())
                .name("Work")
                .color("#FF5733")
                .createdDate(System.currentTimeMillis())
                .lastModifiedDate(System.currentTimeMillis())
                .build();
        storage.put(workCategory.getId(), workCategory);
        
        Categories personalCategory = Categories.builder()
                .id(UUID.randomUUID().toString())
                .name("Personal")
                .color("#33FF57")
                .createdDate(System.currentTimeMillis())
                .lastModifiedDate(System.currentTimeMillis())
                .build();
        storage.put(personalCategory.getId(), personalCategory);
    }
    
    @Override
    public <S extends Categories> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }
        
        // Set timestamps if not already set
        if (entity.getCreatedDate() == null) {
            entity.setCreatedDate(System.currentTimeMillis());
        }
        entity.setLastModifiedDate(System.currentTimeMillis());
        
        return super.save(entity);
    }
}