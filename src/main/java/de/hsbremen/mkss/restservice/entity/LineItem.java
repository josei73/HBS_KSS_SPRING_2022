package de.hsbremen.mkss.restservice.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 Class containing the generic definition of any Item.
 **/
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class LineItem extends IdBasedEntity {


    @Column(name = "product_name",length = 45)
    @Nonnull
    @NotBlank
    private String productName;

    @PositiveOrZero(message = "Price should not be positiv or zero")
    private float price;

    @PositiveOrZero(message = "Quantity should not be positiv or zero")
    private int quantity;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = false)
    @JsonBackReference
    private Order order;

    public LineItem(String productName, float price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }




    @Override
    public String toString() {
        return getProductName();
    }
}
