package com.example.Chess.Repository;

import java.util.Optional;

import com.example.Chess.Model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);
}
