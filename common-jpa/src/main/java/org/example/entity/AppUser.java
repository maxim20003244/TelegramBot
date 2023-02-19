package org.example.entity;

import lombok.*;
import org.example.entity.enums.UserState;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  Long telegramUserId;
    @CreationTimestamp //create  a date when object it's save in DB;
    private LocalDateTime firstLoginDate ;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private Boolean isActive;
    @Enumerated(EnumType.STRING)
private UserState state;


}
