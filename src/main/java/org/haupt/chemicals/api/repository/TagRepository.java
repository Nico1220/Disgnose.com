package org.haupt.chemicals.api.repository;

import org.haupt.chemicals.api.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
