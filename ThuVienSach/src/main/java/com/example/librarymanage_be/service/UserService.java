package com.example.librarymanage_be.service;

import com.example.librarymanage_be.entity.Users;


public interface UserService {
    Users create(Users user);


    Users findEntityById(Integer id);


    Users update(Users user);

    void delete(Integer id);
}
