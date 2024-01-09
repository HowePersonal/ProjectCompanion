package com.example.projectwaifu.models;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class FriendsKey implements Serializable {
    private String userName;
    private String friendName;

    public String getUserName() {        return userName;
    }

    public String getFriendName() {
        return friendName;
    }

    public FriendsKey() {
    }

    public FriendsKey(String userName, String friendName) {
        this.userName = userName;
        this.friendName = friendName;
    }
}
