package com.revature.project1.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
@Table(name="user",schema="project1")
public class User {
    @Id
    @Column(name="USER_ID")
    private int userId;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String password;

    private String firstName;

    private String lastName;

    private String cardNum;

    @NotNull
    private Role role;

    @ElementCollection
    @CollectionTable(name="CartContents", joinColumns = @JoinColumn(name="user_id"))
    private List<Item> cartContents = new ArrayList<Item>();

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", cardNum='" + cardNum + '\'' +
                ", role=" + role +
                '}';
    }
}
