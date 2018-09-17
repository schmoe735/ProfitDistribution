package com.acme.service;

import com.acme.model.PostOffice;

import java.math.BigDecimal;
import java.util.Map;

public interface ProfitDistributionService {
    /**
     * Calculates the profit based on transaction amount among subsidiaries after distributing parent commission
     * @param postOffice profit distribution for give post office
     * @return map containing post office names and its profit percentage
     */
    Map<String, BigDecimal> distributeProfit(PostOffice postOffice);
}
