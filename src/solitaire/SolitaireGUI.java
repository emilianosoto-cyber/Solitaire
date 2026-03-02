package solitaire;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import java.io.FileInputStream;

// Esta clase es la pantalla principal del juego de solitario.
// También guarda un objeto SolitaireGame, que sabe las reglas del juego y en qué lugar está cada carta.
public class SolitaireGUI extends BorderPane implements StockWasteView.SelectionListener, TableauView.SelectionController{
    // Objeto que maneja la lógica del juego (no dibuja nada, solo sabe reglas)
    private final SolitaireGame game;

    // Vistas que se dibujan en la interfaz
    // vista del mazo y descarte
    private final StockWasteView stockWasteView;
    // vista de las foundations
    private final FoundationView foundationView;
    // vista de las 7 columnas
    private final TableauView tableauView;

    // Ruta donde están guardadas las imágenes de las cartas y el fondo
    private static final String IMAGES_BASE_PATH="src/imagen/images/";

    // Estas variables guardan qué tiene seleccionado el jugador
    private boolean wasteSeleccionado=false;        // true si el descarte está seleccionado
    private int tableauSeleccionado=-1;             // índice de la columna seleccionada (0-6)
    private int valorSeleccionadoEnTableau=-1;      // valor de la carta donde hizo clic

    public SolitaireGUI(){
        // Aquí se crea una nueva partida del juego
        game=new SolitaireGame();

        // Cargamos la imagen de fondo para que se vea como una mesa,
        // si no carga la imagen se pone un fondo verde.
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

        // Creamos un contenedor horizontal para la parte de arriba de la pantalla.
        // A la izquierda va el mazo y el descarte, y a la derecha las foundations.
        HBox topRow=new HBox();
        topRow.setPadding(new Insets(10,10,0,10));
        topRow.setAlignment(Pos.TOP_LEFT);
        topRow.setSpacing(20);

        // Creamos la vista del mazo y descarte.
        stockWasteView=new StockWasteView(game,this,this::actualizarTodo);

        // Creamos la vista de las 4 foundations
        foundationView=new FoundationView(game);

        // Esta región vacía sirve para empujar las foundations hacia la derecha
        Region separador=new Region();
        HBox.setHgrow(separador,Priority.ALWAYS);

        // Agregamos los elementos a la fila superior y la ponemos en el BorderPane
        topRow.getChildren().addAll(stockWasteView,separador,foundationView);
        setTop(topRow);

        // En el CENTRO colocamos la vista de las 7 COLUMNAS del tablero.

        tableauView=new TableauView(game,this,this::actualizarTodo);
        setCenter(tableauView);

        // Dibujamos todo al inicio
        actualizarTodo();
    }

    // Se selecciona el descarte (Waste)
    @Override
    public void onWasteSelected(){
        wasteSeleccionado=true;
    }

    // Se deja de seleccionar el descarte (Waste)
    @Override
    public void onWasteDeselected(){
        wasteSeleccionado=false;
    }

    // Devuelve true si el descarte está actualmente seleccionado
    @Override
    public boolean isWasteSelected(){
        return wasteSeleccionado;
    }

    // Quita cualquier selección que haya (ni Waste ni columnas quedan seleccionadas)
    @Override
    public void clearSelection(){
        wasteSeleccionado=false;
        tableauSeleccionado=-1;
        valorSeleccionadoEnTableau=-1;
    }

    // Devuelve true si hay alguna columna del tablero seleccionada
    @Override
    public boolean isTableauSelected(){
        return tableauSeleccionado!=-1;
    }

    // Devuelve el número de la columna seleccionada (0-6)
    @Override
    public int getTableauSeleccionado(){
        return tableauSeleccionado;
    }

    // Marca una columna del tablero como seleccionada y guarda el valor de la carta clicada
    @Override
    public void setTableauSeleccionado(int index,int valorCarta){
        tableauSeleccionado=index;
        valorSeleccionadoEnTableau=valorCarta;
    }

    // Refresca todas las partes gráficas: mazo, descarte, foundations y columnas
    public void actualizarTodo(){
        stockWasteView.actualizar();
        foundationView.actualizar();
        tableauView.actualizar();
    }
}