package com.jams.myapp.dao;

import java.util.List;

import com.jams.myapp.entity.User;

public interface UserDao {  
    public User findUserById(String id); 
    public List<User> listAllUsers(); 
}  
