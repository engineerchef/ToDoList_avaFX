package com.tolgaduran.notdefteri.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class NotDosya {
    private static NotDosya instance = new NotDosya();
    private static String dosyaAdi = "notlar.txt";
    private DateTimeFormatter formatter;

    private ObservableList<NotOge> notListesi;

    public ObservableList<NotOge> getNotListesi() {
        return notListesi;
    }

    private NotDosya() {
        formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    }

    public static NotDosya getInstance() {
        return instance;
    }

    public void dosyadanNotlariGetir() throws IOException {
        notListesi = FXCollections.observableArrayList();

        Path dosyaYolu = Paths.get(dosyaAdi);
        BufferedReader br = Files.newBufferedReader(dosyaYolu);

        String ifade;
        try {
            while ((ifade = br.readLine()) != null) {
                String[] notParcalari = ifade.split("\t");
                String baslik = notParcalari[0];
                String detay = notParcalari[1];
                String tarih = notParcalari[2];

                LocalDate bitisTarih = LocalDate.parse(tarih, formatter);
                NotOge dosyadanOkunanNotSatiri = new NotOge(baslik, detay, bitisTarih);

                notListesi.addAll(dosyadanOkunanNotSatiri);
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    public void notlariDosyayaYaz() throws IOException {
        Path dosyaYolu = Paths.get(dosyaAdi);
        BufferedWriter bw = Files.newBufferedWriter(dosyaYolu);

        Iterator<NotOge> iterator = notListesi.iterator();

        try {
            while (iterator.hasNext()) {
                NotOge okunanNot = iterator.next();
                bw.write(String.format("%s\t%s\t%s",
                        okunanNot.getBaslik(),
                        okunanNot.getDetay(),
                        okunanNot.getBitisTarih().format(formatter)
                ));
                bw.newLine();
            }
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
    }

    public void yeniNotEkle(NotOge yeniNot){
        notListesi.add(yeniNot);
    }

    public void notSil(NotOge silinecekNot){
        notListesi.remove(silinecekNot);
    }
}
