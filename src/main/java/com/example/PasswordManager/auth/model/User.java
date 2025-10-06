package com.example.PasswordManager.auth.model;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {

    /**
     *ID: primary key
     *username: coloana not null si sa fie unica
     *passwordHas: coloana not null/ se va stoca parola hash
     **/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    @Column(nullable = false,unique = true)
    private String username;
    @Column(nullable = false)
    private String passwordHash;

    /// Constructor
    public User(){}

    public User(Long id, String username, String passwordHash) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    /// Setter and Getter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

}
