package com.retailer.rewards.service;

import com.retailer.rewards.constants.Constants;
import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.Rewards;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RewardsService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Rewards getRewardsByCustomerId(Long id) {

        Timestamp lastMonthTimestamp = getDateBasedOnOffSetDays(Constants.daysInMonths);
        Timestamp lastSecondMonthTimestamp = getDateBasedOnOffSetDays(2 * Constants.daysInMonths);
        Timestamp lastThirdMonthTimestamp = getDateBasedOnOffSetDays(3 * Constants.daysInMonths);
        System.out.println(" lastMonthTimestamp : " + lastMonthTimestamp + "lastSecondMonthTimestamp : " + lastSecondMonthTimestamp + "lastThirdMonthTimestamp : " +lastThirdMonthTimestamp );

        List<Transaction> lastMonthTransactions = transactionRepository.findAllByCustomerIdAndTransactionDateBetween(
                id, lastMonthTimestamp, Timestamp.from(Instant.now()));
        System.out.println("lastMonthTransactions ==========> " + lastMonthTransactions);

        List<Transaction> lastSecondMonthTransactions = transactionRepository
                .findAllByCustomerIdAndTransactionDateBetween(id, lastSecondMonthTimestamp, lastMonthTimestamp);
        System.out.println("lastSecondMonthTransactions ==========> " + lastSecondMonthTransactions);

        List<Transaction> lastThirdMonthTransactions = transactionRepository
                .findAllByCustomerIdAndTransactionDateBetween(id, lastThirdMonthTimestamp,
                        lastSecondMonthTimestamp);
        System.out.println("lastThirdMonthTransactions ==========> " + lastThirdMonthTransactions);

        Long lastMonthRewardPoints = getRewardsPerMonth(lastMonthTransactions);
        Long lastSecondMonthRewardPoints = getRewardsPerMonth(lastSecondMonthTransactions);
        Long lastThirdMonthRewardPoints = getRewardsPerMonth(lastThirdMonthTransactions);

        return Rewards.builder()
                .customerId(id)
                .lastMonthRewardPoints(lastMonthRewardPoints)
                .lastSecondMonthRewardPoints(lastSecondMonthRewardPoints)
                .lastThirdMonthRewardPoints(lastThirdMonthRewardPoints)
                .totalRewards(lastMonthRewardPoints + lastSecondMonthRewardPoints + lastThirdMonthRewardPoints)
                .build();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findByCustomerId(id);
    }

    protected Long getRewardsPerMonth(List<Transaction> transactions) {
        return transactions.stream().map(this::calculateRewards).mapToLong(r -> r).sum();
    }

    protected Long calculateRewards(Transaction t) {
        if (t.getTransactionAmount() > Constants.firstRewardLimit && t.getTransactionAmount() <= Constants.secondRewardLimit) {
            return Math.round(t.getTransactionAmount() - Constants.firstRewardLimit);
        } else if (t.getTransactionAmount() > Constants.secondRewardLimit) {
            return Math.round(t.getTransactionAmount() - Constants.secondRewardLimit) * 2
                    + (Constants.secondRewardLimit - Constants.firstRewardLimit);
        } else
            return 0L;

    }

    public Timestamp getDateBasedOnOffSetDays(int days) {
        return Timestamp.valueOf(LocalDateTime.now().minusDays(days));
    }

}
