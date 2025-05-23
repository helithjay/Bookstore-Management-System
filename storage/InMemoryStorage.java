/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.storage;

import com.bookstore.model.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorage {
    private final Map<String, Book> books = new ConcurrentHashMap<>();
    private final Map<String, Author> authors = new ConcurrentHashMap<>();
    private final Map<String, Customer> customers = new ConcurrentHashMap<>();
    private final Map<String, Cart> carts = new ConcurrentHashMap<>();
    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    private int idCounter = 1;

    private static final InMemoryStorage INSTANCE = new InMemoryStorage();

    private InMemoryStorage() {}

    public static InMemoryStorage getInstance() {
        return INSTANCE;
    }

    public synchronized String generateId() {
        return String.valueOf(idCounter++);
    }

    // Book methods
    public void addBook(Book book) { books.put(book.getId(), book); }
    public Book getBook(String id) { return books.get(id); }
    public List<Book> getAllBooks() { return new ArrayList<>(books.values()); }
    public void updateBook(Book book) { books.put(book.getId(), book); }
    public void deleteBook(String id) { books.remove(id); }
    public List<Book> getBooksByAuthor(String authorId) {
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()) {
            if (book.getAuthorId().equals(authorId)) {
                result.add(book);
            }
        }
        return result;
    }

    // Author methods
    public void addAuthor(Author author) { authors.put(author.getId(), author); }
    public Author getAuthor(String id) { return authors.get(id); }
    public List<Author> getAllAuthors() { return new ArrayList<>(authors.values()); }
    public void updateAuthor(Author author) { authors.put(author.getId(), author); }
    public void deleteAuthor(String id) { authors.remove(id); }

    // Customer methods
    public void addCustomer(Customer customer) { customers.put(customer.getId(), customer); }
    public Customer getCustomer(String id) { return customers.get(id); }
    public List<Customer> getAllCustomers() { return new ArrayList<>(customers.values()); }
    public void updateCustomer(Customer customer) { customers.put(customer.getId(), customer); }
    public void deleteCustomer(String id) { customers.remove(id); }

    // Cart methods
    public void addCart(Cart cart) { carts.put(cart.getCustomerId(), cart); }
    public Cart getCart(String customerId) { return carts.get(customerId); }
    public void deleteCart(String customerId) { carts.remove(customerId); }

    // Order methods
    public void addOrder(Order order) { orders.put(order.getId(), order); }
    public Order getOrder(String id) { return orders.get(id); }
    public List<Order> getOrdersByCustomer(String customerId) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getCustomerId().equals(customerId)) {
                result.add(order);
            }
        }
        return result;
    }
}
