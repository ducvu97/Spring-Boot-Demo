package com.example.demo.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserDao, Long> {
    UserDao findByUsername(String username);

}
