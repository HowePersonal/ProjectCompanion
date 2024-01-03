package com.example.projectwaifu.repositories;

import com.example.projectwaifu.models.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    @Query("SELECT u FROM UserData u WHERE u.user_id = :userid")
    UserData getUserData(Long userid);

    @Query(value="SELECT count(*) FROM user_chat u where u.user_id = :userid", nativeQuery = true)
    int getUserTotalMessages(Long userid);

    @Query(value = "SELECT coins FROM user_coins WHERE user_id = :userid", nativeQuery = true)
    Integer getCoins(Long userid);
}
