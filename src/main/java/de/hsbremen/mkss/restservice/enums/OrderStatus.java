package de.hsbremen.mkss.restservice.enums;
/**
 * The enum class for the status of an Order
 *
 * - EMPTY (initial status: no line items / “empty shopping cart”)
 * - IN_PREPARATION (at least one line item, but not purchased)
 * - COMMITTED (purchased order; order not processed by warehouse)
 * - ACCEPTED (order has been processed and accepted by warehouse)
 * - REJECTED (order has been processed and rejected by warehouse, e.g. due to insufficient
 * stock)
 */
public enum OrderStatus {




    EMPTY, IN_PREPARATION, COMMITTED, ACCEPTED, REJECTED;
}
