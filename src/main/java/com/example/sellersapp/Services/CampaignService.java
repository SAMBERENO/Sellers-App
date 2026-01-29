package com.example.sellersapp.Services;

import com.example.sellersapp.DBInit.CampaignTable;
import com.example.sellersapp.Repos.CampaignRepository;
import com.example.sellersapp.Repos.PrePopulatedKeyWords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CampaignService {

    @Autowired
    private PrePopulatedKeyWords keywords;

    private final CampaignRepository campaignRepository;

    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }


    public List<CampaignTable> getAllCampaigns() {

        List<CampaignTable> campaigns = new ArrayList<CampaignTable>();

        campaignRepository.findAll().forEach(campaigns::add);

        return campaigns;
    }

    public Set<String> getSuggestedKeywords(String trimmed) {
        Set<String> allKeywords = keywords.getKeywords();
        Set<String> result = new HashSet<>();

        for (String word : allKeywords) {
            if (word.toLowerCase().contains(trimmed)) {
                result.add(word);
            }
        }
        return result;
    }

    public CampaignTable updateCampaign(Optional<CampaignTable> campaignData, CampaignTable campaignTable){

            CampaignTable _campaignTable = campaignData.get();
            _campaignTable.setCampaignName(campaignTable.getCampaignName());
            _campaignTable.setKeywords(campaignTable.getKeywords());
            _campaignTable.setBidMin(campaignTable.getBidMin());
            _campaignTable.setCampaignFund(campaignTable.getCampaignFund());
            _campaignTable.setStatus(campaignTable.getStatus());
            _campaignTable.setTown(campaignTable.getTown());
            _campaignTable.setRadius(campaignTable.getRadius());

        return _campaignTable;
    }
}