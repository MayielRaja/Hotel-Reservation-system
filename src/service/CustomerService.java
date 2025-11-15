package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {
    private final Map<String, Customer> customers = new HashMap<>();
    private static final CustomerService INSTANCE = new CustomerService();
    private CustomerService(){}
    public static CustomerService getInstance(){
        return INSTANCE;
    }
    public void addCustomer(String email,String firstName,String lastName){
        try {
            Customer customer=new Customer(firstName,lastName,email);
            customers.put(email,customer);
        }catch (IllegalArgumentException ex){
            System.out.println("Error: "+ex.getLocalizedMessage());
        }
    }
    public Customer getCustomer(String customerEmail){
        return customers.get(customerEmail);
    }
    public Collection<Customer> getAllCustomers(){
        return customers.values();
    }
}
