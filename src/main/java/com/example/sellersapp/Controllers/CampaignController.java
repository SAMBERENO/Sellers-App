package com.example.sellersapp.Controllers;

import com.example.sellersapp.DBInit.CampaignTable;
import com.example.sellersapp.Repos.CampaignRepository;
import com.example.sellersapp.Repos.PrePopulatedKeyWords;
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

    private double accountBalance = 10000.00;

    @GetMapping("/Campaigns")
    public ResponseEntity<List<CampaignTable>> getAllCampaigns(@RequestParam(required = false) String title){
        try {
            List<CampaignTable> campaigns = new ArrayList<CampaignTable>();

            if (title == null)
                campaignRepository.findAll().forEach(campaigns::add);
            else
                campaignRepository.findByCampaignName(title).forEach(campaigns::add);

            if (campaigns.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(campaigns, HttpStatus.OK);
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

        Set<String> allKeywords = keywords.getKeywords();
        Set<String> result = new HashSet<>();

        for (String word : allKeywords) {
            if (word.toLowerCase().contains(trimmed)) {
                result.add(word);
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
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
            CampaignTable _campaignTable = campaignData.get();
            _campaignTable.setCampaignName(campaignTable.getCampaignName());
            _campaignTable.setKeywords(campaignTable.getKeywords());
            _campaignTable.setBidMin(campaignTable.getBidMin());
            _campaignTable.setCampaignFund(campaignTable.getCampaignFund());
            _campaignTable.setStatus(campaignTable.getStatus());
            _campaignTable.setTown(campaignTable.getTown());
            _campaignTable.setRadius(campaignTable.getRadius());
        return new ResponseEntity<>(campaignRepository.save(_campaignTable), HttpStatus.OK);
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

    @GetMapping("Campaigns/statuson")
    public ResponseEntity<List<CampaignTable>> getAllActiveCampaigns(){
        try{
            List<CampaignTable> activeCampaigns = campaignRepository.findByStatus(true);

            if (activeCampaigns.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(activeCampaigns, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>((HttpHeaders) null, HttpStatus.INTERNAL_SERVER_ERROR);
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
