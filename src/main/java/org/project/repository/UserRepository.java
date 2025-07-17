package org.project.repository;

import java.util.Optional;

import org.project.model.Command;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Command, Long> {
    Optional<Command> findByUsername(String username);
}