package com.example.filmography.model;

import lombok.*;

import javax.persistence.*;
import java.text.DateFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "default_generator", sequenceName = "users_seq", allocationSize = 1)
public class User extends GenericModel{
  //  @Id
  //  @Column(name = "id")
 //   private Long id;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private DateFormat birthDate;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;


   @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "role_id",
            foreignKey = @ForeignKey(name = "FK_USER_ROLES")
    )
    private Role role;


    @Builder
    public User(Long id, LocalDateTime createdWhen, String createdBy,  String login, String password, String firstName, String lastName,
                String middleName, DateFormat birthDate, String phone, String address, String email) {
        super(id, createdWhen);

        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.address = address;
        this.email = email;

}
}
