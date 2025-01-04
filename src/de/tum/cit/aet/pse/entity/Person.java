package de.tum.cit.aet.pse.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private double wallet = 0;
    @Column(nullable = false)
    private String passwordHash;
    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", email=" + email + "]";
    }
    public void deductFromWallet(double amount) {
        if (this.wallet >= amount) {
            this.wallet -= amount;
        } else {
            throw new RuntimeException("Insufficient funds");
        }
    }

    public void addToWallet(double amount) {
        this.wallet += amount;
    }


}
