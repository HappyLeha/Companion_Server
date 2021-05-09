package com.example.demo.controller;
import com.example.demo.dto.PasswordsDTO;
import com.example.demo.dto.UpdateUserDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.ValidationFailureException;
import com.example.demo.service.UserService;
import com.example.demo.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my")
    public UserDTO getUser(Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());

        userService.calculateDriverRating(user);
        userService.calculatePassengerRating(user);
        return Mapper.convertToUserDTO(user);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable int id) {
        return Mapper.convertToUserDTO(userService.getUser(id));
    }

    @PutMapping("/my")
    public void updateUser(@RequestBody @Valid UpdateUserDTO updateUserDTO,
                           Principal principal) {
       User user = userService.loadUserByUsername(principal.getName());
       PasswordsDTO passwordsDTO = updateUserDTO.getPasswords();

       if (passwordsDTO != null &&
           !passwordsDTO.getPassword()
           .equals(passwordsDTO.getRepeatPassword())) {
            throw new ValidationFailureException(
                    "passwords aren't equal");
       }
       userService.updateUser(user, updateUserDTO);
    }

    @DeleteMapping("/my")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(Principal principal) {
        User user = userService.loadUserByUsername(principal.getName());

        userService.deleteUser(user);
    }
}
