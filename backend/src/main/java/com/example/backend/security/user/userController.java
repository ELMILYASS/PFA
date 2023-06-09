package com.example.backend.security.user;

import com.example.backend.Exception.NonExistentUser;
import com.example.backend.security.config.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class userController {
    @Autowired
    private UserRepository userRepository ;

    @PostMapping("/user")
    private void addUser(@RequestBody User user) {
        user.setCreatedAt(new Date());
        this.userRepository.save(user);
    }
    @DeleteMapping("/user/{id}")
    private void DeleteUser(@PathVariable int id) {
        this.userRepository.deleteById(id);

    }
    @GetMapping("/users")
    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }

    @GetMapping("/user/{idUser}")
    public User getUserById(@PathVariable int idUser) {
        Optional<User> user= this.userRepository.findById(idUser);
        if(user.isPresent()){
            return user.get();
        }
        else{
            throw new NonExistentUser(idUser);
        }
    }

    private final JwtService jwtService = new JwtService();


    @PreAuthorize("permitAll()")
    @GetMapping("/test/user")
    public String checkUser(@RequestHeader("Authorization") String authHeader){

        String jwt = authHeader.substring(7);


        return "Authentificated";
    }


    @PutMapping("/user/{idUser}")
    public ResponseEntity<User> updateEmployee(@PathVariable int idUser, @RequestBody User user) {
        User updateUser = this.userRepository.findById(idUser)
                .orElseThrow(() -> new NonExistentUser(idUser));
        updateUser.setEmail(user.getEmail());
        updateUser.setFirstname(user.getFirstname());
        updateUser.setLastname(user.getLastname());
        updateUser.setPhone(user.getPhone());
        updateUser.setAddress(user.getAddress());
        updateUser.setPassword(user.getPassword());
        this.userRepository.save(updateUser);
        return ResponseEntity.ok(updateUser);
    }

}
