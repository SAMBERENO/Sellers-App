package com.example.sellersapp.Repos;

import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class PrePopulatedKeyWords {

    private Set<String> keywords = new HashSet<>();

    public Set<String> getKeywords() {
        keywords.add("discount");
        keywords.add("promo");
        keywords.add("clearance");
        keywords.add("bestseller");
        keywords.add("new-arrival");
        keywords.add("limited");
        keywords.add("bundle");
        keywords.add("premium");
        keywords.add("seasonal");
        keywords.add("exclusive");
        return keywords;
    } }
