package com.ekomora.springjwt.controllers;

import com.ekomora.springjwt.DTO.MaterialDto;
import com.ekomora.springjwt.models.Material;
import com.ekomora.springjwt.models.Profile;
import com.ekomora.springjwt.models.User;
import com.ekomora.springjwt.repository.MaterialRepository;
import com.ekomora.springjwt.repository.ProfileRepository;
import com.ekomora.springjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/materials")
    public List<MaterialDto> getAllMaterials() {
        return materialRepository.findAllProjectedBy();
    }

    @GetMapping("/users/{userId}/materials")
    public ResponseEntity<List<Material>> getAllMaterialsByUserId(@PathVariable(value = "userId") Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Not found User with id = " + userId);
        }

        List<Material> materials = materialRepository.findAllByUserId(userId);
        return new ResponseEntity<>(materials, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/materials")
    public ResponseEntity<Material> createMaterial(@PathVariable(value = "userId") Long userId,
                                                   @RequestBody Material materialRequest) {
        Material material = userRepository.findById(userId).map(user -> {
            materialRequest.setUser(user);
            return materialRepository.save(materialRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));

        return new ResponseEntity<>(material, HttpStatus.CREATED);
    }

    @PutMapping("/users/{userId}/materials/{materialId}")
    public Material updateMaterial(@PathVariable(value = "userId") Long userId,
                                   @PathVariable(value = "materialId") Long materialId,
                                   @Valid @RequestBody Material materialRequest) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("UserId " + userId + " not found");
        }

        return materialRepository.findById(materialId).map(material -> {
            material.setTitle(materialRequest.getTitle());
            material.setInventoryNumber(materialRequest.getInventoryNumber());
            return materialRepository.save(material);
        }).orElseThrow(() -> new ResourceNotFoundException("MaterialId " + materialId + "not found"));
    }

    @DeleteMapping("/users/{userId}/materials/{materialId}")
    public ResponseEntity<?> deleteMaterial(@PathVariable(value = "userId") Long userId,
                                            @PathVariable(value = "materialId") Long materialId) {
        return materialRepository.findByIdAndUserId(materialId, userId).map(material -> {
            materialRepository.delete(material);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Material not found with id " + materialId + " and UserId " + userId));
    }


    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()) {
            return new ResponseEntity<>(userData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
//            String email, String firstName, String lastName,
//                    String post, String avatar, String password
            User _user = new User(
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
//                    user.getPost(),
                    user.getAvatar(),
                    encoder.encode(user.getPassword())
            );
            return new ResponseEntity<>(userRepository.save(_user), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        Optional<User> userData = userRepository.findById(id);
        Optional<Profile> profileData = profileRepository.findById(id);

        if (userData.isPresent()) {
            User _user = userData.get();
            _user.setEmail(user.getEmail());
            _user.setFirstName(user.getFirstName());
            _user.setLastName(user.getLastName());
            _user.setAvatar(user.getAvatar());
            _user.setPassword(user.getPassword());

            Profile _profile = profileData.get();
            _profile.setPost(user.getProfile().getPost());

            _user.setProfile(_profile);
            _profile.setUser(_user);

            return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


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

    //    @GetMapping("/dashboard/user")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//    public String userAccess() {
//        return "User Content.";
//    }
}
