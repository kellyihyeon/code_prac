package com.backend.teamtalk.repository;

import com.backend.teamtalk.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
