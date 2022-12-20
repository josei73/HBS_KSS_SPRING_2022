package de.hsbremen.mkss.restservice;

import de.hsbremen.mkss.restservice.entity.LineItem;
import de.hsbremen.mkss.restservice.entity.Order;
import de.hsbremen.mkss.restservice.enums.OrderStatus;
import de.hsbremen.mkss.restservice.repository.LineItemRepository;
import de.hsbremen.mkss.restservice.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * Spring Boot will run ALL CommandLineRunner beans once the application context is loaded.
 *
 * This runner will request a copy of the OrderRepository and LineItemRepository.
 *
 * Using it, it will create orders and items entities and store them.
 */

@Configuration
class LoadDatabase {



    @Bean
    public CommandLineRunner initDatabase(OrderRepository orderRepository, LineItemRepository itemRepository) throws Exception {
        return (String[] args) -> {


            Order order1 = new Order(LocalDate.now(), "Tom", OrderStatus.IN_PREPARATION);

            Order order2 = new Order(LocalDate.now(), "Laura", OrderStatus.IN_PREPARATION);
            Order order3 = new Order(LocalDate.now(), "Max",OrderStatus.IN_PREPARATION);
            Order order4 = new Order(LocalDate.now(), "Tim",OrderStatus.ACCEPTED);
            Order order5 = new Order(LocalDate.now(), "Paul",OrderStatus.COMMITTED);

            orderRepository.saveAll(Arrays.asList(order1, order2, order3, order4, order5));

            LineItem lineItem1 = new LineItem("Books", 4.99f, 5, order1);
            LineItem lineItem2 = new LineItem("Notebooks", 399.99f, 2, order1);
            LineItem lineItem3 = new LineItem("T-Shirt", 25f, 7, order2);
            LineItem lineItem4 = new LineItem("Cap", 10.99f, 5, order2);
            LineItem lineItem5 = new LineItem("Phone", 499.99f, 1, order3);
            LineItem lineItem6 = new LineItem("TV", 800f, 5, order3);
            LineItem lineItem8 = new LineItem("AirPods", 250.99f, 2, order4);
            LineItem lineItem7 = new LineItem("Socks", 2.99f, 5, order5);

            itemRepository.saveAll(Arrays.asList(lineItem1, lineItem2, lineItem3, lineItem4, lineItem5, lineItem6, lineItem7, lineItem8));
        };
    }
}