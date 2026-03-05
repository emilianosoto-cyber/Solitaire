package solitaire;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.io.FileInputStream;

// Esta clase es la pantalla principal del juego de solitario.
// También guarda un objeto SolitaireGame, que sabe las reglas del juego y en qué lugar está cada carta.
public class SolitaireGUI extends BorderPane implements StockWasteView.SelectionListener, TableauView.SelectionController{
    // Objeto que maneja la lógica del juego
    private final SolitaireGame game;

    // Vistas que se dibujan en la interfaz
    private final StockWasteView stockWasteView;    // vista del mazo y descarte
    private final FoundationView foundationView;    // vista de las foundations
    private final TableauView tableauView;          // vista de las 7 columnas

    // Ruta donde están guardadas las imágenes
    private static final String IMAGES_BASE_PATH="src/imagen/images/";

    // Variables de selección del jugador
    private boolean wasteSeleccionado=false;
    private int tableauSeleccionado=-1;
    private int valorSeleccionadoEnTableau=-1;

    public SolitaireGUI(){
        game=new SolitaireGame();

        // Cargamos imagen de fondo, si no carga se pone color verde
        try{
            Image fondoImg=new Image(new FileInputStream(IMAGES_BASE_PATH+"fondo_mesa.png"));
            BackgroundImage bgImage=new BackgroundImage(
                    fondoImg,
                    BackgroundRepeat.REPEAT,
                    BackgroundRepeat.REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO,BackgroundSize.AUTO,false,false,true,true)
            );
            setBackground(new Background(bgImage));
        }catch(Exception e){
            setStyle("-fx-background-color: #0A8A3A;");
        }

        // Contenedor horizontal para la parte de arriba
        HBox topRow=new HBox();
        topRow.setPadding(new Insets(10,10,0,10));
        topRow.setAlignment(Pos.TOP_LEFT);
        topRow.setSpacing(20);
        topRow.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,CornerRadii.EMPTY,Insets.EMPTY)));
        topRow.setStyle("-fx-background-color: transparent;");

        // Creamos las vistas pasando this::actualizarTodo como callback
        stockWasteView=new StockWasteView(game,this,this::actualizarTodo);
        foundationView=new FoundationView(game);

        // Región vacía para empujar foundations a la derecha
        Region separador=new Region();
        HBox.setHgrow(separador,Priority.ALWAYS);

        topRow.getChildren().addAll(stockWasteView,separador,foundationView);
        setTop(topRow);

        tableauView=new TableauView(game,this,this::actualizarTodo);
        setCenter(tableauView);

        // Dibujamos todo al inicio
        actualizarTodo();
    }

    @Override
    public void onWasteSelected(){
        wasteSeleccionado=true;
    }

    @Override
    public void onWasteDeselected(){
        wasteSeleccionado=false;
    }

    @Override
    public boolean isWasteSelected(){
        return wasteSeleccionado;
    }

    @Override
    public void clearSelection(){
        wasteSeleccionado=false;
        tableauSeleccionado=-1;
        valorSeleccionadoEnTableau=-1;
    }

    @Override
    public boolean isTableauSelected(){
        return tableauSeleccionado!=-1;
    }

    @Override
    public int getTableauSeleccionado(){
        return tableauSeleccionado;
    }

    @Override
    public void setTableauSeleccionado(int index,int valorCarta){
        tableauSeleccionado=index;
        valorSeleccionadoEnTableau=valorCarta;
    }

    // Refresca todas las partes gráficas
    public void actualizarTodo(){
        stockWasteView.actualizar();
        foundationView.actualizar();
        tableauView.actualizar();
    }
}