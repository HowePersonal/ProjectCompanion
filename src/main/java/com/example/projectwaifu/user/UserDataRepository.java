package com.example.projectwaifu.user;

import com.example.projectwaifu.user.UserData;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDataRepository extends JpaRepository<UserData, Long> {
    @Query("SELECT u FROM UserData u WHERE u.userId = :userid")
    UserData getUserData(Integer userid);

    @Query(value="SELECT count(*) FROM user_chat u where u.user_id = :userid", nativeQuery = true)
    int getUserTotalMessages(Integer userid);

    @Query(value = "SELECT coins FROM user_coins WHERE user_id = :userid", nativeQuery = true)
    Integer getCoins(Integer userid);

    @Modifying
    @Transactional
    @Query("UPDATE UserData u SET u.description = :description WHERE u.userId = :userId")
    void updateDescriptionByUserId(Integer userId, String description);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_coins (user_id, coins) VALUES(:userid, :coins)", nativeQuery = true)
    void initializeCoins(Integer userid, int coins);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_coins SET coins = coins + 1 WHERE user_id = :userid", nativeQuery = true)
    void incrementCoins(Integer userid);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_coins SET coins = coins + :newCoins WHERE user_id = :userid", nativeQuery = true)
    void addCoins(Integer userid, int newCoins);
}
