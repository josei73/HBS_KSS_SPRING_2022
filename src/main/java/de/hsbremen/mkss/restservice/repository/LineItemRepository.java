package de.hsbremen.mkss.restservice.repository;

import de.hsbremen.mkss.restservice.entity.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * * Repository class for <code>LineItem</code>.
 * The JPaRepository interfaces provide sophisticated CRUD functionality for the entity class that is being managed.
 */
@Repository
public interface LineItemRepository extends JpaRepository<LineItem,Integer> {
}
