package com.example.Chess.Repository;

import java.util.Optional;

import com.example.Chess.Model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Retrieves a {@link User} entity from the database by its username.
     * 
     * @param username the username of the user to retrieve
     * @return an {@link Optional} containing the found user, or an empty {@link Optional} if no user is found
     */
    Optional<User> findUserByUsername(String username);
}