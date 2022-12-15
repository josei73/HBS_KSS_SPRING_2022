package de.hsbremen.mkss.restservice.service;


import de.hsbremen.mkss.restservice.entity.LineItem;
import de.hsbremen.mkss.restservice.entity.Order;
import de.hsbremen.mkss.restservice.enums.OrderStatus;
import de.hsbremen.mkss.restservice.exception.ItemNotFound;
import de.hsbremen.mkss.restservice.exception.OrderNotFoundException;
import de.hsbremen.mkss.restservice.exception.OrderStatusException;
import de.hsbremen.mkss.restservice.repository.LineItemRepository;
import de.hsbremen.mkss.restservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Class defining the order service.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LineItemRepository itemRepository;

    // Get all orders
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    // Get an Order
    public Order get(Integer orderId) throws OrderNotFoundException {
        Optional<Order> orderById = orderRepository.findById(orderId);
        if (!orderById.isPresent())
            throw new OrderNotFoundException("No Order with Id " + orderId);
        return orderById.get();

    }

    // Delete a order
    public Order delete(Integer orderId) throws OrderNotFoundException {
        Order order = get(orderId);

        if (order == null || order.getId() == 0)
            throw new OrderNotFoundException("Could not find any Order with ID " + orderId);
        orderRepository.deleteById(orderId);
        return order;
    }

    //Creates a new order
    public Order save(Order order) {
        order.setDate(LocalDate.now());
        order.setStatus(OrderStatus.EMPTY);
        return orderRepository.save(order);

    }

    //Add a new Item
    public LineItem addItem(LineItem lineItem, int id) throws OrderNotFoundException {

        Order order = get(id);
        if (IsOrderCommitted(order))
            throw new OrderNotFoundException("Order is already Committed");
        order.addItem(lineItem);
        if (order.getItems().size() == 1)
            order.setStatus(OrderStatus.IN_PREPARATION);

        return itemRepository.save(lineItem);
    }

    // Delete a Item
    public LineItem removeItem(Integer lineItemId, int id) throws OrderNotFoundException, ItemNotFound, OrderStatusException {


        Order order = get(id);
        if (IsOrderCommitted(order))
            throw new OrderStatusException("Order is already Committed");

        LineItem item = getItem(lineItemId);

        order.removeItem(item);
        if (order.getItems().size() == 0)
            order.setStatus(OrderStatus.EMPTY);
        orderRepository.save(order);

        return item;
    }

    // Check if order status is committed
    private boolean IsOrderCommitted(Order order) {

        return order.getStatus().equals(OrderStatus.COMMITTED);
    }

    // Get a Item
    private LineItem getItem(int id) throws ItemNotFound {

        Optional<LineItem> itemByID = itemRepository.findById(id);

        if (itemByID.isPresent())
            return itemByID.get();

        throw new ItemNotFound("Could not find Item with the Id " + id);
    }

    // Get all Items from an Order
    public Set<LineItem> getItems(int orderId) throws OrderNotFoundException {
        Order order = get(orderId);
        return order.getItems();


    }


    // Purchase an Order
    public Order checkout(int id) throws OrderStatusException, OrderNotFoundException {
        Order order = get(id);
        if (order.getStatus().equals(OrderStatus.IN_PREPARATION)) {
            order.setStatus(OrderStatus.COMMITTED);
            return orderRepository.save(order);
        }
        throw new OrderStatusException("Order should be IN_PREPARATION not in "+order.getStatus().toString());
    }


}
