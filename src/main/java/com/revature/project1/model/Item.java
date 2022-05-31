package com.revature.project1.model;

import com.sun.istack.NotNull;
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
@Embeddable
@Table(name="item",schema="project1")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;

    @NotNull
    private String itemName;

    @NotNull
    private int qoh;

    @NotNull
    private int price;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user = null;
}
