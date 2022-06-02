package com.revature.project1.model;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
@Table(name="cart",schema="project1")
public class Cart {
    @Id
    @Column(name="CART_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cartNum;
    private int numItemsInCart;
    private int userId;

}
