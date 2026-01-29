package com.example.sellersapp.Controllers;

import com.example.sellersapp.DBInit.CampaignTable;
import com.example.sellersapp.Repos.CampaignRepository;
import com.example.sellersapp.Repos.PrePopulatedKeyWords;
import com.example.sellersapp.Services.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/Controller")
public class CampaignController {

    @Autowired
    private  CampaignRepository campaignRepository;

    @Autowired
    private PrePopulatedKeyWords keywords;

    @Autowired
    private  CampaignService campaignService;

    private double accountBalance = 10000.00;

    @GetMapping("/Campaigns")
    public ResponseEntity<List<CampaignTable>> getAllCampaigns(){
        try {
            campaignService.getAllCampaigns();
            return new ResponseEntity<>(campaignService.getAllCampaigns(), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/Campaigns/{campaignName}")
    public ResponseEntity<CampaignTable> getCampaignByName(@PathVariable("campaignName") String campaignNameid){
        Optional<CampaignTable> campaignData = campaignRepository.findById(campaignNameid);

        if (campaignData.isPresent()) {
            return new ResponseEntity<>(campaignData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/Campaigns/key/{keyword}")
    public ResponseEntity<List<CampaignTable>> getCampaignsByKeyword(@PathVariable("keyword") String _keyword){
        List<CampaignTable> campaigns = campaignRepository.findByKeywords(_keyword);

        if (campaigns.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(campaigns, HttpStatus.OK);
        }
    }

    @GetMapping("/Campaigns/keywords")
    public ResponseEntity<Set<String>> getPrePopulated(){
        return new ResponseEntity<>(keywords.getKeywords(), HttpStatus.OK);
    }

    @GetMapping("/Campaigns/keywords/suggest")
    public ResponseEntity<Set<String>> getSuggestedKeywords(@RequestParam(name = "keyword") String keyword){
        String trimmed = keyword.trim().toLowerCase();

        if (trimmed.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        campaignService.getSuggestedKeywords(trimmed);
        return new ResponseEntity<>(campaignService.getSuggestedKeywords(trimmed), HttpStatus.OK);
    }

    @PostMapping("/Campaigns")
    public ResponseEntity<CampaignTable> createCampaign(@RequestBody CampaignTable campaignTable){
        try {
            CampaignTable _campaignTable = campaignRepository.save(new CampaignTable(campaignTable.getCampaignName(), campaignTable.getKeywords(), campaignTable.getBidMin(), campaignTable.getCampaignFund(), campaignTable.getStatus(), campaignTable.getTown(), campaignTable.getRadius()));
            return new ResponseEntity<>(_campaignTable, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/Campaigns/{campaignName}")
    public ResponseEntity<CampaignTable> updateCampaign(@PathVariable("campaignName") String campaignName, @RequestBody CampaignTable campaignTable){
        Optional<CampaignTable> campaignData = campaignRepository.findById(campaignName);

        if (campaignData.isPresent()) {
            double oldFund = campaignData.get().getCampaignFund();
            campaignService.updateCampaign(campaignData, campaignTable);
            double fundDifference = campaignTable.getCampaignFund() - oldFund;
            if (accountBalance - fundDifference >= 0) {
                accountBalance -= fundDifference;
                return new ResponseEntity<>(campaignRepository.save(campaignService.updateCampaign(campaignData, campaignTable)), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/Campaigns/{campaignName}")
    public ResponseEntity<HttpStatus> deleteCampaigns(@PathVariable("campaignName") String campaignName) {
        try {
            Optional<CampaignTable> campaignData = campaignRepository.findById(campaignName);
            if (campaignData.isPresent()) {
                accountBalance += campaignData.get().getCampaignFund();
            }
            campaignRepository.deleteById(campaignName);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<Map<String, Double>> getBalance(){
        Map<String, Double> response = new HashMap<>();
        response.put("balance", accountBalance);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/balance/deduct")
    public ResponseEntity<Map<String, Double>> deductBalance(@RequestBody Map<String, Double> request){
        Double amount = request.get("amount");
        if (amount == null || amount <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (accountBalance < amount) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        accountBalance -= amount;
        Map<String, Double> response = new HashMap<>();
        response.put("balance", accountBalance);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
