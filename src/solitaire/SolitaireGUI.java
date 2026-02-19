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
        raiz.getChildren().addAll(titulo, crearPanelSuperior(), crearPanelPilasFundacion(), crearPanelColumnas(), crearPanelControles());
        Scene escena = new Scene(raiz);
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();

        actualizarPantalla();

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
            Label etiqueta = new Label();
            etiqueta.setFont(Font.font("Arial", FontWeight.BOLD, 9));
            etiqueta.setStyle("-fx-text-fill: #E0B0FF;");

            etiquetasColumnas[i] = crearEtiquetaCarta("C" + (i + 1));
            caja.getChildren().addAll(etiqueta, etiquetasColumnas[i]);
            panel.getChildren().add(caja);
        }

        return panel;
    }

    // PANEL PARA LOS CONTROLES
    private VBox crearPanelControles() {
        VBox panel = new VBox(6);
        panel.setPadding(new Insets(8));
        panel.setStyle("-fx-background-color: rgba(124, 77, 255, 0.2); -fx-border-color: #7C4DFF; " + "-fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;");

        Label titulo = new Label("Opciones");
        titulo.setFont(Font.font("Arial", FontWeight.BOLD, 11));
        titulo.setStyle("-fx-text-fill: #E0B0FF;");

        //  FILAS PARA LOS CONTROLES
        HBox fila1 = crearFila1();
        HBox fila2 = crearFila2();
        HBox fila3 = crearFila3();

        panel.getChildren().addAll(titulo, fila1, fila2, fila3);

        return panel;
    }

    //BOTONES
    private HBox crearFila1() {
        HBox fila1 = new HBox(6);
        fila1.setAlignment(Pos.CENTER);

        javafx.scene.control.Button sacar = crearBoton("Sacar", "#7C4DFF", "#5E35B1", 80);
        javafx.scene.control.Button recargar = crearBoton("Recargar", "#FF9800", "#F57C00", 80);

        sacar.setOnAction(e -> {
            juego.drawCards();
            actualizarPantalla();
        });

        recargar.setOnAction(e -> {
            juego.reloadDrawPile();
            actualizarPantalla();
        });

        fila1.getChildren().addAll(sacar, recargar);
        return fila1;
    }

    /**
     * Crea la segunda fila de botones (Movimientos principales).
     */
    private HBox crearFila2() {
        HBox fila2 = new HBox(6);
        fila2.setAlignment(Pos.CENTER);

        javafx.scene.control.Button d2p = crearBoton("D→P", "#0288D1", "#0277BD", 60);
        javafx.scene.control.Button p2p = crearBoton("P→P", "#00897B", "#00695C", 60);
        javafx.scene.control.Button nuevo = crearBoton("Nuevo", "#FFD700", "#FFA000", 60);

        d2p.setOnAction(e -> {
            juego.moveWasteToFoundation();
            actualizarPantalla();
        });

        p2p.setOnAction(e -> {
            int origen = comboOrigen.getValue();
            juego.moveTableauToFoundation(origen);
            actualizarPantalla();
        });

        nuevo.setOnAction(e -> {
            juego = new SolitaireGame();
            actualizarPantalla();
        });

        fila2.getChildren().addAll(d2p, p2p, nuevo);
        return fila2;
    }

    /**
     * Crea la tercera fila de controles (ComboBox y botones).
     */
    private HBox crearFila3() {
        HBox fila3 = new HBox(6);
        fila3.setAlignment(Pos.CENTER);

        // Etiqueta "De:" y ComboBox origen
        Label l1 = new Label("De:");
        l1.setStyle("-fx-text-fill: #E0B0FF; -fx-font-size: 10;");
        comboOrigen = crearComboBox();
        comboOrigen.setValue(1);

        // Etiqueta "A:" y ComboBox destino
        Label l2 = new Label("A:");
        l2.setStyle("-fx-text-fill: #E0B0FF; -fx-font-size: 10;");
        comboDestino = crearComboBox();
        comboDestino.setValue(2);

        // Botones de movimiento
        javafx.scene.control.Button mover = crearBoton("Mover", "#7B1FA2", "#6A1B9A", 60);
        javafx.scene.control.Button d2c = crearBoton("D→C", "#C2185B", "#AD1457", 60);

        mover.setOnAction(e -> {
            int origen = comboOrigen.getValue();
            int destino = comboDestino.getValue();
            if (origen != destino) {
                juego.moveTableauToTableau(origen, destino);
                actualizarPantalla();
            }
        });

        d2c.setOnAction(e -> {
            int destino = comboDestino.getValue();
            juego.moveWasteToTableau(destino);
            actualizarPantalla();
        });

        fila3.getChildren().addAll(l1, comboOrigen, l2, comboDestino, mover, d2c);
        return fila3;
    }

    /**
     * Crea un botón estilizado con efectos hover.
     */
    private javafx.scene.control.Button crearBoton(String texto, String colorNormal, String colorHover, int ancho) {
        javafx.scene.control.Button boton = new javafx.scene.control.Button(texto);
        boton.setPrefHeight(28);
        boton.setPrefWidth(ancho);
        boton.setFont(Font.font("Arial", FontWeight.BOLD, 10));
        boton.setStyle("-fx-background-color: " + colorNormal + "; -fx-text-fill: #FFFFFF; " +
                "-fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 5;");

        boton.setOnMouseEntered(evento -> boton.setStyle("-fx-background-color: " + colorHover + "; " +
                "-fx-text-fill: #FFFFFF; -fx-border-radius: 8; -fx-background-radius: 8; " +
                "-fx-cursor: hand; -fx-padding: 5; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 8, 0, 0, 3);"));

        boton.setOnMouseExited(evento -> boton.setStyle("-fx-background-color: " + colorNormal + "; " +
                "-fx-text-fill: #FFFFFF; -fx-border-radius: 8; -fx-background-radius: 8; " +
                "-fx-cursor: hand; -fx-padding: 5;"));

        return boton;
    }

    /**
     * Crea un ComboBox para seleccionar columnas.
     */
    private javafx.scene.control.ComboBox<Integer> crearComboBox() {
        javafx.scene.control.ComboBox<Integer> combo = new javafx.scene.control.ComboBox<>();
        combo.getItems().addAll(1, 2, 3, 4, 5, 6, 7);
        combo.setPrefWidth(45);
        combo.setStyle("-fx-background-color: #5E35B1; -fx-text-fill: #E0B0FF; " +
                "-fx-border-radius: 5; -fx-padding: 3; -fx-font-size: 9;");
        return combo;
    }

    public static void main(String[] args) {

        launch(args);
    }
}