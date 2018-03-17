package com.tolgaduran.notdefteri;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXToggleButton;
import com.tolgaduran.notdefteri.datamodel.NotDosya;
import com.tolgaduran.notdefteri.datamodel.NotOge;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {
    private ContextMenu listViewMenu;

    @FXML
    private JFXToggleButton toggleBugun;
    @FXML
    private BorderPane anaPencere;
    @FXML
    private JFXListView<NotOge> lvNotlarListesi;
    @FXML
    private Label labelBitisTarihi;
    @FXML
    private JFXTextArea taDetay;

    private FilteredList<NotOge> filteredList;
    private Predicate<NotOge> tumNotlar;
    private Predicate<NotOge> bugununNotlari;

    @FXML
    public void cikisYap() {
        Platform.exit();
    }

    private void secilenNotuSil(NotOge silinecekNot) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Not Sil");
        alert.setHeaderText("Silinecek Not : " + silinecekNot.getBaslik());
        alert.setContentText("Emin misiniz?");

        Optional<ButtonType> sonuc = alert.showAndWait();
        if (sonuc.get() == ButtonType.OK) {
            NotDosya.getInstance().notSil(silinecekNot);
        }
    }

    @FXML
    public void initialize() {
        listViewMenu = new ContextMenu();
        MenuItem notSil = new MenuItem("Notu Sil...");
        notSil.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                NotOge silinecekNot = lvNotlarListesi.getSelectionModel().getSelectedItem();
                secilenNotuSil(silinecekNot);
            }
        });

        listViewMenu.getItems().addAll(notSil);

        tumNotlar = new Predicate<NotOge>() {
            @Override
            public boolean test(NotOge notOge) {
                return true;
            }
        };

        bugununNotlari = new Predicate<NotOge>() {
            @Override
            public boolean test(NotOge notOge) {
                return notOge.getBitisTarih().equals(LocalDate.now());
            }
        };

        filteredList = new FilteredList<NotOge>(NotDosya.getInstance().getNotListesi(), new Predicate<NotOge>() {
            @Override
            public boolean test(NotOge notOge) {
                return true;
            }
        });

        SortedList<NotOge> siraliListe = new SortedList<>(filteredList, new Comparator<NotOge>() {
            @Override
            public int compare(NotOge o1, NotOge o2) {
                return o1.getBitisTarih().compareTo(o2.getBitisTarih());
            }
        });

        lvNotlarListesi.setItems(siraliListe);
        lvNotlarListesi.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<NotOge>() {
            @Override
            public void changed(ObservableValue<? extends NotOge> observable, NotOge oldValue, NotOge newValue) {
                if (newValue != null) {
                    NotOge secilenNot = lvNotlarListesi.getSelectionModel().getSelectedItem();
                    taDetay.setText(secilenNot.getDetay());

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
                    labelBitisTarihi.setText(secilenNot.getBitisTarih().format(formatter));
                }
            }
        });

        lvNotlarListesi.getSelectionModel().selectFirst();

        lvNotlarListesi.setCellFactory(new Callback<ListView<NotOge>, ListCell<NotOge>>() {
            @Override
            public ListCell<NotOge> call(ListView<NotOge> param) {
                ListCell<NotOge> cell = new ListCell<NotOge>() {
                    @Override
                    protected void updateItem(NotOge item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setTextFill(null);
                            setText(item.getBaslik());
                            if (item.getBitisTarih().equals(LocalDate.now())) {
                                setTextFill(Color.RED);
                            } else {
                                setTextFill(Color.BLACK);
                            }
                        }
                    }
                };
                cell.setContextMenu(listViewMenu);
                return cell;
            }
        });
    }

    public void yeniNotEkleDialog(ActionEvent actionEvent) throws IOException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(anaPencere.getScene().getWindow());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("yeniNotEkle.fxml"));

        dialog.setTitle("Yeni Not Ekle");
        dialog.getDialogPane().setContent(fxmlLoader.load());

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> sonuc = dialog.showAndWait();

        if (sonuc.get() == ButtonType.OK) {
            YeniNotEkle controller = fxmlLoader.getController();
            NotOge eklenenNot = controller.yeniNotEkle();
            lvNotlarListesi.getSelectionModel().select(eklenenNot);
        }
    }

    public void notlariFiltrele(ActionEvent event) {
        if (toggleBugun.isSelected()) {
            filteredList.setPredicate(bugununNotlari);
        } else {
            filteredList.setPredicate(tumNotlar);
        }
    }

    public void klavyedenGiris(KeyEvent keyEvent) {
        NotOge secilenNot=lvNotlarListesi.getSelectionModel().getSelectedItem();
        if (secilenNot!=null){
            if (keyEvent.getCode().equals(KeyCode.DELETE)){
                secilenNotuSil(secilenNot);
            }
        }
    }
}
