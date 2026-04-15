package com.example.librarymanage_be.service.Impl;

import com.example.librarymanage_be.entity.User;
import com.example.librarymanage_be.repo.UserRepository;
import com.example.librarymanage_be.service.UserService;
import com.example.librarymanage_be.utils.EntityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return null;
    }

    @Override
    public User findEntityById(Integer id) {
        return EntityUtils.getOrThrow(userRepository.findById(id),
                "User not found with id=" + id);
    }

    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

}
