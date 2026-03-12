package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

import java.util.ArrayList;

public class SolitaireGame {
    ArrayList<TableauDeck> tableau = new ArrayList<>();
    ArrayList<FoundationDeck> foundation = new ArrayList<>();
    FoundationDeck lastFoundationUpdated;
    DrawPile drawPile;
    WastePile wastePile;

    // LÍNEA AGREGADA: historial de movimientos para poder deshacer
    // Cada vez que el jugador hace un movimiento válido,
    // guardamos el estado anterior del juego aquí
    private HistorialMovimientos historial = new HistorialMovimientos();

    public SolitaireGame() {
        drawPile  = new DrawPile();
        wastePile = new WastePile();
        createTableaux();
        createFoundations();
        wastePile.addCartas(drawPile.retirarCartas());
    }

    //Método para guardar el estado actual del juego
    private void guardarEstado(){
        //Copiamos las cartas del DrawPile
        ArrayList<CartaInglesa> copyDraw = new ArrayList<>(drawPile.getCartasComoLista());

        //Copiamos las cartas del WastePile
        ArrayList<CartaInglesa> copyWaste = new ArrayList<>(wastePile.getCartasComoLista());

        //Copiamos las 7 columnas del tableau
        ArrayList<ArrayList<CartaInglesa>> copyTableau = new ArrayList<>();

        for (TableauDeck col : tableau){
            copyTableau.add(new ArrayList<>(col.getCards()));
        }

        //Copiamos las 4 foundations
        ArrayList<ArrayList<CartaInglesa>> copyFoundations = new ArrayList<>();

        for (FoundationDeck fd : foundation){
            copyFoundations.add(new ArrayList<>(fd.getCartasComoLista()));
        }

        //Guardamos el estado en el historial
        historial.guardar(new EstadoJuego(copyDraw, copyWaste, copyTableau, copyFoundations));
    }

    //Deshace el último movimiento. Lo que hace es que saca el estado anterior
    //del historial y regresa el juego a ese estado
    public boolean undo() {
        //Si no hay movimientos guardados, no hay nada que deshacer
        if (!historial.hayMovimientos()){
            return false;
        }

        //Sacamos el estado anterior de la pila de historial
        EstadoJuego estadoAnterior = historial.deshacer();

        //regresamos el DrawPile con las cartas del estado anterior
        drawPile.recargar(estadoAnterior.getDraw());

        //regresamos el WastePile con las cartas del estado anterior
        wastePile.restaurar(estadoAnterior.getWaste());

        //regresamos las 7 columnas del tableau
        for (int i = 0; i < tableau.size(); i++){
            tableau.get(i).restaurar(estadoAnterior.getTableau().get(i));
        }

        //regresamos las 4 foundations
        for (int i = 0; i < foundation.size(); i++){
            foundation.get(i).restaurar(estadoAnterior.getFoundations().get(i));
        }

        return true;
    }

    //Método que indica si hay movimientos para deshacer
    //más que nada es para habilitar en la gui el botón undo
    public boolean hayUndo() {
        return historial.hayMovimientos();
    }

    //guardarEstado (guarda estados antes de recargar, robar o se hizo un movimiento válido:))
    public void reloadDrawPile() {
        guardarEstado();
        ArrayList<CartaInglesa> cards = wastePile.emptyPile();
        drawPile.recargar(cards);
    }

    public void drawCards() {
        guardarEstado();
        ArrayList<CartaInglesa> cards = drawPile.retirarCartas();
        wastePile.addCartas(cards);
    }

    public boolean moveWasteToTableau(int tableauDestino) {
        boolean movimientoRealizado = false;
        TableauDeck destino = tableau.get(tableauDestino - 1);
        if (moveWasteToTableau(destino)) {
            movimientoRealizado = true;
        }
        return movimientoRealizado;
    }

    public boolean moveWasteToTableau(TableauDeck tableau) {
        boolean movimientoRealizado = false;
        CartaInglesa carta = wastePile.verCarta();
        if (carta != null && moveCartaToTableau(carta, tableau)){
            guardarEstado();
            // Se ajusta el orden (guardar antes de cualquier cambio)
            wastePile.getCarta();
            movimientoRealizado = true;
        }
        return movimientoRealizado;
    }

    public boolean moveTableauToTableau(int tableauFuente, int tableauDestino) {
        boolean movimientoRealizado = false;
        TableauDeck fuente  = tableau.get(tableauFuente - 1);

        if (!fuente.isEmpty()){

            TableauDeck destino = tableau.get(tableauDestino - 1);
            int valorQueDebeTenerLaCartaInicialDeLaFuente;

            if (!destino.isEmpty()){
                CartaInglesa cartaUltimaDelDestino = destino.verUltimaCarta();
                valorQueDebeTenerLaCartaInicialDeLaFuente = cartaUltimaDelDestino.getValor() - 1;
            } else {
                valorQueDebeTenerLaCartaInicialDeLaFuente = 13; // K
            }

            CartaInglesa cartaInicialDePrueba =
                    fuente.viewCardStartingAt(valorQueDebeTenerLaCartaInicialDeLaFuente);

            if (cartaInicialDePrueba != null && destino.sePuedeAgregarCarta(cartaInicialDePrueba)){
                guardarEstado();
                ArrayList<CartaInglesa> cartas = fuente.removeStartingAt(valorQueDebeTenerLaCartaInicialDeLaFuente);

                if (destino.agregarBloqueDeCartas(cartas)){
                    if (!fuente.isEmpty()){
                        fuente.verUltimaCarta().makeFaceUp();
                    }
                    movimientoRealizado = true;
                }
            }
        }
        return movimientoRealizado;
    }

    // MODIFICADO: antes se quitaba la carta del tableau antes de validar
    // ahora primero verificamos con getUltimaCarta() sin quitar,
    // guardamos el estado, y solo entonces removemos del tableau
    public boolean moveTableauToFoundation(int numero) {
        boolean movimientoRealizado = false;
        TableauDeck fuente = tableau.get(numero - 1);

        // Verificamos si la carta puede ir a la foundation SIN quitarla todavía
        CartaInglesa carta = fuente.getUltimaCarta();

        if (carta != null && moveCartaToFoundation(carta)) {
            guardarEstado(); // guardamos ANTES de modificar el tableau ✅
            fuente.removerUltimaCarta(); // ahora sí quitamos la carta del tableau
            movimientoRealizado = true;
        }
        return movimientoRealizado;
    }

    //Se guarda antes cualquier modificación
    public boolean moveWasteToFoundation() {
        boolean movimientoRealizado = false;
        CartaInglesa carta = wastePile.verCarta();

        if (carta != null) {

            guardarEstado();

            if (moveCartaToFoundation(carta)){

                //ahora sí quitamos del waste
                wastePile.getCarta();
                movimientoRealizado = true;
            } else {
                //Si no se pudo mover, descartamos el estado que acabamos de guardar
                historial.deshacer();
            }
        }
        return movimientoRealizado;
    }

    private boolean moveCartaToTableau(CartaInglesa carta, TableauDeck destino) {
        return destino.agregarCarta(carta);
    }

    private boolean moveCartaToFoundation(CartaInglesa carta) {
        int cualFoundation = carta.getPalo().ordinal();
        FoundationDeck destino = foundation.get(cualFoundation);
        lastFoundationUpdated = destino;
        return destino.agregarCarta(carta);
    }

    public boolean isGameOver() {
        boolean gameOver = true;
        for (FoundationDeck f : foundation) {
            if (f.estaVacio()) {
                gameOver = false;
            } else {
                CartaInglesa ultimaCarta = f.getUltimaCarta();
                if (ultimaCarta.getValor() != 13) {
                    gameOver = false;
                }
            }
        }
        return gameOver;
    }

    private void createFoundations() {
        for (Palo palo : Palo.values()) {
            foundation.add(new FoundationDeck(palo));
        }
    }

    private void createTableaux() {
        for (int i = 0; i < 7; i++) {
            TableauDeck tableauDeck = new TableauDeck();
            tableauDeck.inicializar(drawPile.getCartas(i + 1));
            tableau.add(tableauDeck);
        }
    }

    public DrawPile getDrawPile(){
        return drawPile;
    }

    public ArrayList<TableauDeck> getTableau(){
        return tableau;
    }

    public WastePile getWastePile(){
        return wastePile;
    }

    public FoundationDeck getLastFoundationUpdated(){
        return lastFoundationUpdated;
    }

    public ArrayList<FoundationDeck> getFoundations(){
        return foundation;
    }
}