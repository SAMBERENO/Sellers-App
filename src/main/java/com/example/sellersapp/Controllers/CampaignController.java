package com.example.sellersapp.Controllers;

import com.example.sellersapp.DBInit.CampaignTable;
import com.example.sellersapp.Repos.CampaignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Controller")
public class CampaignController {

    @Autowired
    CampaignRepository campaignRepository;

    @GetMapping("/Campaigns")
    public ResponseEntity<List<CampaignTable>> getAllCampaigns(@RequestParam(required = false) String title){
        try {
            List<CampaignTable> campaigns = new ArrayList<CampaignTable>();

            if (title == null)
                campaignRepository.findAll().forEach(campaigns::add);
            else
                campaignRepository.findByCampaignName(title).forEach(campaigns::add);

            //TODO: ZOSTAWIĆ NA KONIEC
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



}
