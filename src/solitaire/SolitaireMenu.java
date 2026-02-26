package solitaire;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

//MENU PRINCIPAL DEL JUEGO.
public class SolitaireMenu extends Application{
    @Override
    public void start(Stage ventanaPrincipal){
        // Root es el contenedor principal
        StackPane root=new StackPane();
        // Fondo con un degradado verde oscuro, para que parezca mesa de juego
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #005522, #003311);");

        // VBox que va a contener el título y el panel de botones
        VBox contenido=new VBox(25);
        contenido.setAlignment(Pos.CENTER);

        // Un texto grande en la parte superior del menú
        Label titulo=new Label("Solitario ");
        titulo.setTextFill(Color.WHITE);
        titulo.setFont(Font.font("Segoe UI",FontWeight.BOLD,36));
        titulo.setEffect(new DropShadow(10,Color.rgb(0,0,0,0.7)));

        //ES EL PANEL DONDE ESTAN LOS BOTONES
        // Aquí se crean los botones de "Nueva Partida" y "Salir"
        VBox panelBotones=crearPanelBotones(ventanaPrincipal);

        // Agregamos el título y el panel de botones al VBox principal
        contenido.getChildren().addAll(titulo,panelBotones);
        root.getChildren().add(contenido);

        // Creamos la escena con un tamaño fijo
        Scene escena=new Scene(root,800,600);
        ventanaPrincipal.setTitle("Solitario - Menú Principal");
        ventanaPrincipal.setScene(escena);
        ventanaPrincipal.show();
    }

    //metodos para personalizar botones y el hoover
    private Button crearBoton(String texto,String colorIni,String colorFin,String colorHoverIni,String colorHoverFin){
        Button boton=new Button(texto);
        boton.setPrefWidth(260);
        boton.setPrefHeight(60);
        boton.setFont(Font.font("Segoe UI",FontWeight.BOLD,18));
        boton.setTextFill(Color.WHITE);
        boton.setStyle("-fx-background-color: linear-gradient(to right, "+colorIni+", "+colorFin+");"+ "-fx-background-radius: 30;"+ "-fx-border-radius: 30;"+ "-fx-border-color: rgba(255,255,255,0.35);"+ "-fx-border-width: 1.5;"+ "-fx-cursor: hand;"
        );

        DropShadow sombra=new DropShadow(15,Color.rgb(0,0,0,0.65));
        boton.setEffect(sombra);

        // Cuando el mouse pasa encima del botón, se pone sombra
        boton.setOnMouseEntered(e->
                boton.setStyle("-fx-background-color: linear-gradient(to right, "+colorHoverIni+", "+colorHoverFin+");"+ "-fx-background-radius: 30;"+ "-fx-border-radius: 30;"+ "-fx-border-color: rgba(255,255,255,0.8);"+ "-fx-border-width: 1.5;"+ "-fx-cursor: hand;"));
        // Cuando el mouse sale del botón, se quita sombral
        boton.setOnMouseExited(e->
                boton.setStyle("-fx-background-color: linear-gradient(to right, "+colorIni+", "+colorFin+");"+ "-fx-background-radius: 30;"+ "-fx-border-radius: 30;"+ "-fx-border-color: rgba(255,255,255,0.35);"+ "-fx-border-width: 1.5;"+ "-fx-cursor: hand;"));

        return boton;
    }

    //Este método crea el panel que contiene los botones del menú.

    private VBox crearPanelBotones(Stage ventanaPrincipal){
        VBox panelBotones=new VBox(20);
        panelBotones.setAlignment(Pos.CENTER);
        panelBotones.setPadding(new Insets(30));
        panelBotones.setStyle("-fx-background-color: rgba(0,0,0,0.45);"+ "-fx-background-radius: 25;"+ "-fx-border-radius: 25;"+ "-fx-border-color: rgba(255,255,255,0.3);"+ "-fx-border-width: 1.5;");
        panelBotones.setEffect(new DropShadow(20,Color.rgb(0,0,0,0.7)));

        // Al hacer clic, abre la ventana del juego y cierra el menú
        Button botonNueva=crearBoton("Nueva Partida", "#8E2DE2","#4A00E0", "#A74CF2","#5F2DF0");
        botonNueva.setOnAction(evento->{
            Stage ventanaJuego=new Stage();
            // usamos la GUI principal del juego
            SolitaireGUI gui=new SolitaireGUI();
            Scene escenaJuego=new Scene(gui,1000,650);
            ventanaJuego.setTitle("Solitario - Partida");
            ventanaJuego.setScene(escenaJuego);
            ventanaJuego.show();
            ventanaPrincipal.close();
        });

        // Al hacer clic, cierra completamente la aplicación
        Button botonSalir=crearBoton("Salir", "#FF416C","#FF4B2B", "#FF6A88","#FF7A3D");
        botonSalir.setOnAction(evento->System.exit(0));
        panelBotones.getChildren().addAll(botonNueva,botonSalir);
        return panelBotones;
    }

    // Método main para poder ejecutar solo este archivo si se desea
    public static void main(String[] args){
        launch(args);
    }
}