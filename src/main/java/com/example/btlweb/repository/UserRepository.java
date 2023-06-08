package com.example.btlweb.repository;

import com.example.btlweb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.ten_tk = :username")
    User findByTen_tk(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(u.ten_tk) > 0 THEN true ELSE false END FROM User u WHERE u.ten_tk = :username")
    boolean existsByUsername(@Param("username") String username);

}
