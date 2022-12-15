package de.hsbremen.mkss.restservice.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import de.hsbremen.mkss.restservice.enums.OrderStatus;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


/**
 * This is the class definition of an order. It contains a Set including all LineItems
 **/

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order extends IdBasedEntity {

    @Nonnull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    private LocalDate date;


    @Column(name = "customer_name", length = 45)
    @Nonnull
    @NotBlank
    private String customerName;


    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order(LocalDate date, String customerName) {
        this.date = date;
        this.customerName = customerName;
    }

    public Order(LocalDate date, String customerName, OrderStatus status) {
        this.date = date;
        this.customerName = customerName;
        this.status = status;
    }

    @Override
    public String toString() {
        return getCustomerName();
    }


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,fetch=FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private Set<LineItem> items = new HashSet<>();


    public void addItem(LineItem item) {
        item.setOrder(this);
        items.add(item);
    }

    public void removeItem(LineItem item) {
        items.remove(item);
    }
}
