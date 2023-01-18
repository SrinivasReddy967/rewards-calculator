package com.retailer.rewards.controller;

import com.retailer.rewards.exception.ResourceNotFoundException;
import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.Rewards;
import com.retailer.rewards.service.RewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8092")
@RestController
public class RewardsController {

    @Autowired
    RewardsService rewardsService;

    @GetMapping(value = "/customers/{id}")
    public ResponseEntity<Rewards> getRewardsByCustomerId(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Customer customer = rewardsService.getCustomerById(id);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer data not found for id : " + id);
        }
        Rewards customerRewards = rewardsService.getRewardsByCustomerId(id);

        return new ResponseEntity<>(customerRewards, HttpStatus.OK);
    }

}
