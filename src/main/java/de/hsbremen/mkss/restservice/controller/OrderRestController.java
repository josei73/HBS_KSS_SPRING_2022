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

/**
 * @RestController indicates that the data returned by each method will be written straight into the response body instead of rendering a template.
 *
 * We have routes for each operation (@GetMapping, @PostMapping, @PutMapping and @DeleteMapping,
 * corresponding to HTTP GET, POST, PUT, and DELETE calls).
 *
 */

@RestController
public class OrderRestController {


    @Autowired
    private OrderService orderService;

    // Takes an id parameter from the URL path and uses it to retrieve an Order object
    @GetMapping(value = "/orders/{id}")
    public Order getOrder(@PathVariable Integer id) throws OrderNotFoundException {

        return orderService.get(id);

    }
    //  Retrieves a list of all Order objects from the orderService.
    @GetMapping("orders")
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }



    // Takes an id parameter from the URL path and uses it to retrieve a set of LineItem objects for the corresponding Order
    @GetMapping(value = "/orders/{id}/items")
    public Set<LineItem> getAllOrderItems(@PathVariable Integer id) throws OrderNotFoundException {
        return orderService.getItems(id);

    }
    // Takes an Order object as a request body, which is validated using the @Valid and @Nonnull annotations.
    @PostMapping(value = "orders")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ChangeResultView addOrder(@Nonnull @Valid @RequestBody Order order, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ChangeResultView.from(bindingResult);
        }
        return ChangeResultView.success(orderService.save(order));

    }

    // This method is similar to the addOrder method, but it takes an id parameter from the URL path and a LineItem object as a request body. It uses these parameters to add the item to the specified order.
    @PostMapping(value = "/orders/{id}/items")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ChangeResultView addItemToOrder(@PathVariable Integer id, @Valid @Nonnull @RequestBody LineItem lineItem, BindingResult bindingResult) throws OrderNotFoundException, OrderStatusException {

        if (bindingResult.hasErrors()) {
            return ChangeResultView.from(bindingResult);
        }
        return ChangeResultView.update(orderService.addItem(lineItem, id));

    }

    //  Takes an id parameter from the URL path and uses it to mark the corresponding order as checked out
    @PutMapping(value = "/orders/{id}/checkout")
    public ChangeResultView purchaseOrder(@PathVariable Integer id) throws OrderNotFoundException, OrderStatusException {
        return ChangeResultView.update(orderService.checkout(id));
    }

    // Iakes oid and lid parameters from the URL path, which represent the IDs of the order and the line item to be removed, respectively
    @DeleteMapping(value = "/orders/{oid}/items/{lid}")
    public ChangeResultView removeItemFromOrder(@PathVariable Integer oid, @PathVariable Integer lid) throws OrderNotFoundException, ItemNotFound, OrderStatusException {
        return ChangeResultView.delete(orderService.removeItem(lid, oid));
    }

    // Takes an id parameter from the URL path and uses it to delete the corresponding Order
    @DeleteMapping("/orders/{id}")
    public ChangeResultView deleteOrder(@PathVariable int id) throws OrderNotFoundException {
        return ChangeResultView.delete(orderService.delete(id));
    }


}
