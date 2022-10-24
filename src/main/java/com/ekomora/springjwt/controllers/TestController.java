package com.ekomora.springjwt.controllers;

import com.ekomora.springjwt.DTO.MaterialDto;
import com.ekomora.springjwt.models.ContactForm;
import com.ekomora.springjwt.models.Material;
import com.ekomora.springjwt.models.Profile;
import com.ekomora.springjwt.models.User;
import com.ekomora.springjwt.repository.MaterialRepository;
import com.ekomora.springjwt.repository.ProfileRepository;
import com.ekomora.springjwt.repository.UserRepository;
import com.ekomora.springjwt.services.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/dashboard/employee")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String employeeAccess() {
        return "Employee Content.";
    }

    @GetMapping("/dashboard/hr")
    @PreAuthorize("hasRole('HR')")
    public String hrAccess() {
        return "HR Board.";
    }

    @GetMapping("/dashboard/accountant")
    @PreAuthorize("hasRole('ACCOUNTANT')")
    public String accountantAccess() {
        return "Accountant Board.";
    }

    @PostMapping("/contactForm")
    public ResponseEntity<HttpStatus> sendContactFormMessage(@RequestBody ContactForm contactForm) {
        try {
            PasswordService.sendToMailContactData(
                    contactForm.getName(),
                    contactForm.getEmail(),
                    contactForm.getMessage()
            );
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    @GetMapping("/dashboard/user")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//    public String userAccess() {
//        return "User Content.";
//    }
}
