package com.kkkh.registration.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kkkh.registration.models.Client;

public interface ClientRepository extends JpaRepository<Client, Integer > {
    public Client findByEmail(String email);

}
