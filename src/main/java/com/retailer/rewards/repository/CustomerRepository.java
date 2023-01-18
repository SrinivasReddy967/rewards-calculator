package com.retailer.rewards.repository;

import com.retailer.rewards.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    public Customer findByCustomerId(Long customerId);

}
