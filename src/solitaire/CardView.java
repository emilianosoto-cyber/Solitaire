package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

//Esta clase dibuja una carta en pantalla. Recibe una CartaInglesa del modelo y la muestra con imagen.

public class CardView extends StackPane{
    private final CartaInglesa carta;
    private static final String IMAGES_BASE_PATH="src/imagen/images/";

    public CardView(CartaInglesa carta){
        this.carta=carta;
        setPrefSize(90,120);
        setMaxSize(90,120);
        setMinSize(90,120);
        dibujarCarta();
    }

    // Aquí decidimos qué imagen o texto mostrar para la carta
    private void dibujarCarta(){
        getChildren().clear();
        String imagePath;
        if(!carta.isFaceup()){
            imagePath=IMAGES_BASE_PATH+"dorso.png";
        }else{
            imagePath=IMAGES_BASE_PATH+obtenerNombreImagenCarta(carta);
        }
        try{
            Image img=new Image(new FileInputStream(imagePath));
            ImageView iv=new ImageView(img);
            iv.setFitWidth(90);
            iv.setFitHeight(120);
            iv.setPreserveRatio(false);
            getChildren().add(iv);
        }catch(FileNotFoundException e){
            // Si no encuentra la imagen, dibujamos un rectángulo blanco con texto
            setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(10),null)));
            setBorder(new Border(new BorderStroke(Color.LIGHTGRAY,BorderStrokeStyle.SOLID, new CornerRadii(10),new BorderWidths(2))));
            String texto=valorComoTexto(carta.getValor())+paloComoTexto(carta.getPalo());
            Label lbl=new Label(texto);
            lbl.setTextFill(carta.getColor().equalsIgnoreCase("rojo")?Color.RED:Color.BLACK);
            lbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            getChildren().add(lbl);
        }
    }


    //BORDE DORADO.
    public void setSeleccionada(boolean seleccionada){
        if(seleccionada){
            setBorder(new Border(new BorderStroke(
                    Color.GOLD,BorderStrokeStyle.SOLID,
                    new CornerRadii(10),new BorderWidths(3))));
        }else{
            setBorder(null);
        }
    }

    // Estas funciones solo convierten la carta a nombre de archivo y texto
    private String obtenerNombreImagenCarta(CartaInglesa carta){
        String valorTexto=valorComoTexto(carta.getValor());
        String paloTexto=switch(carta.getPalo()){
            case CORAZON -> "Corazon";
            case DIAMANTE -> "Diamante";
            case PICA -> "Pica";
            case TREBOL -> "Trebol";
        };
        return valorTexto+paloTexto+".png";
    }

    private String valorComoTexto(int valor){
        return switch(valor){
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            case 14 -> "A";
            default -> String.valueOf(valor);
        };
    }

    private String paloComoTexto(Palo palo){
        return switch(palo){
            case CORAZON -> "♥";
            case DIAMANTE -> "♦";
            case PICA -> "♠";
            case TREBOL -> "♣";
        };
    }
}