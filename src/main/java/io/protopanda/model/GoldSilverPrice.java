package io.protopanda.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GoldSilverPrice {
    private Double goldRate;
    private Double silverRate;
    private String dateInIndia;

}

