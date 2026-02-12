package solitaire;

import DeckOfCards.CartaInglesa;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class SolitaireGUI extends Application {

    private SolitaireGame juego;

    private Label[] etiquetasColumnas;
    private Label[] etiquetasPilasFundacion;
    private Label etiquetaMazo;
    private Label etiquetaDescarte;
    private javafx.scene.control.ComboBox<Integer> comboOrigen;
    private javafx.scene.control.ComboBox<Integer> comboDestino;

    @Override
    public void start(Stage ventanaPrincipal) {

        this.juego = new SolitaireGame();
        ventanaPrincipal.setTitle("Solitario");
        ventanaPrincipal.setWidth(900);
        ventanaPrincipal.setHeight(750);
        VBox raiz = new VBox(6);
        raiz.setPadding(new Insets(8));
        raiz.setStyle("-fx-background-color: linear-gradient(to bottom, #2D1B69, #1A0F3D);");

        // título
        Label titulo = new Label("SOLITARIO");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titulo.setStyle("-fx-text-fill: #E0B0FF;");
        titulo.setAlignment(Pos.CENTER);

        // Se hacen los panales para el contenedor de la interfaz (raiz o base)
        // getChildren devuelve los hijos del contenedor. Se utilizó a cambio
        // de constructor del contenedor.
        raiz.getChildren().addAll(titulo, crearPanelSuperior(), crearPanelPilasFundacion());
        Scene escena = new Scene(raiz);
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();

    }

    // EL PANEL PARA EL MAZO
    private HBox crearPanelSuperior() {

        HBox panel = new HBox(15);
        panel.setPadding(new Insets(8));
        panel.setAlignment(Pos.CENTER);
        panel.setStyle("-fx-background-color: rgba(124, 77, 255, 0.2); -fx-border-color: #7C4DFF; " + "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");

        // ETIQUETAS PARA EL MAZO Y EL DECARTE
        Label etiquetaMazoLabel = new Label("Mazo:");
        etiquetaMazoLabel.setStyle("-fx-text-fill: #E0B0FF; -fx-font-size: 11; -fx-font-weight: bold;");

        etiquetaMazo = crearEtiquetaCarta("M");
        Label etiquetaDescarteLabel = new Label("Descarte:");
        etiquetaDescarteLabel.setStyle("-fx-text-fill: #E0B0FF; -fx-font-size: 11; -fx-font-weight: bold;");

        etiquetaDescarte = crearEtiquetaCarta("D");
        panel.getChildren().addAll(
                etiquetaMazoLabel,
                etiquetaMazo,
                etiquetaDescarteLabel,
                etiquetaDescarte
        );

        return panel;
    }

    private Label crearEtiquetaCarta(String texto) {

        Label etiqueta = new Label(texto);
        etiqueta.setPrefWidth(60);
        etiqueta.setPrefHeight(75);
        etiqueta.setAlignment(Pos.CENTER);
        etiqueta.setWrapText(true);
        etiqueta.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        etiqueta.setStyle("-fx-border-color: #7C4DFF; -fx-border-width: 1; -fx-background-color: #FFFFFF; " + "-fx-border-radius: 6; -fx-background-radius: 6;");

        return etiqueta;
    }

    private VBox crearPanelPilasFundacion() {

        VBox panel = new VBox(6);
        panel.setPadding(new Insets(8));
        panel.setStyle("-fx-background-color: rgba(124, 77, 255, 0.2); -fx-border-color: #7C4DFF; " + "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");

        // Título
        Label titulo = new Label("Fundaciones");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        titulo.setStyle("-fx-text-fill: #E0B0FF;");
        // Caja o apartado para las fundaciones
        HBox cajaPilasFundacion = new HBox(8);
        cajaPilasFundacion.setPadding(new Insets(6));
        cajaPilasFundacion.setAlignment(Pos.CENTER);

        //Pilas de funcaicon
        etiquetasPilasFundacion = new Label[4];
        for (int i = 0; i < 4; i++) {

            etiquetasPilasFundacion[i] = crearEtiquetaCarta("" + (i + 1));
            cajaPilasFundacion.getChildren().add(etiquetasPilasFundacion[i]);

        }

        panel.getChildren().addAll(titulo, cajaPilasFundacion);

        return panel;
    }

    //PANEL PARA LAS COLUMNAS
    private HBox crearPanelColumnas() {
        HBox panel = new HBox(6);
        panel.setPadding(new Insets(8));
        panel.setStyle("-fx-background-color: rgba(124, 77, 255, 0.2); -fx-border-color: #7C4DFF; " + "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");
        panel.setAlignment(Pos.TOP_CENTER);
        etiquetasColumnas = new Label[7];
        for (int i = 0; i < 7; i++) {
            VBox caja = new VBox(3);
            caja.setAlignment(Pos.TOP_CENTER);

            //TITULO DEL PANEL
            Label etiqueta = new Label("C" + (i + 1));
            etiqueta.setFont(Font.font("Arial", FontWeight.BOLD, 9));
            etiqueta.setStyle("-fx-text-fill: #E0B0FF;");

            etiquetasColumnas[i] = crearEtiquetaCarta("C" + (i + 1));
            caja.getChildren().addAll(etiqueta, etiquetasColumnas[i]);
            panel.getChildren().add(caja);
        }

        return panel;
    }

    public static void main(String[] args) {

        launch(args);
    }
}