package com.example.backend.repositories.inmemory;

import com.example.backend.models.User;
import com.example.backend.repositories.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public class InMemoryUserRepository extends InMemoryRepository<User, String> implements UserRepository {

    public InMemoryUserRepository() {
        super(User::getId);

        // Initialize with a default user
        User defaultUser = new User();
        defaultUser.setId(UUID.randomUUID().toString());
        defaultUser.setUsername("user");
        defaultUser.setEmail("user@example.com");
        storage.put(defaultUser.getId(), defaultUser);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return storage.values().stream()
                .anyMatch(user -> user.getEmail() != null && user.getEmail().equals(email));
    }

    @Override
    public Boolean existsByUsername(String username) {
        return storage.values().stream()
                .anyMatch(user -> user.getUsername() != null && user.getUsername().equals(username));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return storage.values().stream()
                .filter(user -> user.getUsername() != null && user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public <S extends User> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID().toString());
        }
        return super.save(entity);
    }
}