package com.example.projectwaifu.models;

import jakarta.persistence.*;

@Entity
@Table(name = "friends")
public class Friends {

    @EmbeddedId
    private FriendsKey id;


    public Friends() {

    }

    public Friends(String userName, String friendName) {
        this.id = new FriendsKey(userName, friendName);
    }
}
