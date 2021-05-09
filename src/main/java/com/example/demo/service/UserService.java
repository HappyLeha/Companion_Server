package com.example.demo.service;
import com.example.demo.dto.CreateUserDTO;
import com.example.demo.dto.NewPasswordDTO;
import com.example.demo.dto.UpdateUserDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void createUser(CreateUserDTO createUserDTO);
    void createCode(String email);
    void confirmCode(NewPasswordDTO newPasswordDTO);
    User getByEmailAndPassword(String email, String password);
    User loadUserByUsername(String username);
    User getUser(int id);
    void updateUser(User user, UpdateUserDTO updateUserDTO);
    void deleteUser(User user);
    void calculatePassengerRating(User passenger);
    void calculateDriverRating(User driver);
    void sendEmail(String subject, String message, String to);
}
