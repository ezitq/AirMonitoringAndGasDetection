package com.bohdan.airmonitoring.repository;

import com.bohdan.airmonitoring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserJpaRepository extends JpaRepository<User,Integer> {

    User findUserByEmail(String email);

    User findUserById(int id);
}
