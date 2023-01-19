package com.ekomora.springjwt;

import com.ekomora.springjwt.models.ERole;
import com.ekomora.springjwt.models.Profile;
import com.ekomora.springjwt.models.Role;
import com.ekomora.springjwt.models.User;
import com.ekomora.springjwt.repository.RoleRepository;
import com.ekomora.springjwt.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class SpringBootSecurityJwtApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    final PasswordEncoder encoder;

    public SpringBootSecurityJwtApplication(UserRepository userRepository,
                                            RoleRepository roleRepository,
                                            PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        createAdminAccount();
    }

    private void createAdminAccount() {
        // check if user table is empty
        if (userRepository.count() == 0) {
            Optional<Role> adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
            if (adminRole.isPresent()) {
                adminData(adminRole);
            }
        } else {
            // check if admin account exists
            Optional<User> admin = userRepository.findByEmail("admin@erobota.com");
            if (!admin.isPresent()) {
                Optional<Role> adminRole = roleRepository.findByName(ERole.ROLE_ADMIN);
                adminData(adminRole);
            }
        }
    }

    private void adminData(Optional<Role> adminRole) {
        User admin = new User(
                "admin@erobota.com",
                "admin",
                "admin",
                null,
                encoder.encode("adminadmin"));

        Profile userProfile = new Profile(
                "Admin", null, null,
                null, null, null);

        admin.setProfile(userProfile);
        userProfile.setUser(admin);
        admin.getRoles().add(adminRole.get());
        userRepository.save(admin);
    }
}
