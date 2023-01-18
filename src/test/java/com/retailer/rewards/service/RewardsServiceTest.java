package com.retailer.rewards.service;

import com.retailer.rewards.model.Rewards;
import com.retailer.rewards.model.Transaction;
import com.retailer.rewards.repository.CustomerRepository;
import com.retailer.rewards.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RewardsServiceTest {

    @InjectMocks
    private RewardsService rewardsService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;


    @Test
    public void testCalculateRewards_1() {
        //Given
        Transaction transaction = getTransaction(10001L, 1L,
                Timestamp.valueOf("2022-12-12 09:01:16"), 185);

        //when
        Long rewardsResult = rewardsService.calculateRewards(transaction);

        //Then
        assertEquals(220L, rewardsResult);

    }

    @Test
    public void testCalculateRewards_2() {
        //Given
        Transaction transaction = getTransaction(10002L, 2L,
                Timestamp.valueOf("2022-12-04 09:02:18"), 115);

        //when
        Long rewardsResult = rewardsService.calculateRewards(transaction);

        //Then
        assertEquals(80L, rewardsResult);

    }

    @Test
    public void testCalculateRewards_3() {
        //Given
        Transaction transaction = getTransaction(10003L, 3L,
                Timestamp.valueOf("2022-12-04 09:02:18"), 1);

        //when
        Long rewardsResult = rewardsService.calculateRewards(transaction);

        //Then
        assertEquals(0L, rewardsResult);

    }

    @Test
    public void testCalculateRewards_4() {
        //Given
        Transaction transaction = getTransaction(10004L, 4L,
                Timestamp.valueOf("2022-11-04 09:02:18"), 1133.30);

        //when
        Long rewardsResult = rewardsService.calculateRewards(transaction);

        //Then
        assertEquals(2116L, rewardsResult);

    }

    @Test
    public void testGetRewardsPerMonth() {
        //Given
        Transaction transaction1 = getTransaction(10007L, 2L,
                Timestamp.valueOf("2022-12-05 09:02:18"), 181);

        Transaction transaction2 = getTransaction(10008L, 2L,
                Timestamp.valueOf("2022-11-27 09:02:18"), 3);

        Transaction transaction3 = getTransaction(10009L, 2L,
                Timestamp.valueOf("2022-11-24 09:02:18"), 120);

        Transaction transaction4 = getTransaction(10010L, 2L,
                Timestamp.valueOf("2022-11-26 09:02:18"), 558);

        Transaction transaction5 = getTransaction(10011L, 2L,
                Timestamp.valueOf("2023-01-15 09:02:18"), 797);

        Transaction transaction6 = getTransaction(10012L, 2L,
                Timestamp.valueOf("2022-12-15 09:02:18"), 469);

        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction1);
        transactionList.add(transaction2);
        transactionList.add(transaction3);
        transactionList.add(transaction4);
        transactionList.add(transaction5);
        transactionList.add(transaction6);


        //when
        Long rewardsResult = rewardsService.getRewardsPerMonth(transactionList);

        //Then
        assertEquals(3500L, rewardsResult);

    }

    @Test
    public void getRewardsByCustomerId() {
        //Given
        long customerId = 2L;
        Transaction transaction1 = getTransaction(10011L, 2L,
                Timestamp.valueOf("2023-01-15 09:02:18"), 797);
        List<Transaction> transactionList1 = new ArrayList<>();
        transactionList1.add(transaction1);

        Transaction transaction2 = getTransaction(10008L, 2L,
                Timestamp.valueOf("2022-11-27 09:02:18"), 3);

        Transaction transaction3 = getTransaction(10009L, 2L,
                Timestamp.valueOf("2022-11-24 09:02:18"), 120);

        Transaction transaction4 = getTransaction(10010L, 2L,
                Timestamp.valueOf("2022-11-26 09:02:18"), 558);

        Transaction transaction5 = getTransaction(10012L, 2L,
                Timestamp.valueOf("2022-12-15 09:02:18"), 469);

        Transaction transaction6 = getTransaction(10007L, 2L,
                Timestamp.valueOf("2022-12-05 09:02:18"), 181);

        List<Transaction> transactionList2 = new ArrayList<>();
        transactionList2.add(transaction2);
        transactionList2.add(transaction3);
        transactionList2.add(transaction4);
        transactionList2.add(transaction5);
        transactionList2.add(transaction6);

        //when
        when(transactionRepository.findAllByCustomerIdAndTransactionDateBetween(anyLong(),
                any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(transactionList1)
                .thenReturn(transactionList2)
                .thenReturn(new ArrayList<>());

        Rewards rewards = rewardsService.getRewardsByCustomerId(customerId);

        //Then
        assertEquals(3500L, rewards.getTotalRewards());

    }

    private static Transaction getTransaction(Long transId, Long customerId, Timestamp timestamp, double amount) {
        return Transaction.builder()
                .transactionId(transId)
                .customerId(customerId)
                .transactionDate(timestamp)
                .transactionAmount(amount)
                .build();
    }
}
