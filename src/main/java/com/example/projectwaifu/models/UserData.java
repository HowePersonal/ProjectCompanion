package com.example.projectwaifu.models;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.Date;

@Entity
@Table(name="user_data")
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(name = "user_id")
    private Long userId;

    @NonNull
    @Column(name = "profile_pic")
    private Integer profilePic;

    @Column(name = "description")
    private String description;

    @NonNull
    @Column(name = "tag")
    private String tag;

    @NonNull
    @Column(name = "join_date")
    private Date joinDate = new Date();

    public UserData() {

    }

    public UserData(Long user_id, int profile_pic, String description, String tag) {
        this.userId = user_id;
        this.profilePic = profile_pic;
        this.description = description;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long user_id) {
        this.userId = user_id;
    }

    public int getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(int profile_pic) {
        this.profilePic = profile_pic;
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
