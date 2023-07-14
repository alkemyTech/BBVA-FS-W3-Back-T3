package com.bbva.wallet.seeder;

import com.bbva.wallet.entities.Role;
import com.bbva.wallet.entities.User;
import com.bbva.wallet.enums.RoleName;
import com.bbva.wallet.repositories.UserRepository;
import com.bbva.wallet.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSeeder {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    public List<User> seedUsers() {
        if (userRepository.count() != 0) {
            return List.of();
        }
        Role adminRole = roleService.getRoleByName(RoleName.ADMIN);
        Role userRole = roleService.getRoleByName(RoleName.USER);

        User user1 = createUser("Diego","Aprosoff","diegoaprosoff@email.com","123",userRole);
        User user2 = createUser("Diego","Martin Perez","diegomartin.perez@email.com","123",userRole);
        User user3 = createUser("Evaristo","Compagnucci","evaristocompagnucci@email.com","123",userRole);
        User user4 = createUser("Melody","Amaro","melodyamaro@email.com","123",userRole);
        User user5 = createUser("Neyen","Ergas","neyenergas@email.com","123",userRole);
        User user6 = createUser("Rodrigo","Juarez","rodrigojuarez@email.com","123",userRole);
        User user7 = createUser("Simon","Nava","simonnava@email.com","123",userRole);
        User user8 = createUser("Soledad","Grilletta","soledadgrilletta@email.com","123",userRole);
        User user9 = createUser("Valentino","Veralli","valentinoveralli@email.com","123",userRole);
        User user10 = createUser("Michelle","Obama","micheleobama@email.com","123",userRole);

        User user11 = createUser("Lionel","Messi","lionelmessi@email.com","admin",adminRole);
        User user12 = createUser("Ángel","Di María","ángeldi.maría@email.com","admin",adminRole);
        User user13 = createUser("Lautaro","Martínez","lautaromartínez@email.com","admin",adminRole);
        User user14 = createUser("Paulo","Dybala","paulodybala@email.com","admin",adminRole);
        User user15 = createUser("Ángel","Correa","ángelcorrea@email.com","admin",adminRole);
        User user16 = createUser("Rodrigo","De Paul","rodrigode.paul@email.com","admin",adminRole);
        User user17 = createUser("Leandro","Paredes","leandroparedes@email.com","admin",adminRole);
        User user18 = createUser("Nicolás","Tagliafico","nicolástagliafico@email.com","admin",adminRole);
        User user19 = createUser("Gonzalo","Montiel","gonzalomontiel@email.com","admin",adminRole);
        User user20 = createUser("Nicolás","Otamendi","nicolásotamendi@email.com","admin",adminRole);

        return userRepository.saveAll(java.util.List.of(user1, user2, user3, user4, user5, user6, user7, user8,
                user9, user10,
                user11, user12, user13, user14, user15, user16, user17, user18, user19, user20));
    }

    private User createUser(String firstName, String lastName, String email, String password, Role role) {
        return User.builder()
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .password(passwordEncoder.encode(password))
            .role(role)
            .build();
    }
}