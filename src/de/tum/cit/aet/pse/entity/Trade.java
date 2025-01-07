package de.tum.cit.aet.pse.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Customer trader;

    @ManyToOne
    private Customer recipient;

    @ManyToOne(fetch = FetchType.EAGER)
    private Item item;

    private int quantity;
    private double price;
    private LocalDateTime timestamp;

    private String status; // "PENDING", "ACCEPTED", "DECLINED"

}
