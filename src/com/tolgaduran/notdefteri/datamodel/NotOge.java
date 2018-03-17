package com.tolgaduran.notdefteri.datamodel;

import java.time.LocalDate;

public class NotOge {
    private String baslik;
    private String detay;
    private LocalDate bitisTarih;

    public NotOge(String baslik, String detay, LocalDate bitisTarih) {
        setBaslik(baslik);
        setDetay(detay);
        setBitisTarih(bitisTarih);
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getDetay() {
        return detay;
    }

    public void setDetay(String detay) {
        this.detay = detay;
    }

    public LocalDate getBitisTarih() {
        return bitisTarih;
    }

    public void setBitisTarih(LocalDate bitisTarih) {
        this.bitisTarih = bitisTarih;
    }

    @Override
    public String toString() {
        return getBaslik();
    }
}
