package solitaire;

import DeckOfCards.CartaInglesa;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;

//Esta clase dibuja las 4 foundations. Solo muestra la última carta de cada foundation.

public class FoundationView extends HBox{
    private final SolitaireGame game;
    private final ArrayList<StackPane> foundationPanes=new ArrayList<>();

    public FoundationView(SolitaireGame game){
        this.game=game;
        setSpacing(15);
        setAlignment(Pos.TOP_RIGHT);
        setPadding(new Insets(15));

        for(int i=0;i<4;i++){
            StackPane pane=crearSlot("F"+(i+1));
            foundationPanes.add(pane);
            getChildren().add(pane);
        }
        actualizar();
    }

    // Redibuja cada foundation
    public void actualizar(){
        for(int i=0;i<foundationPanes.size();i++){
            StackPane pane=foundationPanes.get(i);
            pane.getChildren().clear();

            FoundationDeck fd=game.getFoundations().get(i);
            CartaInglesa ultima=fd.getUltimaCarta();
            if(ultima!=null){
                ultima.makeFaceUp();
                pane.getChildren().add(new CardView(ultima));
            }else{
                Label guia=new Label("F"+(i+1));
                guia.setTextFill(Color.rgb(230,230,230,0.8));
                guia.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
                pane.getChildren().add(guia);
            }
        }
    }

    // Crea el hueco visual para una foundation vacía
    private StackPane crearSlot(String texto){
        StackPane slot=new StackPane();
        slot.setPrefSize(90,120);
        slot.setMaxSize(90,120);
        slot.setMinSize(90,120);

        slot.setBorder(new Border(new BorderStroke(Color.rgb(250,250,250,0.85), BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));
        slot.setBackground(new Background(new BackgroundFill(Color.rgb(0,0,0,0.15), new CornerRadii(10), Insets.EMPTY)));

        Label guia=new Label(texto);
        guia.setTextFill(Color.rgb(230,230,230,0.8));
        guia.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        slot.getChildren().add(guia);
        return slot;
    }
}