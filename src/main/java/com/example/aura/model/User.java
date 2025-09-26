package com.example.aura.model;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class User {
    private String userId;
    private String email;
    private String password;
    private String name;
    private List<String> friendIds;
    private Date createdAt;
    private Date updatedAt;

}
