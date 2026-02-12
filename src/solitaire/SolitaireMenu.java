package solitaire;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.awt.*;

public class SolitaireMenu extends Application {

    @Override
    public void start(Stage ventanaPrincipal) {

        VBox raiz = new VBox(30);
        raiz.setPadding(new Insets(50));
        raiz.setAlignment(Pos.CENTER);
        raiz.setStyle("-fx-background-image: url('file:src/imagen/fondo.png'); " + "-fx-background-repeat: no-repeat; " + "-fx-background-position: center; " + "-fx-background-size: cover;");

        // Título
        Label titulo = new Label("SOLITARIO");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        titulo.setStyle("-fx-text-fill: #E0B0FF; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 0, 2);");
        // Panel de botones
        VBox panelBotones = new VBox(20);
        panelBotones.setAlignment(Pos.CENTER);
        panelBotones.setPadding(new Insets(30));
        panelBotones.setStyle("-fx-background-color: rgba(93, 39, 175, 0.3); -fx-border-color: #7C4DFF; " + "-fx-border-width: 2; -fx-border-radius: 20; -fx-background-radius: 20;");

        // Botón de Nueva Partida
        Button botonNueva = crearBoton("Nueva Partida", "#7C4DFF", "#5E35B1");
        botonNueva.setOnAction(evento -> {
            SolitaireGUI gui = new SolitaireGUI();
            gui.start(new Stage());
            ventanaPrincipal.close();
        });

        // Botón deSalir
        Button botonSalir = crearBoton("Salir", "#512DA8", "#311B92");
        botonSalir.setOnAction(evento -> System.exit(0));
        panelBotones.getChildren().addAll(botonNueva, botonSalir);
        raiz.getChildren().addAll(titulo, panelBotones);

        Scene escena = new Scene(raiz, 600, 500);
        ventanaPrincipal.setTitle("Solitario - Menú Principal");
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.setResizable(false);
        ventanaPrincipal.show();
    }

    private Button crearBoton(String texto, String colorNormal, String colorHover) {

        Button boton = new Button(texto);
        boton.setPrefWidth(250);
        boton.setPrefHeight(60);
        boton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        boton.setStyle("-fx-background-color: " + colorNormal + "; -fx-text-fill: #FFFFFF; " + "-fx-border-radius: 15; -fx-background-radius: 15; -fx-cursor: hand; " + "-fx-padding: 15; -fx-font-size: 18;");
        boton.setOnMouseEntered(evento -> boton.setStyle("-fx-background-color: " + colorHover + "; " + "-fx-text-fill: #FFFFFF; -fx-border-radius: 15; -fx-background-radius: 15; " + "-fx-cursor: hand; -fx-padding: 15; -fx-font-size: 18; " + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 15, 0, 0, 5);"));
        boton.setOnMouseExited(evento -> boton.setStyle("-fx-background-color: " + colorNormal + "; " + "-fx-text-fill: #FFFFFF; -fx-border-radius: 15; -fx-background-radius: 15; " + "-fx-cursor: hand; -fx-padding: 15; -fx-font-size: 18;"));

        return boton;
    }

    public static void main(String[] args) {
        launch(args);
    }
}