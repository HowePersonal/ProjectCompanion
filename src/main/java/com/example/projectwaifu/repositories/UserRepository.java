package com.example.projectwaifu.repositories;

import com.example.projectwaifu.models.User;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param String email);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(@Param String username);

    @Query("SELECT u.username FROM User u WHERE u.email = :email")
    String getUsername(@Param String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET username = :username WHERE email = :email", nativeQuery = true)
    void changeUsername(String username, String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET password = :password WHERE email = :email", nativeQuery = true)
    void changePassword(String password, String email);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_chat (user_id, input, response) VALUES(:userid, :input, :response)", nativeQuery = true)
    void saveChatLog(Long userid, String input, String response);

    @Query(value = "SELECT u.input, u.response FROM user_chat u WHERE user_id = :userid", nativeQuery = true)
    List<Map<String, Object>> getChatLog(Long userid);
}
