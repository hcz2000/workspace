package com.jams.authentication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jams.authentication.model.UserOrganization;

@Repository
public interface UserOrganizationRepository extends CrudRepository<UserOrganization,String>  {
    public UserOrganization findByUserName(String userName);
}