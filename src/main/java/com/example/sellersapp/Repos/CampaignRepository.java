package com.example.sellersapp.Repos;

import com.example.sellersapp.DBInit.CampaignTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Set;

public interface CampaignRepository extends JpaRepository<CampaignTable, String> {

    List<CampaignTable> findByCampaignName(String campaignName);

    List<CampaignTable> findByKeywords(String keyword);

    List<CampaignTable> findByBidMin(Integer bidMin);

    List<CampaignTable> findByCampaignFund(Double campaignFund);

    List<CampaignTable> findByStatus(Boolean status);

    List<CampaignTable> findByTown(String town);

    List<CampaignTable> findByRadius(Integer radius);

}
