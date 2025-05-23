/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.resource;

import com.bookstore.exceptions.*;
import com.bookstore.model.Book;
import com.bookstore.model.Cart;
import com.bookstore.model.CartItem;
import com.bookstore.storage.InMemoryStorage;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    private final InMemoryStorage storage = InMemoryStorage.getInstance();

    @POST
    @Path("items")
    public Response addItemToCart(@PathParam("customerId") String customerId, CartItem item) {
        if (storage.getCustomer(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        Book book = storage.getBook(item.getBookId());
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + item.getBookId() + " does not exist.");
        }
        if (item.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be positive.");
        }
        if (book.getStock() < item.getQuantity()) {
            throw new OutOfStockException("Insufficient stock for book with ID " + item.getBookId());
        }
        Cart cart = storage.getCart(customerId);
        if (cart == null) {
            cart = new Cart(customerId);
            storage.addCart(cart);
        }
        cart.getItems().add(item);
        book.setStock(book.getStock() - item.getQuantity());
        storage.updateBook(book);
        return Response.status(Response.Status.CREATED).entity(cart).build();
    }

    @GET
    public Cart getCart(@PathParam("customerId") String customerId) {
        if (storage.getCustomer(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        Cart cart = storage.getCart(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist.");
        }
        return cart;
    }

    @PUT
    @Path("items/{bookId}")
    public Response updateCartItem(@PathParam("customerId") String customerId,
                                  @PathParam("bookId") String bookId, CartItem updatedItem) {
        if (storage.getCustomer(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        Cart cart = storage.getCart(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist.");
        }
        Book book = storage.getBook(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " does not exist.");
        }
        if (updatedItem.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be positive.");
        }
        CartItem existingItem = null;
        for (CartItem item : cart.getItems()) {
            if (item.getBookId().equals(bookId)) {
                existingItem = item;
                break;
            }
        }
        if (existingItem == null) {
            throw new InvalidInputException("Book with ID " + bookId + " is not in the cart.");
        }
        int stockDifference = updatedItem.getQuantity() - existingItem.getQuantity();
        if (stockDifference > book.getStock()) {
            throw new OutOfStockException("Insufficient stock for book with ID " + bookId);
        }
        existingItem.setQuantity(updatedItem.getQuantity());
        book.setStock(book.getStock() - stockDifference);
        storage.updateBook(book);
        return Response.ok(cart).build();
    }

    @DELETE
    @Path("items/{bookId}")
    public Response removeItemFromCart(@PathParam("customerId") String customerId,
                                       @PathParam("bookId") String bookId) {
        if (storage.getCustomer(customerId) == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " does not exist.");
        }
        Cart cart = storage.getCart(customerId);
        if (cart == null) {
            throw new CartNotFoundException("Cart for customer with ID " + customerId + " does not exist.");
        }
        Book book = storage.getBook(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " does not exist.");
        }
        CartItem itemToRemove = null;
        for (CartItem item : cart.getItems()) {
            if (item.getBookId().equals(bookId)) {
                itemToRemove = item;
                break;
            }
        }
        if (itemToRemove == null) {
            throw new InvalidInputException("Book with ID " + bookId + " is not in the cart.");
        }
        cart.getItems().remove(itemToRemove);
        book.setStock(book.getStock() + itemToRemove.getQuantity());
        storage.updateBook(book);
        return Response.noContent().build();
    }
}
