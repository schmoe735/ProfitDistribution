package com.acme.service;

import com.acme.model.PostOffice;
import com.acme.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class ProfitDistributionServiceImpl implements ProfitDistributionService {
    @Override
    public Map<String, BigDecimal> distributeProfit(PostOffice postOffice) {
        Objects.requireNonNull(postOffice, "Post Office object cannot be null for distribution of profit");
        Map<String, BigDecimal> returnMap = new HashMap<>();

        BigDecimal totalTransactionAmt = getTotalTransactionAmount(postOffice);
        BigDecimal profit = totalTransactionAmt.multiply(BigDecimal.valueOf(2.5d)).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_EVEN);
        distribute(postOffice, profit,  returnMap);
        return returnMap;
    }

    /**
     * Calculate the total transaction amount for the post office and its subsidiaries
     * @param postOffice post office for calculation of total transactions
     * @return total transaction amount for post office and its subsidiaries
     */
    private BigDecimal getTotalTransactionAmount(PostOffice postOffice) {
        return Stream.concat(postOffice.getTransactions().stream(),collectTransactions(postOffice)).
                map(Transaction::getAmount).
                reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    /**
     * Collect Transaction streams recursively for all the subsidiaries
     * @param postOffice  collection of transaction streams for its subsidiaries
     * @return collective stream of all transactions for the subsidiries
     */
    private Stream<Transaction> collectTransactions(PostOffice postOffice){
        return postOffice.getSubsidiaries().stream().
                flatMap(p -> Stream.concat(p.getTransactions().stream(), collectTransactions(p)));
    }

    /**
     * Recursively distribute profits proportionately according to the transactions made after deducting
     * parent profit percentage
     * @param postOffice recursively calculate profit for the given post office and subsidiaries
     * @param profit Total profit available for distributing to all the subsidiaries
     * @param returnMap map containing all the profits for each post office by name
     */
    private void distribute(PostOffice postOffice, BigDecimal profit, Map<String, BigDecimal> returnMap) {
        if (!postOffice.getSubsidiaries().isEmpty()){
            BigDecimal profitForParent = profit.
                    multiply(BigDecimal.valueOf(postOffice.getProfitPercentage()));
            returnMap.put(postOffice.getName(), profitForParent.setScale(2, RoundingMode.HALF_EVEN));

            BigDecimal remainingProfitForDistribution = profit.subtract(profitForParent);

            BigDecimal totalTransactionsForAllSubsidiaries = collectTransactions(postOffice).
                                map(Transaction::getAmount).
                                reduce(BigDecimal::add).orElse(BigDecimal.ZERO);

            postOffice.getSubsidiaries().forEach(s -> {
                BigDecimal totalTransactionAmtForSubsidiary = getTotalTransactionAmount(s);
                BigDecimal distributionRatio =
                        calculateDistributionProportionForSubsidiary(remainingProfitForDistribution,
                                totalTransactionAmtForSubsidiary,
                                totalTransactionsForAllSubsidiaries);
                distribute(s, distributionRatio, returnMap);
            });
        } else {
            returnMap.put(postOffice.getName(), profit.setScale(2, RoundingMode.HALF_EVEN));
        }


    }

    /**
     * calculate the profit available for distribution based on transaction amount
     * @param remainingProfitForDistribution balance of profit available for distribution
     * @param totalTransactionAmtForSubsidiary total of all transactions for the given subsidiary to calculate ratio
     * @param totalTransactionsForAllSubsidiaries total of all transactions for ALL subsidiraries to calculate ratio
     * @return profit allocation from total profits based on the ratio of transactions
     */
    private BigDecimal calculateDistributionProportionForSubsidiary(BigDecimal remainingProfitForDistribution, BigDecimal totalTransactionAmtForSubsidiary, BigDecimal totalTransactionsForAllSubsidiaries) {
        return totalTransactionsForAllSubsidiaries.compareTo(BigDecimal.ZERO) != 0 ?
                remainingProfitForDistribution.multiply(totalTransactionAmtForSubsidiary).divide(totalTransactionsForAllSubsidiaries, 2, RoundingMode.HALF_EVEN):
                BigDecimal.ZERO;
    }

}
