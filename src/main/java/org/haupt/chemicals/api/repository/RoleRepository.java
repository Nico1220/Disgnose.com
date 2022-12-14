package org.haupt.chemicals.api.repository;

import org.haupt.chemicals.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;


public interface RoleRepository extends JpaRepository<Role, Long> {
    public Set<Role> findByName(String USER);
}
