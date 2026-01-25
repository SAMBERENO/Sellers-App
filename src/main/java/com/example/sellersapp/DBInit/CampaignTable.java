package com.example.sellersapp.DBInit;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "campaign_table")
public class CampaignTable {

    @Id
    @Column(name = "campaign_name")
    private String campaignName;

    @ElementCollection
    @CollectionTable(name = "campaign_keywords", joinColumns = @JoinColumn(name = "campaign_name"))
    @Column(name = "keyword")
    private Set<String> keywords = new HashSet<>();

    @Min(value = 1, message = "Provide minimum bid that's at least 1")
    @Column(name = "bid_min")
    private Integer bidMin;

    @Min(value = 0, message = "Campaign fund must be positive")
    @Column(name = "campaign_fund")
    private Double campaignFund;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "town")
    private String town;

    @Min(value = 1, message = "Provide radius")
    @Column(name = "radius")
    private Integer radius;

    public CampaignTable() {}

    public CampaignTable(String campaignName, Set<String> keywords, Integer bidMin, Double campaignFund, Boolean status, String town, Integer radius) {
        this.campaignName = campaignName;
        this.keywords = keywords;
        this.bidMin = bidMin;
        this.campaignFund = campaignFund;
        this.status = status;
        this.town = town;
        this.radius = radius;
    }

    public String getCampaignName() { return campaignName; }

    public void setCampaignName(String campaignName) { this.campaignName = campaignName; }

    public Set<String> getKeywords() { return keywords; }

    public void setKeywords(Set<String> keywords) { this.keywords = keywords; }

    public Integer getBidMin() { return bidMin; }

    public void setBidMin(Integer bidMin) { this.bidMin = bidMin; }

    public Double getCampaignFund() { return campaignFund; }

    public void setCampaignFund(Double campaignFund) { this.campaignFund = campaignFund; }

    public Boolean getStatus() { return status; }

    public void setStatus(Boolean status) { this.status = status; }

    public String getTown() { return town; }

    public void setTown(String town) { this.town = town; }

    public Integer getRadius() { return radius; }

    public void setRadius(Integer radius) { this.radius = radius; }
}
