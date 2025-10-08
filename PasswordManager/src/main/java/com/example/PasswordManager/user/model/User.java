package com.example.PasswordManager.user.model;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {

    /**
     *ID: primary key
     *username: coloana not null si sa fie unica
     *passwordHas: coloana not null/ se va stoca parola hash
     * saltMaster: criptare
     **/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;
    @Column(nullable = false,unique = true)
    private String username;
    @Column(nullable = false)
    private String passwordHash;
    private String saltMaster;
    private String verifyCyper;
    private String verifyIv;


    /// Constructor
    public User(){}

    public User(Long id, String username, String passwordHash, String saltMaster, String verifyCyper , String verifyIv) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.saltMaster = saltMaster;
        this.verifyCyper = verifyCyper;
        this.verifyIv = verifyIv;
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

    public String getSaltMaster() {
        return saltMaster;
    }

    public void setSaltMaster(String saltMaster) {
        this.saltMaster = saltMaster;
    }

    public String getVerifyCyper() {
        return verifyCyper;
    }

    public void setVerifyCyper(String verifyCyper) {
        this.verifyCyper = verifyCyper;
    }

    public String getVerifyIv() {
        return verifyIv;
    }

    public void setVerifyIv(String verifyIv) {
        this.verifyIv = verifyIv;
    }
}
