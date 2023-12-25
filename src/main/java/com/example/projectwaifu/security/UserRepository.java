package com.example.projectwaifu.security;

import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);

//    @Modifying
//    @Transactional
//    @Query("INSERT INTO User (email, username, password) VALUES(:email, :username, :password)")
//    void saveUser(@Param("email") String email, @Param("username") String username, @Param("password") String password);
}
