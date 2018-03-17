package com.tolgaduran.notdefteri;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.tolgaduran.notdefteri.datamodel.NotDosya;
import com.tolgaduran.notdefteri.datamodel.NotOge;
import javafx.fxml.FXML;

import java.time.LocalDate;

public class YeniNotEkle {

    @FXML
    private JFXTextField notBaslik;

    @FXML
    private JFXTextArea notDetay;

    @FXML
    private JFXDatePicker notBitisTarihi;

    public NotOge yeniNotEkle() {
        String baslik=notBaslik.getText().trim();
        String detay=notDetay.getText().trim();
        LocalDate bitis=notBitisTarihi.getValue();

        NotOge eklenecekNot=new NotOge(baslik,detay,bitis);
        NotDosya.getInstance().yeniNotEkle(eklenecekNot);

        return eklenecekNot;
    }
}
