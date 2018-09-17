package com.acme.model;

import java.math.BigDecimal;

public class Transaction {
    private Long transactionId;
    private BigDecimal amount;
    private ArticleType article;

    public Transaction(Long transactionId, ArticleType article, BigDecimal transactionAmount){
        this.transactionId = transactionId;
        this.article = article;
        this.amount = transactionAmount;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public ArticleType getArticle() {
        return article;
    }
}
