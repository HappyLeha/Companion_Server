package com.example.demo.service;
import com.example.demo.dto.CreateUserDTO;
import com.example.demo.dto.NewPasswordDTO;
import com.example.demo.dto.UpdateUserDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Code;
import com.example.demo.entity.User;
import com.example.demo.exception.InvalidPasswordException;
import com.example.demo.exception.ResourceAlreadyExistException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnknownServerException;
import com.example.demo.repository.CodeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Calendar;
import java.util.Properties;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CodeRepository codeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${codeSubject}")
    private String codeSubject;

    @Value("${codeMessage}")
    private String codeMessage;

    @Value("${emailAddress}")
    private String from;

    @Value("${emailPassword}")
    private String password;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           CodeRepository codeRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.codeRepository = codeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(String username)  {
        if (userRepository.findByEmail(username).isPresent()) {
            return userRepository.findByEmail(username).get();
        }
        else {
            throw new ResourceNotFoundException("User with this email doesn't exist.");
        }
    }

    @Override
    public void createUser(CreateUserDTO createUserDTO) {
        User user;

        if (userRepository.existsByEmail(createUserDTO.getEmail())) {
            throw new ResourceAlreadyExistException("User with this email already exist");
        }
        user = Mapper.convertToUser(createUserDTO);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void createCode(String email) {
        User user;
        Code code;

        codeRepository.deleteByExpiryDateLessThan(Calendar.getInstance());
        user = loadUserByUsername(email);
        code = user.getCode();
        if (code == null) {
            code = new Code(user);
            codeRepository.save(code);
        }
        codeMessage += " " + code.getCode();
        sendEmail(codeSubject, codeMessage, email);
    }

    @Override
    public void confirmCode(NewPasswordDTO newPasswordDTO) {
        User user;

        codeRepository.deleteByExpiryDateLessThan(Calendar.getInstance());
        user = loadUserByUsername(newPasswordDTO.getEmail());
        if (!codeRepository.existsByCodeAndUser(newPasswordDTO.getCode(), user)) {
            throw new ResourceNotFoundException("This code is wrong");
        }
        user.setPassword(bCryptPasswordEncoder.encode(
                newPasswordDTO.getNewPassword()));
        codeRepository.delete(user.getCode());
    }

    @Override
    @Transactional(readOnly = true)
    public User getByEmailAndPassword(String email, String password) {
        User user = loadUserByUsername(email);

        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        else {
            throw new InvalidPasswordException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(int id) {
        User user;

        if (!userRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("User with this id doesn't exist.");
        }
        user = userRepository.findById(id).get();
        calculateDriverRating(user);
        calculatePassengerRating(user);
        return user;
    }

    @Override
    public void updateUser(User user, UpdateUserDTO updateUserDTO) {
        if (!user.getEmail().equals(updateUserDTO.getEmail())
            && userRepository.existsByEmail(updateUserDTO.getEmail())) {
            throw new ResourceAlreadyExistException("User with this email already exist");
        }
        if (updateUserDTO.getPasswords() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(updateUserDTO.getPasswords()
                                                          .getPassword()));
        }
        user.setFirstName(updateUserDTO.getFirstName());
        user.setLastName(updateUserDTO.getLastName());
        user.setEmail(updateUserDTO.getEmail());
        user.setPhone(updateUserDTO.getPhone());
        user.setPhoto(updateUserDTO.getPhoto());
        user.setNote(updateUserDTO.getNote());
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public void calculatePassengerRating(User passenger) {
        passenger.setPassengerRating(userRepository.calculatePassengerRatingByUser(passenger));
    }

    @Override
    @Transactional(readOnly = true)
    public void calculateDriverRating(User driver) {
        driver.setDriverRating(userRepository.calculateDriverRatingByUser(driver));
    }

    @Override
    @Transactional(readOnly = true)
    public void sendEmail(String subject, String message, String to) {
        Properties props;
        Session session;
        MimeMessage mimeMessage;

        try {
            props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });
            mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);
            Transport.send(mimeMessage);
        }
        catch (MessagingException e) {
            throw new UnknownServerException();
        }
    }
}
