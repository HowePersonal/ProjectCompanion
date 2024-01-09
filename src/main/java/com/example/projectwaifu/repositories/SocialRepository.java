package com.example.projectwaifu.repositories;

import com.example.projectwaifu.models.Friends;
import com.example.projectwaifu.models.FriendsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialRepository extends JpaRepository<Friends, FriendsKey> {

    @Query("SELECT f.id.friendName FROM Friends f WHERE f.id.userName = :userName")
    List<String> getFriends(String userName);


}
