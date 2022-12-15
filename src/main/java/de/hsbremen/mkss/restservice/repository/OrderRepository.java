package de.hsbremen.mkss.restservice.repository;

import de.hsbremen.mkss.restservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * * Repository class for <code>Order</code>.
 * The JPaRepository interfaces provide sophisticated CRUD functionality for the entity class that is being managed.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
