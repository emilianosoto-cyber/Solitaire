package solitaire;

import DeckOfCards.CartaInglesa;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

//Esta clase dibuja el mazo (draw) y el descarte (waste).Solo es la parte visual, la lógica está en SolitaireGame.

public class StockWasteView extends HBox{
    private final SolitaireGame game;
    private final StackPane drawPane;
    private final StackPane wastePane;

    // Esta interfaz sirve para avisar a la GUI principal
    public interface SelectionListener{
        void onWasteSelected();
        void onWasteDeselected();
        boolean isWasteSelected();
    }
    private final SelectionListener selectionListener;

    public StockWasteView(SolitaireGame game,SelectionListener selectionListener){
        this.game=game;
        this.selectionListener=selectionListener;

        setSpacing(20);
        setPadding(new Insets(15));
        setAlignment(Pos.TOP_LEFT);

        drawPane=crearSlotCarta(true,"DRAW");
        wastePane=crearSlotCarta(false,"WASTE");
        getChildren().addAll(drawPane,wastePane);

        configurarEventos();
        actualizar();
    }

    // Aquí decimos qué pasa cuando el usuario hace clic en draw o waste
    private void configurarEventos(){
        drawPane.setOnMouseClicked(event->{
            if(event.getButton()!=MouseButton.PRIMARY)return;
            if(game.getDrawPile().hayCartas()){
                game.drawCards();
            }else{
                if(game.getWastePile().verCarta()!=null){
                    game.reloadDrawPile();
                    game.drawCards();
                }
            }
            selectionListener.onWasteDeselected();
            actualizar();
        });

        wastePane.setOnMouseClicked(event->{
            if(event.getButton()==MouseButton.PRIMARY){
                if(selectionListener.isWasteSelected()){
                    selectionListener.onWasteDeselected();
                }else{
                    selectionListener.onWasteSelected();
                }
                actualizar();
            }else if(event.getButton()==MouseButton.SECONDARY){
                game.moveWasteToFoundation();
                selectionListener.onWasteDeselected();
                actualizar();
            }
        });
    }

    // Redibuja draw y waste con las cartas actuales
    public void actualizar(){
        // Draw
        drawPane.getChildren().clear();
        CartaInglesa drawCard=game.getDrawPile().verCarta();
        if(drawCard!=null){
            drawCard.makeFaceDown();
            drawPane.getChildren().add(new CardView(drawCard));
        }else{
            Label guia=new Label("DRAW");
            guia.setTextFill(Color.rgb(230,230,230,0.7));
            guia.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
            drawPane.getChildren().add(guia);
        }

        // Waste
        wastePane.getChildren().clear();
        CartaInglesa wasteCard=game.getWastePile().verCarta();
        if(wasteCard!=null){
            wasteCard.makeFaceUp();
            CardView nodoWaste=new CardView(wasteCard);
            if(selectionListener.isWasteSelected()){
                nodoWaste.setSeleccionada(true);
            }
            wastePane.getChildren().add(nodoWaste);
        }else{
            Label guia=new Label("WASTE");
            guia.setTextFill(Color.rgb(230,230,230,0.7));
            guia.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
            wastePane.getChildren().add(guia);
        }
    }

    // Crea un hueco visual para una carta (solo borde y texto)
    private StackPane crearSlotCarta(boolean draw,String textoGuia){
        StackPane slot=new StackPane();
        slot.setPrefSize(90,120);
        slot.setMaxSize(90,120);
        slot.setMinSize(90,120);

        slot.setBorder(new Border(new BorderStroke(Color.rgb(250,250,250,0.85), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        slot.setBackground(new Background(new BackgroundFill(Color.rgb(0,0,0,0.15), new CornerRadii(10), Insets.EMPTY)));

        Label guia=new Label(textoGuia);
        guia.setTextFill(Color.rgb(230,230,230,0.8));
        guia.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        slot.getChildren().add(guia);

        if(draw)slot.setCursor(Cursor.HAND);
        return slot;
    }
}