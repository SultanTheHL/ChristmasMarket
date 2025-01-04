package de.tum.cit.aet.pse.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long vendorId;

    @Column(nullable = false)
    private boolean settled = false;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Column(nullable = false)
    private double interest;

    @Column(nullable = false)
    private double sum;

    public Loan(Long vendorId, double sum, double interest, LocalDateTime startTime, LocalDateTime endTime) {
        this.vendorId = vendorId;
        this.sum = sum;
        this.interest = interest;
        this.startTime = startTime;
        this.endTime = endTime;
    }

}
