package com.acme.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PostOfficeTest {

    private static final String POST_OF_AUSTRALIA = "Post of Australia";

    private PostOffice underTest;
    private PostOffice subsidiary1;
    private PostOffice subsidiary2;

    private Transaction transaction1;
    private Transaction transaction2;

    @Before
    public void setup(){
        underTest = new PostOffice(POST_OF_AUSTRALIA, 0.1d);
        subsidiary1 = new PostOffice("PostOffice 2", 0.2d);
        subsidiary2 = new PostOffice("PostOffice 3", 0.3d);
        transaction1 = new Transaction(1L, ArticleType.ENVELOPES, BigDecimal.TEN);
        transaction2 = new Transaction(2L, ArticleType.STAMPS, BigDecimal.TEN);
    }

    @Test
    public void testPostOfficeCreation(){
        assertThat(underTest.getName(), equalTo(POST_OF_AUSTRALIA));
        assertThat(underTest.getProfitPercentage(), equalTo(0.1d));
        assertThat(underTest.getParentOffice(), is(nullValue()));
    }

    @Test
    public void testSubsidiaryPopulation(){
        underTest.addSubsidiary(subsidiary1);
        underTest.addSubsidiary(subsidiary2);
        List<String> subsidiaryNames = underTest.getSubsidiaries().stream().map(PostOffice::getName).collect(Collectors.toList());

        assertThat(underTest.getSubsidiaries().size(), equalTo(2));
        assertThat(subsidiaryNames, containsInAnyOrder("PostOffice 2", "PostOffice 3"));
        assertThat(underTest.getSubsidiaries().get(0).getParentOffice(), equalTo(underTest));
        assertThat(underTest.getSubsidiaries().get(1).getParentOffice(), equalTo(underTest));
    }

    @Test
    public void testTransactionPopulation(){
        underTest.addSubsidiary(subsidiary1);
        subsidiary1.addTransaction(transaction1);
        subsidiary1.addTransaction(transaction2);
        List<Long> transactions = underTest.getSubsidiaries().stream().
                flatMap(p -> p.getTransactions().stream()).
                map(Transaction::getTransactionId).
                collect(Collectors.toList());
        assertThat(transactions, containsInAnyOrder(1L, 2L));
    }

}
