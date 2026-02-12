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

        // Agregar todos los paneles a la raíz
        raiz.getChildren().addAll(
                titulo,
                crearPanelSuperior(),
                crearPanelPilasFundacion(),
                crearPanelColumnas(),
                crearPanelControles()
        );

        Scene escena = new Scene(raiz);
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();

        actualizarPantalla();
    }

    public static void main(String[] args) {
        launch(args);
    }
}