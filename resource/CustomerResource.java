/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.resource;

import com.bookstore.exceptions.*;
import com.bookstore.model.Customer;
import com.bookstore.storage.InMemoryStorage;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.UUID;

@Path("customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    private final InMemoryStorage storage = InMemoryStorage.getInstance();

    @POST
    public Response createCustomer(Customer customer) {
        if (customer.getFirstName() == null || customer.getLastName() == null ||
            customer.getEmail() == null || customer.getPassword() == null) {
            throw new InvalidInputException("All fields are required.");
        }
        customer.setId(UUID.randomUUID().toString());
        storage.addCustomer(customer);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @GET
    public List<Customer> getAllCustomers() {
        return storage.getAllCustomers();
    }

    @GET
    @Path("{id}")
    public Customer getCustomer(@PathParam("id") String id) {
        Customer customer = storage.getCustomer(id);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        return customer;
    }

    @PUT
    @Path("{id}")
    public Customer updateCustomer(@PathParam("id") String id, Customer customer) {
        if (storage.getCustomer(id) == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        if (customer.getFirstName() == null || customer.getLastName() == null ||
            customer.getEmail() == null || customer.getPassword() == null) {
            throw new InvalidInputException("All fields are required.");
        }
        customer.setId(id);
        storage.updateCustomer(customer);
        return customer;
    }

    @DELETE
    @Path("{id}")
    public Response deleteCustomer(@PathParam("id") String id) {
        if (storage.getCustomer(id) == null) {
            throw new CustomerNotFoundException("Customer with ID " + id + " does not exist.");
        }
        storage.deleteCustomer(id);
        return Response.noContent().build();
    }
}
