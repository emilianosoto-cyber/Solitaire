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

    public SolitaireGame() {
        drawPile = new DrawPile();
        wastePile = new WastePile();
        createTableaux();          // 28 cartas a los 7 tableaus
        createFoundations();       // 4 foundations vacías
        wastePile.addCartas(drawPile.retirarCartas());  // 3 cartas iniciales a waste
    }

    public void reloadDrawPile() {
        ArrayList<CartaInglesa> cards = wastePile.emptyPile();
        drawPile.recargar(cards);
    }

    public void drawCards() {
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
        if (carta != null && moveCartaToTableau(carta, tableau)) {
            wastePile.getCarta();  // ahora sí la removemos
            movimientoRealizado = true;
        }
        return movimientoRealizado;
    }

    public boolean moveTableauToTableau(int tableauFuente, int tableauDestino) {
        boolean movimientoRealizado = false;
        TableauDeck fuente = tableau.get(tableauFuente - 1);
        if (!fuente.isEmpty()) {
            TableauDeck destino = tableau.get(tableauDestino - 1);

            int valorQueDebeTenerLaCartaInicialDeLaFuente;
            if (!destino.isEmpty()) {
                CartaInglesa cartaUltimaDelDestino = destino.verUltimaCarta();
                valorQueDebeTenerLaCartaInicialDeLaFuente = cartaUltimaDelDestino.getValor() - 1;
            } else {
                valorQueDebeTenerLaCartaInicialDeLaFuente = 13; // K
            }

            CartaInglesa cartaInicialDePrueba =
                    fuente.viewCardStartingAt(valorQueDebeTenerLaCartaInicialDeLaFuente);
            if (cartaInicialDePrueba != null && destino.sePuedeAgregarCarta(cartaInicialDePrueba)) {
                ArrayList<CartaInglesa> cartas = fuente.removeStartingAt(
                        valorQueDebeTenerLaCartaInicialDeLaFuente);
                if (destino.agregarBloqueDeCartas(cartas)) {
                    if (!fuente.isEmpty()) {
                        fuente.verUltimaCarta().makeFaceUp();
                    }
                    movimientoRealizado = true;
                }
            }
        }
        return movimientoRealizado;
    }

    public boolean moveTableauToFoundation(int numero) {
        boolean movimientoRealizado = false;

        TableauDeck fuente = tableau.get(numero - 1);
        CartaInglesa carta = fuente.removerUltimaCarta();
        if (carta != null && moveCartaToFoundation(carta)) {
            movimientoRealizado = true;
        } else if (carta != null) {
            // regresar la carta al tableau porque no se puede hacer el movimiento
            fuente.agregarCarta(carta);
        }
        return movimientoRealizado;
    }

    public boolean moveWasteToFoundation() {
        boolean movimientoRealizado = false;

        CartaInglesa carta = wastePile.verCarta();
        if (carta != null && moveCartaToFoundation(carta)) {
            wastePile.getCarta();   // quitarla del waste
            movimientoRealizado = true;
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
                if (ultimaCarta.getValor() != 13) { // no llegó al Rey
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

    public DrawPile getDrawPile() {
        return drawPile;
    }

    public ArrayList<TableauDeck> getTableau() {
        return tableau;
    }

    public WastePile getWastePile() {
        return wastePile;
    }

    public FoundationDeck getLastFoundationUpdated() {
        return lastFoundationUpdated;
    }

    public ArrayList<FoundationDeck> getFoundations() {
        return foundation;
    }
}