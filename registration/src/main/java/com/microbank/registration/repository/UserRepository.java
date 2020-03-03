package com.microbank.registration.repository;

import com.microbank.registration.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * A JpaRepository for saving user information.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {}
