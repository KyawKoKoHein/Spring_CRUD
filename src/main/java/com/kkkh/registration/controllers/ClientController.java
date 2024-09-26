package com.kkkh.registration.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kkkh.registration.repositories.ClientRepository;

@Controller
@RequestMapping("/clients")
public class ClientController {
    
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping()
    public String getClients(){
        var clients = clientRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        return "clients/index";
    }
}
