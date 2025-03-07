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
import org.springframework.web.bind.annotation.CrossOrigin;
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
     * Registers a new user with the provided information in the request body of the post request.
     * Ensures that the user credentials are validated before saving the user.
     * 
     * @param user the user details from the request body to be registered
     * @return a ResponseEntity with a success message or an error message
     */
    @CrossOrigin(origins = "https://gordoly.github.io/")
    @PostMapping("/api/users/auth/register")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        String errMsg = null;
        
        // validate user credentials
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

        // encode the password and save the user details onto the database if there are no validation issues
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
    
    /**
     * Authenticates a user by verifying the provided username and password.
     * If successful, generates a JWT token and returns it to the client.
     * 
     * @param user the user credentials to authenticate
     * @return a ResponseEntity containing the JWT token if authentication is successful, 
     *         or an error message if authentication fails
     */
    @CrossOrigin(origins = "https://gordoly.github.io/")
    @PostMapping("/api/users/auth/login")
    public ResponseEntity<String> authenticate(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()));

            String jwtToken = jwtService.generateToken(user);

            // if the user has not already been added to the Users manager, add them
            if (users.getUser(user.getUsername()) == 0) {
                users.addUser(user.getUsername());
            }

            return ResponseEntity.ok(jwtToken);
        } catch (Exception e) {
            return new ResponseEntity<>("Authentication failed: invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Retrieves the details of the currently authenticated user and returns it to the user.
     * 
     * @return a ResponseEntity containing the user's details, or an error if the user is not found
     * @throws Exception if an error occurs while processing the request or the user is not authenticated
     */
    @CrossOrigin(origins = "https://gordoly.github.io/")
    @GetMapping(path = "/api/users/get")
    public ResponseEntity<Optional<User>> getUserDetails() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> user = repository.findUserByUsername(username);
        if (user.isPresent()) {
            // generates a secure number which will need to be sent in the socket messages to
            // ensure the server can track the user's identity
            Integer secureNum = users.getUser(username);
            user.get().setSecureNum(secureNum);
            user.get().setPassword(null);
            return ResponseEntity.ok(user);
        }

        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Retrieves the leaderboard by fetching all users and sorting them by score in descending order.
     * 
     * @return a ResponseEntity containing the sorted list of users on the leaderboard
     */
    @CrossOrigin(origins = "https://gordoly.github.io/")
    @GetMapping(path = "/api/users/leaderboard")
    public ResponseEntity<List<User>> getLeaderBoard() {
        List<User> users = repository.findAll();
        users.sort(Comparator.comparing(User::getScore).reversed());
        return ResponseEntity.ok(users);
    }

    /**
     * Adds a user to the Users manager if they do not already exist, allowing them to join
     * a chess game session.
     * 
     * @return a ResponseEntity with a success status
     * @throws Exception if an error occurs while processing the request or if the user is not authenticated
     */
    @CrossOrigin(origins = "https://gordoly.github.io/")
    @PostMapping(path = "/api/users/add")
    public ResponseEntity<Object> addUserToList() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (users.getUser(username) == 0) {
            users.addUser(username);
        }
 
        return ResponseEntity.ok(null);
    }
}