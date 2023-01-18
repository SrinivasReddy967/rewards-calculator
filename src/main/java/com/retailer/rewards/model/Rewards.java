package com.retailer.rewards.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Rewards {
    private long customerId;
    private long lastMonthRewardPoints;
    private long lastSecondMonthRewardPoints;
    private long lastThirdMonthRewardPoints;
    private long totalRewards;
}
