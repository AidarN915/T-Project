package Tproject.config;

import Tproject.model.User;
import Tproject.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class StartupDataLoader {

    @Bean
    public CommandLineRunner initSuperAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            if (userRepository.findByUsername("superadmin").isEmpty()) {

                User superAdmin = new User();
                superAdmin.setUsername("superadmin");
                superAdmin.setPassword(passwordEncoder.encode("superpassword"));
                superAdmin.setRole("SUPERADMIN");

                userRepository.save(superAdmin);

                System.out.println("SUPERADMIN created");
            }
        };
    }
}
