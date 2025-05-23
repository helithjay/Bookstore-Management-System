/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.resource;

import com.bookstore.exceptions.*;
import com.bookstore.model.Author;
import com.bookstore.model.Book;
import com.bookstore.storage.InMemoryStorage;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.UUID;

@Path("authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {
    private final InMemoryStorage storage = InMemoryStorage.getInstance();

    @POST
    public Response createAuthor(Author author) {
        if (author.getFirstName() == null || author.getLastName() == null) {
            throw new InvalidInputException("First name and last name are required.");
        }
        author.setId(UUID.randomUUID().toString());
        storage.addAuthor(author);
        return Response.status(Response.Status.CREATED).entity(author).build();
    }

    @GET
    public List<Author> getAllAuthors() {
        return storage.getAllAuthors();
    }

    @GET
    @Path("{id}")
    public Author getAuthor(@PathParam("id") String id) {
        Author author = storage.getAuthor(id);
        if (author == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        return author;
    }

    @PUT
    @Path("{id}")
    public Author updateAuthor(@PathParam("id") String id, Author author) {
        if (storage.getAuthor(id) == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        if (author.getFirstName() == null || author.getLastName() == null) {
            throw new InvalidInputException("First name and last name are required.");
        }
        author.setId(id);
        storage.updateAuthor(author);
        return author;
    }

    @DELETE
    @Path("{id}")
    public Response deleteAuthor(@PathParam("id") String id) {
        if (storage.getAuthor(id) == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        storage.deleteAuthor(id);
        return Response.noContent().build();
    }

    @GET
    @Path("{id}/books")
    public List<Book> getBooksByAuthor(@PathParam("id") String id) {
        if (storage.getAuthor(id) == null) {
            throw new AuthorNotFoundException("Author with ID " + id + " does not exist.");
        }
        return storage.getBooksByAuthor(id);
    }
}
