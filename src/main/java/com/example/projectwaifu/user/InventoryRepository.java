package com.example.projectwaifu.user;

import com.example.projectwaifu.user.UserItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<UserItem, Long> {
    @Query("SELECT u.productId FROM UserItem u WHERE u.userId = :userid AND u.productId like CONCAT(:productName, '%')")
    List<String> getPurchased(Integer userid, String productName);

    boolean existsByProductId(String product_id);
}
