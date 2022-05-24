package com.revature.project1.model;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
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

    @NotNull
    @Column(unique = true)
    private String email;

    private String firstName;

    private String lastName;

    private String cardNum;

    //@OneToMany(fetch= FetchType.LAZY, mappedBy = "user",cascade = CascadeType.ALL)
    @ElementCollection
    @CollectionTable(name="in_cart", joinColumns = @JoinColumn(name="user_id"))
    private List<Item> cartContents;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="user", cascade = CascadeType.ALL)
    @ElementCollection
    @CollectionTable(name="in_cart", joinColumns = @JoinColumn(name="user_id"))
    private List<Order> orders;
}
