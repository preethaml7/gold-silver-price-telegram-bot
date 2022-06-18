package io.protopanda.service;

import io.protopanda.model.GoldSilverPrice;
import io.protopanda.util.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoldSilverPriceService {
    public GoldSilverPrice scrapeGoldSilverPrice() {

        final String goldPriceUrl = "https://www.goodreturns.in/gold-rates/bangalore.html";
        final String silverPriceUrl = "https://www.goodreturns.in/silver-rates/bangalore.html";

        Double goldPriceForTenGrams = 0.0;
        Double silverPriceForOneKg = 0.0;

        try {

            Document gold = Jsoup.connect(goldPriceUrl).get();
            Document silver = Jsoup.connect(silverPriceUrl).get();

            goldPriceForTenGrams = Double.parseDouble(gold.select(".metal_sub_heading_container")
                    .select(".inr-symbol-icon").first().parentNode().childNodes().get(1).toString()
                    .replace(",", "")) * 10;

            silverPriceForOneKg = Double.parseDouble(silver.select(".metal_sub_heading_container")
                    .select(".inr-symbol-icon").first().parentNode().childNodes().get(1).toString()
                    .replace(",", "")) * 1000;


        } catch (Exception ex) {
            log.error("Error occurred while scraping website to get gold silver price: " + ex.getMessage());
        }

        return GoldSilverPrice.builder()
                .goldRate(goldPriceForTenGrams)
                .silverRate(silverPriceForOneKg)
                .dateInIndia(ServiceUtil.getCurrentDateTimeInIndia())
                .build();
    }
}