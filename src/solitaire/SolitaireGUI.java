package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Versión visual del Solitario usando imágenes de src/imagen/images.
 */
public class SolitaireGUI extends BorderPane {

    private final SolitaireGame sg;

    private final HBox topRow;
    private final HBox tableauRow;

    private final StackPane drawPane;
    private final StackPane wastePane;
    private final List<StackPane> foundationPanes = new ArrayList<>();
    private final List<VBox> tableauPanes = new ArrayList<>();

    private final Label mensajeLabel;

    private boolean wasteSeleccionado = false;

    // Rutas base de imágenes
    private static final String IMAGES_BASE_PATH = "src/imagen/images/";

    public SolitaireGUI() {
        this.sg = new SolitaireGame();

        // --------- Fondo tipo tapete (usando imagen si existe) ---------
        try {
            Image fondoImg = new Image(new FileInputStream(IMAGES_BASE_PATH + "fondo_mesa.png"));
            BackgroundImage bgImage = new BackgroundImage(
                    fondoImg,
                    BackgroundRepeat.REPEAT,
                    BackgroundRepeat.REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
            );
            setBackground(new Background(bgImage));
        } catch (Exception e) {
            setStyle("-fx-background-color: #0A8A3A;");
        }

        // --------- Fila superior: Draw, Waste, Foundations ---------
        topRow = new HBox(20);
        topRow.setPadding(new Insets(15));
        topRow.setAlignment(Pos.TOP_LEFT);

        drawPane = crearSlotCarta(true, "DRAW");
        wastePane = crearSlotCarta(false, "WASTE");

        HBox foundationsBox = new HBox(15);
        foundationsBox.setAlignment(Pos.TOP_RIGHT);
        foundationsBox.setPrefWidth(650);

        for (int i = 0; i < 4; i++) {
            StackPane f = crearSlotCarta(false, "F" + (i + 1));
            foundationPanes.add(f);
            foundationsBox.getChildren().add(f);
        }

        topRow.getChildren().addAll(drawPane, wastePane, foundationsBox);
        setTop(topRow);

        // --------- Fila inferior: 7 columnas de tableau ---------
        tableauRow = new HBox(15);
        tableauRow.setPadding(new Insets(10, 15, 15, 15));
        tableauRow.setAlignment(Pos.TOP_CENTER);

        for (int i = 0; i < 7; i++) {
            VBox col = new VBox(-80); // solapado vertical
            col.setAlignment(Pos.TOP_CENTER);
            col.setPrefWidth(100);
            col.setPadding(new Insets(0, 5, 0, 5));
            col.setCursor(Cursor.HAND);
            col.setPickOnBounds(true); // IMPORTANTE: toda la columna recibe clics
            col.setBackground(new Background(new BackgroundFill(
                    Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY
            )));

            final int tableauIndex = i + 1;
            col.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && wasteSeleccionado) {
                    boolean ok = sg.moveWasteToTableau(tableauIndex);
                    if (ok) {
                        mostrarMensaje("Waste → Tableau " + tableauIndex);
                    } else {
                        mostrarMensaje("Movimiento no permitido.");
                    }
                    wasteSeleccionado = false;
                    resaltarWaste(false);
                    actualizarVista();
                }
            });

            tableauPanes.add(col);
            tableauRow.getChildren().add(col);
        }

        setCenter(tableauRow);

        // --------- Barra inferior de mensaje ---------
        mensajeLabel = new Label("Juego iniciado.");
        mensajeLabel.setTextFill(Color.WHITE);
        mensajeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        HBox bottomBar = new HBox(mensajeLabel);
        bottomBar.setPadding(new Insets(8, 15, 10, 15));
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setStyle(
                "-fx-background-color: rgba(0,0,0,0.35);" +
                        "-fx-border-color: rgba(255,255,255,0.25);" +
                        "-fx-border-width: 1 0 0 0;"
        );
        setBottom(bottomBar);

        // --------- Eventos básicos ---------

        // Draw: robar cartas
        drawPane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                sg.drawCards();
                mostrarMensaje("Robando cartas...");
                wasteSeleccionado = false;
                resaltarWaste(false);
                actualizarVista();
            }
        });

        // Waste:
        //  - clic izq: seleccionar/deseleccionar para mover a Tableau
        //  - clic der: intentar mover a Foundation (As o siguiente carta válida)
        wastePane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                wasteSeleccionado = !wasteSeleccionado;
                if (wasteSeleccionado) {
                    mostrarMensaje("Waste seleccionado. Clic en un Tableau para mover.");
                    resaltarWaste(true);
                } else {
                    mostrarMensaje("Selección cancelada.");
                    resaltarWaste(false);
                }
            } else if (event.getButton() == MouseButton.SECONDARY) {
                boolean ok = sg.moveWasteToFoundation();
                if (ok) {
                    mostrarMensaje("Waste → Foundation");
                } else {
                    mostrarMensaje("Movimiento a Foundation no permitido.");
                }
                wasteSeleccionado = false;
                resaltarWaste(false);
                actualizarVista();
            }
        });

        actualizarVista();
    }

    // --------- Creación de slots y cartas ---------

    private StackPane crearSlotCarta(boolean draw, String textoGuia) {
        StackPane slot = new StackPane();
        slot.setPrefSize(90, 120);
        slot.setMaxSize(90, 120);
        slot.setMinSize(90, 120);

        slot.setBorder(new Border(new BorderStroke(
                Color.rgb(250, 250, 250, 0.85),
                BorderStrokeStyle.SOLID,
                new CornerRadii(10),
                new BorderWidths(2)
        )));
        slot.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.15),
                new CornerRadii(10),
                Insets.EMPTY
        )));

        Label guia = new Label(textoGuia);
        guia.setTextFill(Color.rgb(230, 230, 230, 0.8));
        guia.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        slot.getChildren().add(guia);

        if (draw) {
            slot.setCursor(Cursor.HAND);
        }

        return slot;
    }

    private StackPane crearNodoCarta(CartaInglesa carta) {
        StackPane pane = new StackPane();
        pane.setPrefSize(90, 120);
        pane.setMaxSize(90, 120);
        pane.setMinSize(90, 120);

        String imagePath;
        if (!carta.isFaceup()) {
            imagePath = IMAGES_BASE_PATH + "dorso.png";
        } else {
            imagePath = IMAGES_BASE_PATH + obtenerNombreImagenCarta(carta);
        }

        try {
            Image img = new Image(new FileInputStream(imagePath));
            ImageView imageView = new ImageView(img);
            imageView.setFitWidth(90);
            imageView.setFitHeight(120);
            imageView.setPreserveRatio(false);
            pane.getChildren().add(imageView);
        } catch (FileNotFoundException e) {
            // Fallback: carta simple de texto
            pane.setBackground(new Background(new BackgroundFill(
                    Color.WHITE,
                    new CornerRadii(10),
                    Insets.EMPTY
            )));
            pane.setBorder(new Border(new BorderStroke(
                    Color.LIGHTGRAY,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(10),
                    new BorderWidths(2)
            )));
            Label fallback = new Label(
                    valorComoTexto(carta.getValor()) + paloComoTexto(carta.getPalo())
            );
            fallback.setTextFill(carta.getColor().equalsIgnoreCase("rojo") ? Color.RED : Color.BLACK);
            fallback.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            pane.getChildren().add(fallback);
        }

        return pane;
    }

    private String obtenerNombreImagenCarta(CartaInglesa carta) {
        String valorTexto = valorComoTexto(carta.getValor()); // A, 2..10, J, Q, K
        String paloTexto = switch (carta.getPalo()) {
            case CORAZON -> "Corazon";
            case DIAMANTE -> "Diamante";
            case PICA -> "Pica";
            case TREBOL -> "Trebol";
        };
        return valorTexto + paloTexto + ".png";
    }

    private String valorComoTexto(int valor) {
        return switch (valor) {
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            case 14 -> "A";
            default -> String.valueOf(valor);
        };
    }

    private String paloComoTexto(Palo palo) {
        return switch (palo) {
            case CORAZON -> "♥";
            case DIAMANTE -> "♦";
            case PICA -> "♠";
            case TREBOL -> "♣";
        };
    }

    // --------- Actualización de la vista ---------

    private void actualizarVista() {
        // Draw
        drawPane.getChildren().clear();
        CartaInglesa cartaDraw = sg.getDrawPile().verCarta();
        if (cartaDraw != null) {
            cartaDraw.makeFaceDown();
            drawPane.getChildren().add(crearNodoCarta(cartaDraw));
        } else {
            Label guia = new Label("DRAW");
            guia.setTextFill(Color.rgb(230, 230, 230, 0.7));
            guia.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
            drawPane.getChildren().add(guia);
        }

        // Waste
        wastePane.getChildren().clear();
        CartaInglesa cartaWaste = sg.getWastePile().verCarta();
        if (cartaWaste != null) {
            cartaWaste.makeFaceUp();
            wastePane.getChildren().add(crearNodoCarta(cartaWaste));
        } else {
            Label guia = new Label("WASTE");
            guia.setTextFill(Color.rgb(230, 230, 230, 0.7));
            guia.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
            wastePane.getChildren().add(guia);
        }

        // Foundations: por ahora solo “slots”; si expones getFoundations() se pueden dibujar cartas reales
        for (int i = 0; i < foundationPanes.size(); i++) {
            StackPane pane = foundationPanes.get(i);
            pane.getChildren().clear();
            Label guia = new Label("F" + (i + 1));
            guia.setTextFill(Color.rgb(230, 230, 230, 0.8));
            guia.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
            pane.getChildren().add(guia);
        }

        // Tableaux
        for (int i = 0; i < tableauPanes.size(); i++) {
            VBox col = tableauPanes.get(i);
            col.getChildren().clear();

            ArrayList<CartaInglesa> cartas = sg.getTableau().get(i).getCards();
            if (cartas.isEmpty()) {
                StackPane placeholder = new StackPane();
                placeholder.setPrefSize(90, 120);
                placeholder.setMaxSize(90, 120);
                placeholder.setMinSize(90, 120);
                placeholder.setBorder(new Border(new BorderStroke(
                        Color.rgb(230, 230, 230, 0.4),
                        BorderStrokeStyle.DASHED,
                        new CornerRadii(10),
                        new BorderWidths(2)
                )));
                placeholder.setBackground(new Background(new BackgroundFill(
                        Color.rgb(0, 0, 0, 0.1),
                        new CornerRadii(10),
                        Insets.EMPTY
                )));
                col.getChildren().add(placeholder);
            } else {
                for (CartaInglesa c : cartas) {
                    col.getChildren().add(crearNodoCarta(c));
                }
            }
        }

        if (sg.isGameOver()) {
            mostrarMensaje("GAME OVER. ¡Has completado el solitario!");
        }
    }

    private void mostrarMensaje(String texto) {
        mensajeLabel.setText(texto);
    }

    private void resaltarWaste(boolean activo) {
        if (activo) {
            wastePane.setBorder(new Border(new BorderStroke(
                    Color.GOLD,
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(10),
                    new BorderWidths(3)
            )));
        } else {
            wastePane.setBorder(new Border(new BorderStroke(
                    Color.rgb(250, 250, 250, 0.85),
                    BorderStrokeStyle.SOLID,
                    new CornerRadii(10),
                    new BorderWidths(2)
            )));
        }
    }
}