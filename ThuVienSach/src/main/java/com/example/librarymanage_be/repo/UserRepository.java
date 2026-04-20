package com.example.librarymanage_be.repo;

import com.example.librarymanage_be.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> , PagingAndSortingRepository<Users,Integer> {
    boolean existsByEmail(String email);

     Users findByEmail(String email);
}
