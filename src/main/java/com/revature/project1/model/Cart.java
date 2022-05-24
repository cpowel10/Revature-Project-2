package com.revature.project1.model;

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
@Table(name="cart",schema="project1")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cartNum;
    private int numberOfItems;
    private int userId;
}
