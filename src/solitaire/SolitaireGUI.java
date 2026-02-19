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

public class SolitaireGUI extends BorderPane {

    // Lógica del juego (no dibuja nada, solo sabe reglas y posiciones)
    private final SolitaireGame sg;

    // Contenedores principales
    private final HBox topRow;       // parte superior: mazo, descarte y foundations
    private final HBox tableauRow;   // parte central: 7 columnas

    // Nodos gráficos principales
    private final StackPane drawPane;
    private final StackPane wastePane;
    private final List<StackPane> foundationPanes = new ArrayList<>();
    private final List<VBox> tableauPanes = new ArrayList<>();

    // Mensaje al usuario (texto abajo)
    private final Label mensajeLabel;

    private static final String IMAGES_BASE_PATH = "src/imagen/images/";

    // Tipo de cosa seleccionada por el usuario
    private enum TipoSeleccion { NINGUNO, WASTE, TABLEAU }
    private TipoSeleccion tipoSeleccion = TipoSeleccion.NINGUNO;

    // Índice de la columna seleccionada (0–6) si se seleccionó un tableau
    private int tableauSeleccionado = -1;

    // Valor de la carta sobre la que se hizo clic al seleccionar un tableau
    private int valorSeleccionadoEnTableau = -1;

    // Nodo gráfico de la carta actualmente resaltada (para quitar el borde después)
    private StackPane cartaSeleccionadaNodo = null;

    public SolitaireGUI() {
        this.sg = new SolitaireGame();

        // Fondo verde con imagen; si falla, usa color sólido
        try {
            Image fondoImg = new Image(new FileInputStream(IMAGES_BASE_PATH + "fondo_mesa.png"));
            BackgroundImage bgImage = new BackgroundImage(
                    fondoImg,
                    BackgroundRepeat.REPEAT,
                    BackgroundRepeat.REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO,
                            false, false, true, true)
            );
            setBackground(new Background(bgImage));
        } catch (Exception e) {
            setStyle("-fx-background-color: #0A8A3A;");
        }

        //TOPROW REALIZADO GRACIAS A CHATGPT (ES EL ENCABEZADO O PANEL DONDE SE ENCUENTRA EL MAZO)
        topRow = new HBox(20);
        topRow.setPadding(new Insets(15));
        topRow.setAlignment(Pos.TOP_LEFT);

        // Lugar donde se dibuja el mazo de robo
        drawPane = crearSlotCarta(true, "DRAW");

        // Lugar donde se dibuja el descarte
        wastePane = crearSlotCarta(false, "WASTE");

        // Cuatro foundations a la derecha
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

        //tableauRow tomado de proyectos anteriores
        tableauRow = new HBox(15);
        tableauRow.setPadding(new Insets(10, 15, 15, 15));
        tableauRow.setAlignment(Pos.TOP_CENTER);

        for (int i = 0; i < 7; i++) {
            // Cada columna es un VBox con solapado vertical (espacio negativo)
            VBox col = new VBox(-80);
            col.setAlignment(Pos.TOP_CENTER);
            col.setPrefWidth(100);
            col.setPadding(new Insets(0, 5, 0, 5));
            col.setCursor(Cursor.HAND);
            col.setPickOnBounds(true); // permite clic aunque se haga en espacio vacío
            col.setBackground(new Background(new BackgroundFill(
                    Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY
            )));
            tableauPanes.add(col);
            tableauRow.getChildren().add(col);
        }
        setCenter(tableauRow);

        mensajeLabel = new Label("Juego iniciado.");
        mensajeLabel.setTextFill(Color.WHITE);
        mensajeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        HBox bottomBar = new HBox(mensajeLabel);
        bottomBar.setPadding(new Insets(8, 15, 10, 15));
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setStyle("-fx-background-color: rgba(0,0,0,0.35);" + "-fx-border-color: rgba(255,255,255,0.25);" + "-fx-border-width: 1 0 0 0;"
        );
        setBottom(bottomBar);

        drawPane.setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) return;

            // Si aún hay cartas en el mazo, robamos
            if (sg.getDrawPile().hayCartas()) {
                sg.drawCards();
                limpiarSeleccion();
                mostrarMensaje("Robando cartas...");
            } else {
                // Si no hay cartas en draw pero sí en waste, recargamos el mazo
                if (sg.getWastePile().verCarta() != null) {
                    sg.reloadDrawPile();
                    sg.drawCards();
                    limpiarSeleccion();
                    mostrarMensaje("Recargando mazo desde descarte...");
                } else {
                    mostrarMensaje("No quedan más cartas en el mazo.");
                }
            }
            actualizarVista();
        });

        wastePane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Clic izquierdo: seleccionar o deseleccionar el Waste
                if (tipoSeleccion == TipoSeleccion.WASTE) {
                    limpiarSeleccion();
                    mostrarMensaje("Selección cancelada.");
                } else {
                    limpiarSeleccion();
                    tipoSeleccion = TipoSeleccion.WASTE;
                    mostrarMensaje("Waste seleccionado. Clic en un Tableau para mover.");
                }
                actualizarVista();
            } else if (event.getButton() == MouseButton.SECONDARY) {
                // Clic derecho: intentar mover Waste → Foundation
                boolean ok = sg.moveWasteToFoundation();
                if (ok) {
                    mostrarMensaje("Descarte → Foundation");
                } else {
                    mostrarMensaje("Movimiento a Foundation no permitido.");
                }
                limpiarSeleccion();
                actualizarVista();
            }
        });

        // Dibujar por primera vez
        actualizarVista();
    }

    // Crea un "hueco" para una carta (se usa para Draw, Waste y Foundations)
    private StackPane crearSlotCarta(boolean draw, String textoGuia) {
        StackPane slot = new StackPane();
        slot.setPrefSize(90, 120);
        slot.setMaxSize(90, 120);
        slot.setMinSize(90, 120);

        slot.setBorder(new Border(new BorderStroke(Color.rgb(250, 250, 250, 0.85), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        slot.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.15), new CornerRadii(10), Insets.EMPTY)));

        Label guia = new Label(textoGuia);
        guia.setTextFill(Color.rgb(230, 230, 230, 0.8));
        guia.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
        slot.getChildren().add(guia);

        if (draw) slot.setCursor(Cursor.HAND);
        return slot;
    }

    // Crea el nodo gráfico para una carta (usa imagen según palo y valor)
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
            ImageView iv = new ImageView(img);
            iv.setFitWidth(90);
            iv.setFitHeight(120);
            iv.setPreserveRatio(false);
            pane.getChildren().add(iv);
        } catch (FileNotFoundException e) { //TOMADO DE PROYECTOS ANTERIORES
            // Si no encuentra la imagen, se dibuja un rectángulo blanco con texto
            pane.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(10), Insets.EMPTY)));
            pane.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
            Label lbl = new Label(valorComoTexto(carta.getValor()) + paloComoTexto(carta.getPalo()));
            lbl.setTextFill(carta.getColor().equalsIgnoreCase("rojo") ? Color.RED : Color.BLACK);
            lbl.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            pane.getChildren().add(lbl);
        }

        return pane;
    }

    // Devuelve el nombre del archivo de imagen para una carta (ej. "10Pica.png")
    private String obtenerNombreImagenCarta(CartaInglesa carta) {
        String valorTexto = valorComoTexto(carta.getValor());
        String paloTexto = switch (carta.getPalo()) {
            case CORAZON -> "Corazon";
            case DIAMANTE -> "Diamante";
            case PICA -> "Pica";
            case TREBOL -> "Trebol";
        };
        return valorTexto + paloTexto + ".png";
    }

    // Convierte el valor numérico a texto (A, 2..10, J, Q, K)
    private String valorComoTexto(int valor) {
        return switch (valor) {
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            case 14 -> "A";
            default -> String.valueOf(valor);
        };
    }

    // Devuelve el símbolo de palo como texto
    private String paloComoTexto(Palo palo) {
        return switch (palo) {
            case CORAZON -> "♥";
            case DIAMANTE -> "♦";
            case PICA -> "♠";
            case TREBOL -> "♣";
        };
    }

    private void actualizarVista() {
        drawPane.getChildren().clear();
        CartaInglesa drawCard = sg.getDrawPile().verCarta();
        if (drawCard != null) {
            drawCard.makeFaceDown();
            drawPane.getChildren().add(crearNodoCarta(drawCard));
        } else {
            Label guia = new Label("DRAW");
            guia.setTextFill(Color.rgb(230, 230, 230, 0.7));
            guia.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
            drawPane.getChildren().add(guia);
        }

        // descarte
        wastePane.getChildren().clear();
        CartaInglesa wasteCard = sg.getWastePile().verCarta();
        if (wasteCard != null) {
            wasteCard.makeFaceUp();
            StackPane nodoWaste = crearNodoCarta(wasteCard);
            wastePane.getChildren().add(nodoWaste);

            // Si el Waste está seleccionado, se resalta con borde dorado
            if (tipoSeleccion == TipoSeleccion.WASTE) {
                resaltarNodo(nodoWaste, true);
                cartaSeleccionadaNodo = nodoWaste;
            } else {
                resaltarNodo(nodoWaste, false);
            }
        } else {
            Label guia = new Label("WASTE");
            guia.setTextFill(Color.rgb(230, 230, 230, 0.7));
            guia.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
            wastePane.getChildren().add(guia);
        }

        // FUNDACIONES
        for (int i = 0; i < foundationPanes.size(); i++) {
            StackPane pane = foundationPanes.get(i);
            pane.getChildren().clear();

            FoundationDeck fd = sg.getFoundations().get(i);
            CartaInglesa ultima = fd.getUltimaCarta();

            if (ultima != null) {
                ultima.makeFaceUp();
                StackPane nodo = crearNodoCarta(ultima);
                pane.getChildren().add(nodo);
            } else {
                Label guia = new Label("F" + (i + 1));
                guia.setTextFill(Color.rgb(230, 230, 230, 0.8));
                guia.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 12));
                pane.getChildren().add(guia);
            }
        }

        for (int i = 0; i < tableauPanes.size(); i++) {

            VBox col = tableauPanes.get(i);
            col.getChildren().clear();

            ArrayList<CartaInglesa> cartas = sg.getTableau().get(i).getCards();
            if (cartas.isEmpty()) {
                // Si la columna está vacía, dibujamos solo un contorno
                StackPane placeholder = new StackPane();
                placeholder.setPrefSize(90, 120);
                placeholder.setMaxSize(90, 120);
                placeholder.setMinSize(90, 120);
                placeholder.setBorder(new Border(new BorderStroke(Color.rgb(230, 230, 230, 0.4), BorderStrokeStyle.DASHED, new CornerRadii(10), new BorderWidths(2))));
                placeholder.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.1), new CornerRadii(10), Insets.EMPTY)));
                col.getChildren().add(placeholder);
            } else {
                for (int idx = 0; idx < cartas.size(); idx++) {
                    CartaInglesa c = cartas.get(idx);
                    StackPane nodoCarta = crearNodoCarta(c);

                    final int colIndex = i;
                    final int cardIndex = idx;

                    // Si la carta está boca arriba, permitimos clics sobre ella
                    nodoCarta.setCursor(c.isFaceup() ? Cursor.HAND : Cursor.DEFAULT);
                    if (c.isFaceup()) {
                        nodoCarta.setOnMouseClicked(event -> {
                            if (event.getButton() == MouseButton.PRIMARY) {
                                manejarClickCartaTableau(colIndex, c.getValor());
                            } else if (event.getButton() == MouseButton.SECONDARY && cardIndex == cartas.size() - 1) {
                                // Clic derecho solo tiene sentido en la carta superior
                                manejarClickDerechoTableau(colIndex);
                            }
                        });
                    }

                    // Si esa columna está seleccionada, resaltamos la carta superior
                    if (tipoSeleccion == TipoSeleccion.TABLEAU && tableauSeleccionado == i && cardIndex == cartas.size() - 1) {
                        resaltarNodo(nodoCarta, true);
                        cartaSeleccionadaNodo = nodoCarta;
                    }

                    col.getChildren().add(nodoCarta);
                }
            }
        }

        if (sg.isGameOver()) {
            mostrarMensaje("GAME OVER. ¡Has completado el solitario!");
        }
    }

    // Maneja clic IZQUIERDO en una carta boca arriba de alguna columna
    private void manejarClickCartaTableau(int colIndex, int valorCartaClicada) {
        // Si tenemos seleccionado el Waste, este clic significa "mover Waste → esta columna"
        if (tipoSeleccion == TipoSeleccion.WASTE) {
            boolean ok = sg.moveWasteToTableau(colIndex + 1);
            if (ok) {
                mostrarMensaje("Waste a Tableau " + (colIndex + 1));
            } else {
                mostrarMensaje("Movimiento no permitido.");
            }
            limpiarSeleccion();
            actualizarVista();
            return;
        }

        // Si ya teníamos seleccionada otra columna, intentamos mover un bloque entre columnas
        if (tipoSeleccion == TipoSeleccion.TABLEAU && tableauSeleccionado != -1 && tableauSeleccionado != colIndex) {
            boolean ok = sg.moveTableauToTableau(tableauSeleccionado + 1, colIndex + 1);
            if (ok) {
                mostrarMensaje("Tableau " + (tableauSeleccionado + 1) + " → Tableau " + (colIndex + 1));
            } else {
                mostrarMensaje("Movimiento no permitido.");
            }
            limpiarSeleccion();
            actualizarVista();
            return;
        }

        // Si no había nada seleccionado, ahora marcamos esta columna como seleccionada
        limpiarSeleccion();
        tipoSeleccion = TipoSeleccion.TABLEAU;
        tableauSeleccionado = colIndex;
        valorSeleccionadoEnTableau = valorCartaClicada;
        mostrarMensaje("Tableau " + (colIndex + 1) + " seleccionado. Clic en otro Tableau para mover el bloque, " + "o clic derecho en la carta superior para Foundation.");
        actualizarVista();
    }

    // Maneja clic DERECHO en la carta superior de una columna: intenta Tableau → Foundation
    private void manejarClickDerechoTableau(int colIndex) {
        boolean ok = sg.moveTableauToFoundation(colIndex + 1);
        if (ok) {
            mostrarMensaje("Tableau " + (colIndex + 1) + " → Foundation");
        } else {
            mostrarMensaje("Movimiento a Foundation no permitido.");
        }
        limpiarSeleccion();
        actualizarVista();
    }

    // Limpia cualquier selección actual (waste o tableau)
    private void limpiarSeleccion() {
        tipoSeleccion = TipoSeleccion.NINGUNO;
        tableauSeleccionado = -1;
        valorSeleccionadoEnTableau = -1;
        if (cartaSeleccionadaNodo != null) {
            resaltarNodo(cartaSeleccionadaNodo, false);
            cartaSeleccionadaNodo = null;
        }
    }

    // Agrega o quita un borde dorado a un nodo de carta
    private void resaltarNodo(StackPane nodo, boolean activo) {
        if (activo) {
            nodo.setBorder(new Border(new BorderStroke(
                    Color.GOLD, BorderStrokeStyle.SOLID,
                    new CornerRadii(10), new BorderWidths(3)
            )));
        } else {
            nodo.setBorder(null);
        }
    }

    // Muestra un mensaje en la barra inferior
    private void mostrarMensaje(String texto) {
        mensajeLabel.setText(texto);
    }
}