package com.revature.project1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
@Entity
@Table(name="order",schema="project1")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderNumber;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;
}
