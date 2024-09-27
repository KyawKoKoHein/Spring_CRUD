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
import org.springframework.web.bind.annotation.RequestParam;

import com.kkkh.registration.models.Client;
import com.kkkh.registration.models.ClientDto;
import com.kkkh.registration.repositories.ClientRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping({ "", "/" })
    public String getClients(Model model) {
        var clients = clientRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        model.addAttribute("clients", clients);

        return "clients/index";
    }

    // Client Dto state
    @GetMapping({ "create" })
    public String createClient(Model model) {
        ClientDto clientDto = new ClientDto();
        model.addAttribute("clientDto", clientDto);

        return "clients/create";
    }

    // Saving State
    @PostMapping("/create")
    public String createClient(@Valid @ModelAttribute ClientDto clientDto, BindingResult bResult) {

        if (clientRepository.findByEmail(clientDto.getEmail()) != null) {
            bResult.addError(
                    new FieldError("ClientDto", "email", clientDto.getEmail(), false, null, null,
                            "Email address is already in use!"));
        }

        if (bResult.hasErrors()) {
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

    // Edit state
    @GetMapping("/edit")
    public String editClient(Model model, @RequestParam int id) {
        Client client = clientRepository.findById(id).orElse(null);

        if (client == null) {
            return "redirect:/clients";
        }

        ClientDto clientDto = new ClientDto();
        clientDto.setFirstName(client.getFirstName());
        clientDto.setLastName(client.getLastName());
        clientDto.setEmail(client.getEmail());
        clientDto.setPhone(client.getPhone());
        clientDto.setAddress(client.getAddress());
        clientDto.setStatus(client.getStatus());

        model.addAttribute("client", client);
        model.addAttribute("clientDto", clientDto);

        return "clients/edit";
    }

    //update state
    @PostMapping("/edit")
    public String editClient(Model model, @RequestParam int id, @Valid ClientDto clientDto, BindingResult bResult) {
        Client client = clientRepository.findById(id).orElse(null);

        if (client == null) {
            return "redirect:/clients";
        }

        model.addAttribute("client", client);

        if (bResult.hasErrors()) {
            return "clients/edit";
        }

        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setAddress(clientDto.getAddress());
        client.setStatus(clientDto.getStatus());

        try {
            clientRepository.save(client);
            
        } catch (Exception e) {
            // TODO: handle exception
            bResult.addError(new FieldError("clientDto", "email", clientDto.getEmail(), false, null, null, "Email is already in use!"));
        }

        return "redirect:/clients";
    }

    //delete state
    @GetMapping("/delete")
    public String deleteClient(@RequestParam int id){
        Client client = clientRepository.findById(id).orElse(null);

        if(client != null){
            clientRepository.delete(client);
        }

        return "redirect:/clients";
    }
}
