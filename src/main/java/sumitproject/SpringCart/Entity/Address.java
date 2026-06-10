package sumitproject.SpringCart.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String street;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 20)
    private String pincode;

    @Column(length = 100)
    private String country;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDefault = false;

    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "address", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Order> orderList = new HashSet<>();

    public void addOrder(Order order) {
        orderList.add(order);
        order.setAddress(this);
    }

    public void removeOrder(Order order) {
        orderList.remove(order);
        order.setAddress(null);
    }
}