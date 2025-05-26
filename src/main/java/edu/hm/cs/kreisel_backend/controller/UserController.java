package edu.hm.cs.kreisel_backend.controller;

import edu.hm.cs.kreisel_backend.model.Rental;
import edu.hm.cs.kreisel_backend.model.User;
import edu.hm.cs.kreisel_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
//nur der admin soll diese Methode haben um alle user zu suchen
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
//wieder nur der admin soll diese Methode haben um nach den Usern zu schauen
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
//nur der admin soll diese Methode haben um nach den Usern zu schauen
    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }
//nur der admin soll diese Methode haben um nach den rentals der User zu schauen, der User kann seine eigenen rentals sehen
    @GetMapping("/{id}/rentals")
    public List<Rental> getUserRentals(@PathVariable Long id) {
        return userService.getRentalsByUserId(id);
    }
//nur admin
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }
//nur admin
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }
//nur admin
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
