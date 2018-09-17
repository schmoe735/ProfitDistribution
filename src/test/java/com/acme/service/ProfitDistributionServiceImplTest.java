package com.acme.service;

import com.acme.model.ArticleType;
import com.acme.model.PostOffice;
import com.acme.model.Transaction;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ProfitDistributionServiceImplTest {

    private static final String POST_OF_AUSTRALIA = "Post of Australia";
    private static final String POST_OFFICE_1 = "Post Office 1";
    private static final String POST_OFFICE_2 = "Post Office 2";
    private static final String SUBSIDIARY_3 = "Subsidiary 3";
    private static final String SUBSIDIARY_4 = "Subsidiary 4";
    private static final String SUBSIDIARY_5 = "Subsidiary 5";
    private static final String SUBSIDIARY_6 = "Subsidiary 6";


    ProfitDistributionService profitDistributionService;

    PostOffice underTest;
    PostOffice subsidiary1 = new PostOffice(POST_OFFICE_1, 0.1d);
    PostOffice subsidiary2 = new PostOffice(POST_OFFICE_2, 0.2d);

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setup(){
        this.profitDistributionService = new ProfitDistributionServiceImpl();
        this.underTest = new PostOffice(POST_OF_AUSTRALIA, 0.1d);
    }

    @Test
    public void testIfProfitDistributionInvokedWithANullObject(){
        exception.expectMessage("Post Office object cannot be null for distribution of profit");
        this.profitDistributionService.distributeProfit(null);
    }

    @Test
    public void testIfProfitDistributionWithNoTransactionReturnsZeroProfits(){
        Map<String, BigDecimal> profitDistribution = this.profitDistributionService.distributeProfit(underTest);
        assertThat(profitDistribution.get(POST_OF_AUSTRALIA), equalTo(new BigDecimal("0.00")));
    }

    @Test
    public void testIfProfitDistributionWithNoTransactionsInSubsidiaryReturnsZeroProfitsForAll(){
        underTest.addSubsidiary(subsidiary1);
        underTest.addSubsidiary(subsidiary2);
        Map<String, BigDecimal> profitDistribution = this.profitDistributionService.distributeProfit(underTest);
        assertThat(profitDistribution.get(POST_OFFICE_1), equalTo(new BigDecimal("0.00")));
        assertThat(profitDistribution.get(POST_OFFICE_2), equalTo(new BigDecimal("0.00")));
    }

    @Test
    public void testIfProfitDistributionWithTransactionAmountParentGetsProfitBasedOnTheAllowedPercentage(){
        underTest.addSubsidiary(subsidiary1);
        underTest.addSubsidiary(subsidiary2);
        subsidiary1.addTransaction(new Transaction(1l, ArticleType.STAMPS, BigDecimal.valueOf(1000)));
        Map<String, BigDecimal> profitDistribution = this.profitDistributionService.distributeProfit(underTest);
        assertThat(profitDistribution.get(POST_OF_AUSTRALIA), equalTo(new BigDecimal("2.50")));
        assertThat(profitDistribution.get(POST_OFFICE_1), equalTo(new BigDecimal("22.50")));
        assertThat(profitDistribution.get(POST_OFFICE_2), equalTo(new BigDecimal("0.00")));
    }

    @Test
    public void testIfProfitDistributionWithMultipleTransactionAcrossSubsidiariesAreCalculated(){
        underTest.addSubsidiary(subsidiary1);
        underTest.addSubsidiary(subsidiary2);
        subsidiary1.addTransaction(new Transaction(1l, ArticleType.STAMPS, BigDecimal.valueOf(1000)));
        subsidiary1.addTransaction(new Transaction(2l, ArticleType.ENVELOPES, BigDecimal.valueOf(3000)));
        subsidiary2.addTransaction(new Transaction(3l, ArticleType.STAMPS, BigDecimal.valueOf(500)));
        subsidiary2.addTransaction(new Transaction(4l, ArticleType.STAMPS, BigDecimal.valueOf(1)));

        Map<String, BigDecimal> profitDistribution = this.profitDistributionService.distributeProfit(underTest);
        assertThat(profitDistribution.get(POST_OF_AUSTRALIA), equalTo(new BigDecimal("11.25")));
        assertThat(profitDistribution.get(POST_OFFICE_1), equalTo(new BigDecimal("90.00")));
        assertThat(profitDistribution.get(POST_OFFICE_2), equalTo(new BigDecimal("11.27")));
    }

    @Test
    public void testIfProfitDistributionWithMultipleTransactionsOverMultipleHierarcyIsCalculated(){
        long transactionid = 1l;
        PostOffice subsidiary3 = new PostOffice(SUBSIDIARY_3, 0.3);
        PostOffice subsidiary4 = new PostOffice(SUBSIDIARY_4, 0.4);
        PostOffice subsidiary5 = new PostOffice(SUBSIDIARY_5, 0.5);
        PostOffice subsidiary6 = new PostOffice(SUBSIDIARY_6, 0.6);

        subsidiary1.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));

        subsidiary2.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary2.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));

        subsidiary3.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary3.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary3.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));

        subsidiary4.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary4.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary4.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary4.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));

        subsidiary5.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary5.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary5.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary5.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary5.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));

        subsidiary6.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary6.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary6.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary6.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary6.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));
        subsidiary6.addTransaction(new Transaction(transactionid++, ArticleType.STAMPS, BigDecimal.valueOf(100)));

        subsidiary1.addSubsidiary(subsidiary3);
        subsidiary1.addSubsidiary(subsidiary4);
        subsidiary2.addSubsidiary(subsidiary5);
        subsidiary2.addSubsidiary(subsidiary6);


        underTest.addSubsidiary(subsidiary1);
        underTest.addSubsidiary(subsidiary2);

        Map<String, BigDecimal> profitDistribution = this.profitDistributionService.distributeProfit(underTest);
        assertThat(profitDistribution.get(POST_OF_AUSTRALIA), equalTo(new BigDecimal("5.25")));
        assertThat(profitDistribution.get(POST_OFFICE_1), equalTo(new BigDecimal("1.80")));
        assertThat(profitDistribution.get(POST_OFFICE_2), equalTo(new BigDecimal("5.85")));
        assertThat(profitDistribution.get(SUBSIDIARY_3), equalTo(new BigDecimal("6.94")));
        assertThat(profitDistribution.get(SUBSIDIARY_4), equalTo(new BigDecimal("9.26")));
        assertThat(profitDistribution.get(SUBSIDIARY_5), equalTo(new BigDecimal("10.64")));
        assertThat(profitDistribution.get(SUBSIDIARY_6), equalTo(new BigDecimal("12.76")));


    }

}
