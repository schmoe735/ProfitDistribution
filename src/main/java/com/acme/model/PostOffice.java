package com.acme.model;

import java.util.ArrayList;
import java.util.List;

public class PostOffice {

    private String name;
    private PostOffice parentOffice;
    private double profitPercentage;//0.0 = Nil % to 1.0 = 100%
    private List<PostOffice> subsidiaries;
    private List<Transaction> transactions;

    public PostOffice(String name, double profitPercentage) {
        super();
        this.setName(name);
        this.profitPercentage = profitPercentage;
        this.subsidiaries = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PostOffice getParentOffice() {
        return parentOffice;
    }

    public void setParentOffice(PostOffice parentOffice) {
        this.parentOffice = parentOffice;
    }

    public double getProfitPercentage() {
        return profitPercentage;
    }

    public List<PostOffice> getSubsidiaries() {
        return subsidiaries;
    }

    public void addSubsidiary(PostOffice subsidiary){
        this.subsidiaries.add(subsidiary);
        subsidiary.setParentOffice(this);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.getTransactions().add(transaction);
    }
}
