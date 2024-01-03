package com.example.projectwaifu.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name="user_data")
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long user_id;

    private int profile_pic;

    private String description;

    private String tag;

    private Date join_date = new Date();

    public UserData() {

    }

    public UserData(Long user_id, int profile_pic, String description, String tag) {
        this.user_id = user_id;
        this.profile_pic = profile_pic;
        this.description = description;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public int getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(int profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}
