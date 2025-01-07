package de.tum.cit.aet.pse.controller;

import de.tum.cit.aet.pse.entity.Customer;
import de.tum.cit.aet.pse.entity.Person;
import de.tum.cit.aet.pse.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String userType) {
        return authService.register(name, email, password, userType);
    }

    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestParam String email,
                                      @RequestParam String password,
                                      HttpSession session) {
        return authService.login(email, password, session);
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        authService.logout(session);
        return "Logout successful";
    }
    @GetMapping("/profile")
    public ResponseEntity<Person> getProfile(HttpSession session) {
        return ResponseEntity.ok(authService.profile(session));
    }
    @GetMapping("/isCustomer")
    public ResponseEntity<Boolean> getProfileBoolean(HttpSession session) {
        return ResponseEntity.ok(authService.profileBoolean(session));
    }
}
