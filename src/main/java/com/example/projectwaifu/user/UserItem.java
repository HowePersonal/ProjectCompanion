package com.example.projectwaifu.user;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Table(name="user_items")
public class UserItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "user_id")
    private Integer userId;

    @NonNull
    @Column(name = "product_id")
    private String productId;

    public UserItem() {
    }

    public UserItem(Integer userId, String productId) {
        this.userId = userId;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
