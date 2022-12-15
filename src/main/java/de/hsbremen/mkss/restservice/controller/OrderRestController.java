package de.hsbremen.mkss.restservice.controller;


import de.hsbremen.mkss.restservice.entity.LineItem;
import de.hsbremen.mkss.restservice.entity.Order;
import de.hsbremen.mkss.restservice.exception.ItemNotFound;
import de.hsbremen.mkss.restservice.exception.OrderNotFoundException;
import de.hsbremen.mkss.restservice.exception.OrderStatusException;
import de.hsbremen.mkss.restservice.service.OrderService;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
public class OrderRestController {


    @Autowired
    private OrderService orderService;


    @GetMapping(value = "/orders/{id}")
    public Order getOrder(@PathVariable Integer id) throws OrderNotFoundException {

        return orderService.get(id);

    }

    @GetMapping("orders")
    public List<Order> getAllOrders() {

        return orderService.findAll();
    }

    @GetMapping(value = "/orders/{id}/items")
    public Set<LineItem> getAllOrderItems(@PathVariable Integer id) throws OrderNotFoundException {
        return orderService.getItems(id);

    }

    @PostMapping(value = "orders")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public ChangeResultView addOrder(@Nonnull @Valid @RequestBody Order order, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ChangeResultView.from(bindingResult);
        }
        return ChangeResultView.success(orderService.save(order));

    }

    @PostMapping(value = "/orders/{id}/items")
    public ChangeResultView addItemToOrder(@PathVariable Integer id, @Valid @Nonnull @RequestBody LineItem lineItem, BindingResult bindingResult) throws OrderNotFoundException {

        if (bindingResult.hasErrors()) {
            return ChangeResultView.from(bindingResult);
        }


        return ChangeResultView.update(orderService.addItem(lineItem, id));


    }

    @PutMapping(value = "/orders/{id}/checkout")
    public ChangeResultView purchaseOrder(@PathVariable Integer id) throws OrderNotFoundException, OrderStatusException {

        return ChangeResultView.update(orderService.checkout(id));


    }

    @DeleteMapping(value = "/orders/{oid}/items/{lid}")
    public ChangeResultView removeItemFromOrder(@PathVariable Integer oid, @PathVariable Integer lid) throws OrderNotFoundException, ItemNotFound, OrderStatusException {


        return ChangeResultView.delete(orderService.removeItem(lid, oid));


    }

    @DeleteMapping("/orders/{id}")
    public ChangeResultView deleteOrder(@PathVariable int id) throws OrderNotFoundException {
        return ChangeResultView.delete(orderService.delete(id));

    }


}
