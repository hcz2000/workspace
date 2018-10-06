package com.jams.authentication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jams.authentication.model.User;

@Repository
public interface UserRepository extends CrudRepository<User,String>  {
    public User findByName(String userName);
}