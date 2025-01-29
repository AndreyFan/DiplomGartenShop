package de.telran.gartenshop.entity;

import de.telran.gartenshop.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @Column(name="UserID")
    private Long userId;

    @Column(name="Name")
    private String name;

    @Column(name ="Email")
    private String email;

    @Column(name="PhoneNumber")
    private String phone;

    @Column(name = "PasswordHash")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role")
    private Role role;


}