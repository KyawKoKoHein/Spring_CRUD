package com.kkkh.registration.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kkkh.registration.models.Client;
import com.kkkh.registration.models.ClientDto;
import com.kkkh.registration.repositories.ClientRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/clients")
public class ClientController {
    
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping({"", "/"})
    public String getClients(Model model){
        var clients = clientRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        model.addAttribute("clients", clients);

        return "clients/index";
    }


    @GetMapping({"create"})
    public String createClient(Model model){
        ClientDto clientDto = new ClientDto();
        model.addAttribute("clientDto", clientDto);
        
        return "clients/create";
    }

    @PostMapping("/create")
    public String createClient( @Valid @ModelAttribute ClientDto clientDto, BindingResult bResult ){

        if(clientRepository.findByEmail(clientDto.getEmail()) != null){
            bResult.addError(
                new FieldError("ClientDto", "email", clientDto.getEmail(), false, null, null, "Email address is already in use!")
            );
        }

        if(bResult.hasErrors()){
            return "clients/create";
        }

        Client client = new Client();
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setAddress(clientDto.getAddress());
        client.setStatus(clientDto.getStatus());
        client.setCreatedAt(new Date());

        clientRepository.save(client);

        return "redirect:/clients";
    }
}
