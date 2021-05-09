package com.example.demo.repository;
import com.example.demo.entity.Code;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Calendar;

public interface CodeRepository extends JpaRepository<Code,Integer> {
    void deleteByExpiryDateLessThan(Calendar date);
    boolean existsByCodeAndUser(String code, User user);
}
