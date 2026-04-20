package com.example.librarymanage_be.service.Impl;

import com.example.librarymanage_be.entity.Users;
import com.example.librarymanage_be.repo.UserRepository;
import com.example.librarymanage_be.service.UserService;
import com.example.librarymanage_be.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Users create(Users user) {
        return userRepository.save(user);
    }



    @Override
    public Users findEntityById(Integer id) {
        return EntityUtils.getOrThrow(userRepository.findById(id),
                "User not found with id=" + id);
    }


    @Override
    public Users update(Users user) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

}
