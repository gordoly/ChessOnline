package com.example.Chess.Controller;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.CrossOrigin; // uncomment for testing react client
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Chess.Model.User;
import com.example.Chess.Repository.UserRepository;
import com.example.Chess.Service.JwtService;
import com.example.Chess.Service.Users;

@Controller
@RequestMapping("/")
public class RESTController {
    private final Users users;

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public RESTController(JwtService jwtService, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, Users users, UserRepository repository) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.users = users;
        this.repository = repository;
    }

    /**
     * Forwards all get requests, except those beginning with a request to /api,
     * to index.html, which performs its own routing.
     * @return the string that forwards requests to index.html.
     */
    @GetMapping({
        "/",
        "/{path:^(?!static|api|.*\\.html$).*}/**"
    })
    public String forward() {
        return "forward:/index.html";
    }

    @PostMapping("/api/users/auth/register")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        String errMsg = null;
        if (user.getUsername() == null) {
            errMsg = "the username cannot be null.";
        } else if (user.getPassword() == null || user.getReEnteredPassword() == null) {
            errMsg = "the password cannot be null.";
        } else if (user.getUsername().isEmpty()) {
            errMsg = "the username cannot be empty."; 
        } else if (user.getPassword().isEmpty() || user.getReEnteredPassword().isEmpty()) {
            errMsg = "the password cannot be empty.";
        } else if (!user.getPassword().equals(user.getReEnteredPassword())) {
            errMsg = "the password and re-entered password do not match.";
        }

        if (errMsg == null) {
            try {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                repository.save(user);
                return new ResponseEntity<String>("User created successfully", HttpStatus.CREATED);
            } catch (Exception e) {
                errMsg = "the username may already have been taken or it is over 20 characters long."; 
            }
        }

        return new ResponseEntity<String>("Error registering the user: " + errMsg, HttpStatus.INTERNAL_SERVER_ERROR);
    }
     
    @PostMapping("/api/users/auth/login")
    public ResponseEntity<String> authenticate(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()));

            String jwtToken = jwtService.generateToken(user);

            if (users.getUser(user.getUsername()) == 0) {
                users.addUser(user.getUsername());
            }

            return ResponseEntity.ok(jwtToken);
        } catch (Exception e) {
            return new ResponseEntity<>("Authentication failed: invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/api/users/get")
    public ResponseEntity<Optional<User>> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> user = repository.findUserByUsername(username);
        if (user.isPresent()) {
            Integer secureNum = users.getUser(username);
            user.get().setSecureNum(secureNum);
            user.get().setPassword(null);
            return ResponseEntity.ok(user);
        }

        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/api/users/leaderboard")
    public ResponseEntity<List<User>> getLeaderBoard() {
        List<User> users = repository.findAll();
        users.sort(Comparator.comparing(User::getScore).reversed());
        return ResponseEntity.ok(users);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "/api/users/add")
    public ResponseEntity<Object> setUsername() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (users.getUser(username) == 0) {
            users.addUser(username);
        }
 
        return ResponseEntity.ok(null);
    }
}