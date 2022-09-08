package com.example.demo.respository;

import com.example.demo.model.UserDao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserDao, Long> {
    UserDao findByUsername(String username);
    List<UserDao> findAll();
}
