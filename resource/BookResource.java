/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.resource;

import com.bookstore.exceptions.*;
import com.bookstore.model.Book;
import com.bookstore.storage.InMemoryStorage;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Path("books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private final InMemoryStorage storage = InMemoryStorage.getInstance();

    @POST
    public Response createBook(Book book) {
        if (book.getTitle() == null || book.getAuthorId() == null || book.getIsbn() == null ||
            book.getPublicationYear() <= 0 || book.getPrice() <= 0 || book.getStock() < 0) {
            throw new InvalidInputException("All fields must be provided and valid.");
        }
        if (book.getPublicationYear() > LocalDate.now().getYear()) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }
        if (storage.getAuthor(book.getAuthorId()) == null) {
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " does not exist.");
        }
        book.setId(UUID.randomUUID().toString());
        storage.addBook(book);
        return Response.status(Response.Status.CREATED).entity(book).build();
    }

    @GET
    public List<Book> getAllBooks() {
        return storage.getAllBooks();
    }

    @GET
    @Path("{id}")
    public Book getBook(@PathParam("id") String id) {
        Book book = storage.getBook(id);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        return book;
    }

    @PUT
    @Path("{id}")
    public Book updateBook(@PathParam("id") String id, Book book) {
        if (storage.getBook(id) == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        if (book.getTitle() == null || book.getAuthorId() == null || book.getIsbn() == null ||
            book.getPublicationYear() <= 0 || book.getPrice() <= 0 || book.getStock() < 0) {
            throw new InvalidInputException("All fields must be provided and valid.");
        }
        if (book.getPublicationYear() > LocalDate.now().getYear()) {
            throw new InvalidInputException("Publication year cannot be in the future.");
        }
        if (storage.getAuthor(book.getAuthorId()) == null) {
            throw new AuthorNotFoundException("Author with ID " + book.getAuthorId() + " does not exist.");
        }
        book.setId(id);
        storage.updateBook(book);
        return book;
    }

    @DELETE
    @Path("{id}")
    public Response deleteBook(@PathParam("id") String id) {
        if (storage.getBook(id) == null) {
            throw new BookNotFoundException("Book with ID " + id + " does not exist.");
        }
        storage.deleteBook(id);
        return Response.noContent().build();
    }
}