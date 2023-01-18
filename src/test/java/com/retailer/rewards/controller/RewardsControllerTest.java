package com.retailer.rewards.controller;

import com.retailer.rewards.exception.ResourceNotFoundException;
import com.retailer.rewards.model.Customer;
import com.retailer.rewards.model.Rewards;
import com.retailer.rewards.service.RewardsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
public class RewardsControllerTest {

    @InjectMocks
    private RewardsController rewardsController;

    @Mock
    private RewardsService rewardsService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(rewardsController).build();

    }

    @Test
    public void givenInvalidCustomerId_whenGetRewardsByCustomerId_thenReturnNotFound() throws Exception {
        //given
        long customerId = 11111L;

        given(rewardsService.getCustomerById(customerId)).willReturn(null);

        //when
        ResultActions result = mockMvc.perform(get("/customers/{id}", customerId));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(res -> assertTrue(res.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(res -> assertEquals("Customer data not found for id : 11111",
                        Objects.requireNonNull(res.getResolvedException()).getMessage()));
    }

    @Test
    public void givenValidCustomerId_whenGetRewardsByCustomerId_thenReturnNotFound() throws Exception {
        //given
        long id = 1L;

        Customer customer = Customer.builder().customerName("test1").customerId(id).build();

        Rewards rewards = Rewards.builder().customerId(id).totalRewards(3500L)
                .lastMonthRewardPoints(1444L)
                .lastSecondMonthRewardPoints(20561L)
                .lastThirdMonthRewardPoints(0L)
                .build();

        given(rewardsService.getCustomerById(id)).willReturn(customer);
        given(rewardsService.getRewardsByCustomerId(id)).willReturn(rewards);


        //when
        ResultActions result = mockMvc.perform(get("/customers/{id}", id));

        //then
        result.andExpectAll(status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.totalRewards").value("3500"),
                jsonPath("$.customerId").value("1"));
    }
}
