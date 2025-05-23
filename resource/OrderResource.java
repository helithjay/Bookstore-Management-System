/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.resource;

import com.bookstore.exceptions.*;
import com.bookstore.model.Book;
import com.bookstore.model.Cart;
import com.bookstore.model.CartItem;
import com.bookstore.model.Order;
import com.bookstore.storage.InMemoryStorage;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;

@Path("customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private final InMemoryStorage storage = InMemoryStorage.getInstance();

    @POST
    public Response createOrder(@PathParam("customerId") String customerId) {
        if (storage.getCustomer(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        Cart cart = storage.getCart(customerId);
        if (cart == null || cart.getItems().isEmpty()) {
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " is empty or does not exist.");
        }
        double totalPrice = 0;
        for (CartItem item : cart.getItems()) {
            Book book = storage.getBook(item.getBookId());
            if (book == null) {
                throw new BookNotFoundException("Book with ID " + item.getBookId() + " does not exist.");
            }
            if (book.getStock() < item.getQuantity()) {
                throw new OutOfStockException("Insufficient stock for book with ID " + item.getBookId());
            }
            totalPrice += book.getPrice() * item.getQuantity();
        }
        for (CartItem item : cart.getItems()) {
            Book book = storage.getBook(item.getBookId());
            book.setStock(book.getStock() - item.getQuantity());
            storage.updateBook(book);
        }
        Order order = new Order(UUID.randomUUID().toString(), customerId, cart.getItems(), totalPrice);
        storage.addOrder(order);
        storage.deleteCart(customerId); // Clear cart after order
        return Response.status(Response.Status.CREATED).entity(order).build();
    }

    @GET
    public Response getOrders(@PathParam("customerId") String customerId) {
        if (storage.getCustomer(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        return Response.ok(storage.getOrdersByCustomer(customerId)).build();
    }

    @GET
    @Path("{orderId}")
    public Response getOrder(@PathParam("customerId") String customerId,
                            @PathParam("orderId") String orderId) {
        if (storage.getCustomer(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        Order order = storage.getOrder(orderId);
        if (order == null || !order.getCustomerId().equals(customerId)) {
            throw new InvalidInputException("Order with ID " + orderId + " does not exist for customer.");
        }
        return Response.ok(order).build();
    }
}
