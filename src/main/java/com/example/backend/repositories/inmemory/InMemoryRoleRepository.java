package com.example.backend.repositories.inmemory;

import com.example.backend.models.Role;
import com.example.backend.models.RoleEnum;
import com.example.backend.repositories.RoleRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Primary
public class InMemoryRoleRepository extends InMemoryRepository<Role, Long> implements RoleRepository {
    
    private final AtomicLong idSequence = new AtomicLong(1);
    
    public InMemoryRoleRepository() {
        super(Role::getId);
        
        // Initialize with default roles
        Role userRole = new Role();
        userRole.setId(idSequence.getAndIncrement());
        userRole.setName(RoleEnum.ROLE_USER);
        storage.put(userRole.getId(), userRole);
        
        Role moderatorRole = new Role();
        moderatorRole.setId(idSequence.getAndIncrement());
        moderatorRole.setName(RoleEnum.ROLE_MODERATOR);
        storage.put(moderatorRole.getId(), moderatorRole);
        
        Role adminRole = new Role();
        adminRole.setId(idSequence.getAndIncrement());
        adminRole.setName(RoleEnum.ROLE_ADMIN);
        storage.put(adminRole.getId(), adminRole);
    }
    
    @Override
    public Optional<Role> findByName(RoleEnum name) {
        return storage.values().stream()
                .filter(role -> role.getName() == name)
                .findFirst();
    }
    
    @Override
    public <S extends Role> S save(S entity) {
        if (entity.getId() == null) {
            entity.setId(idSequence.getAndIncrement());
        }
        return super.save(entity);
    }
}