package com.example.librarymanage_be.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "user_name")
    private String username;

    private String email;

    private String phone;

    private String address;

    private String password;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<UserRole> userRoles = new HashSet<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
