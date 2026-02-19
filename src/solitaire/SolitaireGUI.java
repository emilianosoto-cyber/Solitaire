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

// INTERFAZ GRÁFICA DEL SOLITARIO - TABLERO CLÁSICO
public class SolitaireGUI extends Application {
    // VARIABLES DEL JUEGO
    private SolitaireGame juego;

    // VARIABLES DE LA INTERFAZ
    private Label[] etiquetasColumnas;
    private Label[] etiquetasPilasFundacion;
    private Label etiquetaMazo;
    private Label etiquetaDescarte;

    @Override
    public void start(Stage ventanaPrincipal) {
        this.juego = new SolitaireGame();

        ventanaPrincipal.setTitle("Solitario");
        ventanaPrincipal.setWidth(900);
        ventanaPrincipal.setHeight(700);

        // CREAR CONTENEDOR PRINCIPAL
        VBox raiz = new VBox(10);
        raiz.setPadding(new Insets(10));

        // CARGAR IMAGEN DE FONDO
        raiz.setStyle("-fx-background-image: url('file:src/imagen/fondoEmi.png'); " + "-fx-background-repeat: no-repeat; " + "-fx-background-position: center; " + "-fx-background-size: cover;");

        // CREAR PANELES
        HBox panelSuperior = crearPanelSuperior();
        HBox panelFundaciones = crearPanelFundaciones();
        HBox panelTableau = crearPanelTableau();

        raiz.getChildren().addAll(panelSuperior, panelFundaciones, panelTableau);

        Scene escena = new Scene(raiz);
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();

        actualizarPantalla();
    }

    // PANEL SUPERIOR: MAZO Y DESCARTE
    private HBox crearPanelSuperior() {
        HBox panel = new HBox(20);
        panel.setPadding(new Insets(5));
        panel.setAlignment(Pos.CENTER_LEFT);
        panel.setStyle("-fx-background-color: rgba(0, 100, 0, 0.3);");

        // ETIQUETA MAZO
        VBox cajamazo = new VBox();
        cajamazo.setAlignment(Pos.CENTER);
        Label labelMazo = new Label("Mazo");
        labelMazo.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 10; -fx-font-weight: bold;");
        etiquetaMazo = crearEtiquetaCarta("M");
        cajamazo.getChildren().addAll(labelMazo, etiquetaMazo);

        // ETIQUETA DESCARTE
        VBox cajadescarte = new VBox();
        cajadescarte.setAlignment(Pos.CENTER);
        Label labelDescarte = new Label("Descarte");
        labelDescarte.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 10; -fx-font-weight: bold;");
        etiquetaDescarte = crearEtiquetaCarta("D");
        cajadescarte.getChildren().addAll(labelDescarte, etiquetaDescarte);

        panel.getChildren().addAll(cajamazo, cajadescarte);

        return panel;
    }

    // PANEL DE FUNDACIONES
    private HBox crearPanelFundaciones() {
        HBox panel = new HBox(8);
        panel.setPadding(new Insets(5));
        panel.setAlignment(Pos.CENTER);
        panel.setStyle("-fx-background-color: rgba(0, 100, 0, 0.3);");

        Label titulo = new Label("Fundaciones:");
        titulo.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 10; -fx-font-weight: bold;");

        // CREAR 4 FUNDACIONES
        etiquetasPilasFundacion = new Label[4];
        HBox cajaFundaciones = new HBox(8);
        cajaFundaciones.setAlignment(Pos.CENTER);

        for (int i = 0; i < 4; i++) {
            etiquetasPilasFundacion[i] = crearEtiquetaCarta("F");
            cajaFundaciones.getChildren().add(etiquetasPilasFundacion[i]);
        }

        panel.getChildren().addAll(titulo, cajaFundaciones);

        return panel;
    }

    // PANEL DEL TABLERO: 7 COLUMNAS
    private HBox crearPanelTableau() {
        HBox panel = new HBox(8);
        panel.setPadding(new Insets(5));
        panel.setAlignment(Pos.CENTER);
        panel.setStyle("-fx-background-color: rgba(0, 100, 0, 0.5);");

        // CREAR 7 COLUMNAS
        etiquetasColumnas = new Label[7];

        for (int i = 0; i < 7; i++) {
            VBox columna = new VBox(3);
            columna.setAlignment(Pos.TOP_CENTER);
            columna.setPadding(new Insets(5));
            columna.setStyle("-fx-border-color: #228B22; -fx-border-width: 2; " + "-fx-background-color: rgba(0, 100, 0, 0.2);");

            // TÍTULO DE LA COLUMNA
            Label titulo = new Label("Col " + (i + 1));
            titulo.setStyle("-fx-text-fill: #FFFFFF; -fx-font-size: 9;");

            // ETIQUETA DE LA CARTA
            etiquetasColumnas[i] = crearEtiquetaCarta("C");

            columna.getChildren().addAll(titulo, etiquetasColumnas[i]);
            panel.getChildren().add(columna);
        }

        return panel;
    }

    // CREAR ETIQUETA PARA MOSTRAR CARTA
    private Label crearEtiquetaCarta(String texto) {
        Label etiqueta = new Label(texto);
        etiqueta.setPrefWidth(50);
        etiqueta.setPrefHeight(70);
        etiqueta.setAlignment(Pos.CENTER);
        etiqueta.setWrapText(true);
        etiqueta.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        etiqueta.setStyle("-fx-border-color: #000000; -fx-border-width: 1; " + "-fx-background-color: #FFFFFF; -fx-border-radius: 4; " + "-fx-background-radius: 4;");

        return etiqueta;
    }

    // ACTUALIZAR TODA LA INTERFAZ
    private void actualizarPantalla() {
        try {
            actualizarMazo();
            actualizarDescarte();
            actualizarFundaciones();
            actualizarColumnas();

            if (juego.isGameOver()) {
                mostrarVictoria();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ACTUALIZAR VISUALIZACIÓN DEL MAZO
    private void actualizarMazo() {
        if (juego.getDrawPile().hayCartas()) {
            etiquetaMazo.setText(juego.getDrawPile().toString());
            etiquetaMazo.setStyle("-fx-border-color: #000000; -fx-border-width: 1; " + "-fx-background-color: #4169E1; -fx-border-radius: 4; " + "-fx-background-radius: 4; -fx-text-fill: #FFFFFF;");
        } else {
            etiquetaMazo.setText("∅");
            etiquetaMazo.setStyle("-fx-border-color: #000000; -fx-border-width: 1; " + "-fx-background-color: #CCCCCC; -fx-border-radius: 4; " + "-fx-background-radius: 4;");
        }
    }

    // ACTUALIZAR VISUALIZACIÓN DEL DESCARTE
    private void actualizarDescarte() {
        CartaInglesa cartaDescarte = juego.getWastePile().verCarta();

        if (cartaDescarte != null) {
            etiquetaDescarte.setText(cartaDescarte.toString());
            etiquetaDescarte.setStyle("-fx-border-color: #000000; -fx-border-width: 1; " + "-fx-background-color: #FFFFFF; -fx-border-radius: 4; " + "-fx-background-radius: 4;");
        } else {
            etiquetaDescarte.setText("∅");
            etiquetaDescarte.setStyle("-fx-border-color: #000000; -fx-border-width: 1; " + "-fx-background-color: #CCCCCC; -fx-border-radius: 4; " + "-fx-background-radius: 4;");
        }
    }

    // ACTUALIZAR PILAS DE FUNDACIÓN
    private void actualizarFundaciones() {
        String estadoJuego = juego.toString();
        String[] lineas = estadoJuego.split("\n");
        int indicePila = 0;
        boolean enPilas = false;

        for (String linea : lineas) {
            if (linea.equals("Foundation")) {
                enPilas = true;
                continue;
            }
            if (linea.equals("Tableaux") || linea.equals("Waste")) {
                enPilas = false;
                break;
            }
            if (enPilas && !linea.trim().isEmpty()) {
                if (indicePila < etiquetasPilasFundacion.length) {
                    etiquetasPilasFundacion[indicePila].setText(linea);
                    if (linea.equals("---")) {
                        etiquetasPilasFundacion[indicePila].setStyle("-fx-border-color: #000000; " + "-fx-border-width: 1; -fx-background-color: #FFFFFF; " + "-fx-border-radius: 4; -fx-background-radius: 4;");
                    } else {
                        etiquetasPilasFundacion[indicePila].setStyle("-fx-border-color: #000000; " + "-fx-border-width: 1; -fx-background-color: #FFFFCC; " + "-fx-border-radius: 4; -fx-background-radius: 4;");
                    }
                    indicePila++;
                }
            }
        }
    }

    // ACTUALIZAR COLUMNAS
    private void actualizarColumnas() {
        java.util.ArrayList<TableauDeck> columnas = juego.getTableau();
        for (int i = 0; i < columnas.size(); i++) {
            String textoColumna = columnas.get(i).toString();
            etiquetasColumnas[i].setText(textoColumna);
            if (textoColumna.equals("---")) {
                etiquetasColumnas[i].setStyle("-fx-border-color: #000000; -fx-border-width: 1; " + "-fx-background-color: #FFFFFF; -fx-border-radius: 4; " + "-fx-background-radius: 4;");
            } else {
                etiquetasColumnas[i].setStyle("-fx-border-color: #000000; -fx-border-width: 1; " + "-fx-background-color: #FFFFFF; -fx-border-radius: 4; " + "-fx-background-radius: 4;");
            }
        }
    }

    // MOSTRAR VENTANA DE VICTORIA
    private void mostrarVictoria() {
        Stage victoria = new Stage();
        victoria.setTitle("¡Victoria!");

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: linear-gradient(to bottom, #FFD700, #FFA000);");

        // TÍTULO
        Label titulo = new Label("¡GANASTE!");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titulo.setStyle("-fx-text-fill: #2D1B69;");

        // MENSAJE
        Label mensaje = new Label("¡Felicidades!");
        mensaje.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        mensaje.setStyle("-fx-text-fill: #000000;");

        // BOTÓN CERRAR
        javafx.scene.control.Button cerrar = new javafx.scene.control.Button("Cerrar");
        cerrar.setPrefWidth(100);
        cerrar.setPrefHeight(35);
        cerrar.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        cerrar.setStyle("-fx-background-color: #2D1B69; -fx-text-fill: #E0B0FF; " + "-fx-border-radius: 5; -fx-background-radius: 5;");
        cerrar.setOnAction(e -> victoria.close());

        vbox.getChildren().addAll(titulo, mensaje, cerrar);

        Scene scene = new Scene(vbox, 300, 200);
        victoria.setScene(scene);
        victoria.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}