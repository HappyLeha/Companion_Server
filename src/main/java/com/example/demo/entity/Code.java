package com.example.demo.entity;
import lombok.*;
import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Entity
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class Code {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    private String code;

    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar expiryDate;

    @OneToOne
    @Setter
    private User user;

    public Code(User user) {
        Random random = new Random();
        Calendar calendar = Calendar.getInstance();
        int length;

        this.code = String.valueOf(Math.abs((int) (random.nextDouble()*10000)));
        length = code.length();
        for (int i=0; i<4 - length; i++) {
            code = "0" + code;
        }
        calendar.add(Calendar.MINUTE,15);
        this.expiryDate = calendar;
        this.user = user;
    }
}
