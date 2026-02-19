package solitaire;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * MENÚ PRINCIPAL DEL SOLITARIO
 */
public class SolitaireMenu extends Application {

    @Override
    public void start(Stage ventanaPrincipal) {
        // CREAR CONTENEDOR PRINCIPAL
        VBox raiz = new VBox(20);
        raiz.setPadding(new Insets(50));
        raiz.setAlignment(Pos.CENTER);

        // CARGAR IMAGEN DE FONDO
        raiz.setStyle(
                "-fx-background-image: url('file:src/imagen/fondoEmi.png'); " +
                        "-fx-background-repeat: no-repeat; " +
                        "-fx-background-position: center; " +
                        "-fx-background-size: cover;"
        );

        // CREAR COMPONENTES
        VBox logo = crearLogo();
        VBox panelBotones = crearPanelBotones(ventanaPrincipal);

        raiz.getChildren().addAll(logo, panelBotones);

        Scene escena = new Scene(raiz, 600, 500);
        ventanaPrincipal.setTitle("Solitario - Menú Principal");
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();
    }

    /**
     * CREAR LOGO CON IMAGEN
     */
    private VBox crearLogo() {
        VBox logoContainer = new VBox();
        logoContainer.setPrefWidth(200);
        logoContainer.setPrefHeight(200);
        logoContainer.setAlignment(Pos.CENTER);

        // CARGAR IMAGEN COMO FONDO
        logoContainer.setStyle(
                "-fx-background-image: url('file:src/imagen/fondoal.png'); " +
                        "-fx-background-repeat: no-repeat; " +
                        "-fx-background-position: center; " +
                        "-fx-background-size: contain; " +
                        "-fx-background-color: transparent;"
        );

        return logoContainer;
    }

    /**
     * CREAR BOTÓN CON EFECTOS
     */
    private Button crearBoton(String texto, String colorNormal, String colorHover) {
        Button boton = new Button(texto);
        boton.setPrefWidth(250);
        boton.setPrefHeight(60);
        boton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        boton.setStyle(
                "-fx-background-color: " + colorNormal + "; " +
                        "-fx-text-fill: #FFFFFF; " +
                        "-fx-border-radius: 15; -fx-background-radius: 15; " +
                        "-fx-cursor: hand; " +
                        "-fx-padding: 15; -fx-font-size: 18;"
        );

        // EFECTO AL PASAR EL RATÓN
        boton.setOnMouseEntered(evento ->
                boton.setStyle(
                        "-fx-background-color: " + colorHover + "; " +
                                "-fx-text-fill: #FFFFFF; " +
                                "-fx-border-radius: 15; -fx-background-radius: 15; " +
                                "-fx-cursor: hand; " +
                                "-fx-padding: 15; -fx-font-size: 18; " +
                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 15, 0, 0, 5);"
                )
        );

        // EFECTO AL SALIR EL RATÓN
        boton.setOnMouseExited(evento ->
                boton.setStyle(
                        "-fx-background-color: " + colorNormal + "; " +
                                "-fx-text-fill: #FFFFFF; " +
                                "-fx-border-radius: 15; -fx-background-radius: 15; " +
                                "-fx-cursor: hand; " +
                                "-fx-padding: 15; -fx-font-size: 18;"
                )
        );

        return boton;
    }

    /**
     * PANEL CON BOTONES DEL MENÚ
     */
    private VBox crearPanelBotones(Stage ventanaPrincipal) {
        VBox panelBotones = new VBox(20);
        panelBotones.setAlignment(Pos.CENTER);
        panelBotones.setPadding(new Insets(30));
        panelBotones.setStyle(
                "-fx-background-color: rgba(93, 39, 175, 0.5); " +
                        "-fx-border-color: #7C4DFF; " +
                        "-fx-border-width: 2; -fx-border-radius: 20; -fx-background-radius: 20;"
        );

        // BOTÓN NUEVA PARTIDA
        Button botonNueva = crearBoton(" Nueva Partida", "#7C4DFF", "#5E35B1");
        botonNueva.setOnAction(evento -> {
            // Crear nueva ventana de juego
            Stage ventanaJuego = new Stage();
            SolitaireGUI gui = new SolitaireGUI();
            Scene escenaJuego = new Scene(gui, 900, 600);
            ventanaJuego.setTitle("Solitario - Partida");
            ventanaJuego.setScene(escenaJuego);
            ventanaJuego.show();

            // Cerrar menú principal (opcional)
            ventanaPrincipal.close();
        });

        // BOTÓN SALIR
        Button botonSalir = crearBoton(" Salir", "#512DA8", "#311B92");
        botonSalir.setOnAction(evento -> System.exit(0));

        panelBotones.getChildren().addAll(botonNueva, botonSalir);

        return panelBotones;
    }

    public static void main(String[] args) {
        launch(args);
    }
}