package com.example.PasswordManager.vault.model;

import com.example.PasswordManager.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Vault {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String domain;
    private String username;
    private String passwordCrypt;
    private String iv;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}