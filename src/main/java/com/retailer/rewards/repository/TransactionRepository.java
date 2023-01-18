package com.retailer.rewards.repository;

import com.retailer.rewards.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    public List<Transaction> findAllByCustomerId(Long customerId);
    public List<Transaction> findAllByCustomerIdAndTransactionDateBetween(Long customerId, Timestamp from, Timestamp to);
}
