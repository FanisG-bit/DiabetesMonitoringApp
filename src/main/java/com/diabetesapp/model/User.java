package com.diabetesapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    private int userId;

    private String username;

    private String password;

    private UserType userType;

}
