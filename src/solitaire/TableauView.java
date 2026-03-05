package solitaire;

import DeckOfCards.CartaInglesa;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.util.ArrayList;

// Esta clase dibuja las 7 columnas del tablero.
// También maneja los clics en las cartas de estas columnas y en los espacios vacíos.
public class TableauView extends ScrollPane{
    private final SolitaireGame game;
    private final HBox row;
    private final ArrayList<VBox> tableauPanes=new ArrayList<>();

    // Esta interfaz permite que la clase principal controle qué está seleccionado
    public interface SelectionController{
        boolean isWasteSelected();
        void clearSelection();
        boolean isTableauSelected();
        int getTableauSeleccionado();
        void setTableauSeleccionado(int index,int valorCarta);
    }
    private final SelectionController selectionController;

    // Esta acción se usa para avisar que hubo un cambio en el juego
    private final Runnable onCambio;

    public TableauView(SolitaireGame game,SelectionController selectionController,Runnable onCambio){
        this.game=game;
        this.selectionController=selectionController;
        this.onCambio=onCambio;

        row=new HBox(15);
        row.setPadding(new Insets(10,15,15,15));
        row.setAlignment(Pos.TOP_CENTER);
        row.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.TRANSPARENT,CornerRadii.EMPTY,Insets.EMPTY)));

        // Creamos las 7 columnas
        for(int i=0;i<7;i++){
            VBox col=new VBox(0); // ← LÍNEA MODIFICADA: de -80 a 0, los márgenes se manejan carta por carta
            col.setAlignment(Pos.TOP_CENTER);
            col.setPrefWidth(100);
            col.setPadding(new Insets(0,5,0,5));
            col.setCursor(Cursor.HAND);
            col.setPickOnBounds(true);
            col.setBackground(new Background(new BackgroundFill(
                    javafx.scene.paint.Color.TRANSPARENT,CornerRadii.EMPTY,Insets.EMPTY)));
            tableauPanes.add(col);
            row.getChildren().add(col);
        }

        setContent(row);
        setFitToWidth(true);
        setPannable(true);
        setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        setStyle("-fx-background-color: transparent; -fx-background-insets: 0; -fx-padding: 0;");
        setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.TRANSPARENT,CornerRadii.EMPTY,Insets.EMPTY)));
        skinProperty().addListener((obs,oldSkin,newSkin)->{
            lookup(".viewport").setStyle("-fx-background-color: transparent;");
        });

        actualizar();
    }

    // Redibuja todas las columnas
    public void actualizar(){
        for(int i=0;i<tableauPanes.size();i++){
            VBox col=tableauPanes.get(i);
            col.getChildren().clear();

            ArrayList<CartaInglesa> cartas=game.getTableau().get(i).getCards();
            if(cartas.isEmpty()){
                // rectángulo gris que también recibe clics
                StackPane placeholder=new StackPane();
                placeholder.setPrefSize(90,120);
                placeholder.setMaxSize(90,120);
                placeholder.setMinSize(90,120);
                placeholder.setBorder(new Border(new BorderStroke(javafx.scene.paint.Color.rgb(230,230,230,0.4),BorderStrokeStyle.DASHED,new CornerRadii(10),new BorderWidths(2))));
                placeholder.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.rgb(0,0,0,0.1),new CornerRadii(10),Insets.EMPTY)));

                final int colIndex=i;
                placeholder.setOnMouseClicked(event->{
                    if(event.getButton()==MouseButton.PRIMARY){
                        manejarClickEnColumnaVacia(colIndex);
                    }
                });

                col.getChildren().add(placeholder);
            }else{
                // Columna con cartas
                for(int idx=0;idx<cartas.size();idx++){
                    CartaInglesa c=cartas.get(idx);
                    CardView nodoCarta=new CardView(c);

                    final int colIndex=i;
                    final int cardIndex=idx;

                    // ← LÍNEAS AGREGADAS: margen dinámico según si la carta está boca arriba o abajo
                    // La primera carta no tiene margen negativo, las demás sí se solapan
                    if(idx>0){
                        VBox.setMargin(nodoCarta,new Insets(
                                c.isFaceup()?-65:-100,0,0,0));
                    }

                    // Cursor encima de la carta y controlador de los clics
                    nodoCarta.setCursor(c.isFaceup()?Cursor.HAND:Cursor.DEFAULT);
                    if(c.isFaceup()){
                        nodoCarta.setOnMouseClicked(event->{
                            if(event.getButton()==MouseButton.PRIMARY){
                                manejarClickCarta(colIndex,c.getValor());
                            }else if(event.getButton()==MouseButton.SECONDARY&&cardIndex==cartas.size()-1){
                                // Clic derecho en la carta superior intentar ir a Foundation
                                game.moveTableauToFoundation(colIndex+1);
                                selectionController.clearSelection();
                                onCambio.run();
                            }
                        });
                    }

                    // Resaltamos la carta superior de la columna seleccionada
                    if(selectionController.isTableauSelected()&&selectionController.getTableauSeleccionado()==i&&cardIndex==cartas.size()-1){
                        nodoCarta.setSeleccionada(true);
                    }

                    col.getChildren().add(nodoCarta);
                }
            }
        }
    }

    // Esta función decide qué hacer cuando se hace clic izquierdo en una carta
    private void manejarClickCarta(int colIndex,int valorCarta){
        // Si el Waste está seleccionado, intentamos WASTE A COLUMNA
        if(selectionController.isWasteSelected()){
            game.moveWasteToTableau(colIndex+1);
            selectionController.clearSelection();
            onCambio.run();
            return;
        }

        // Si ya había una columna seleccionada, intentamos mover un bloque entre columnas
        if(selectionController.isTableauSelected()&&selectionController.getTableauSeleccionado()!=colIndex){
            int origen=selectionController.getTableauSeleccionado()+1;
            int destino=colIndex+1;
            game.moveTableauToTableau(origen,destino);
            selectionController.clearSelection();
            onCambio.run();
            return;
        }

        // Si no había nada seleccionado, ahora seleccionamos esta columna
        selectionController.clearSelection();
        selectionController.setTableauSeleccionado(colIndex,valorCarta);
        actualizar();
    }

    // Esta función decide qué hacer cuando se hace clic en una columna VACÍA
    private void manejarClickEnColumnaVacia(int colIndex){
        int destino=colIndex+1;

        // Si el Waste está seleccionado, probamos mover Waste a la columna vacía
        if(selectionController.isWasteSelected()){
            game.moveWasteToTableau(destino);
            selectionController.clearSelection();
            onCambio.run();
            return;
        }

        // Si hay una columna seleccionada, intentamos mover bloque Tableau a la columna
        if(selectionController.isTableauSelected()){
            int origen=selectionController.getTableauSeleccionado()+1;
            game.moveTableauToTableau(origen,destino);
            selectionController.clearSelection();
            onCambio.run();
            return;
        }
    }
}