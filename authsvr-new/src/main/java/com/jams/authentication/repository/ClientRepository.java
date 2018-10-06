package com.jams.authentication.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jams.authentication.model.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client,String>  {
    public Client findById(String clientId);
}