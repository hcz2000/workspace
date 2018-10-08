package com.jams.authentication.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jams.authentication.model.UserRole;


@Repository
public interface UserRoleRepository extends CrudRepository<UserRole,Integer>  {
    public List<UserRole> findByUserName(String userName);
}