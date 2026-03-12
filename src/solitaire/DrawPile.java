package solitaire;

import DeckOfCards.CartaInglesa;

import java.util.ArrayList;

/**
 * Modela un mazo de cartas de solitario.
 * @author Cecilia Curlango
 * @version 2025
 */
public class DrawPile {

    //Usamos la clase pila
    private Pila<CartaInglesa> cartas = new Pila<>();

    private int cuantasCartasSeEntregan = 3;

    public DrawPile() {
        DeckOfCards.Mazo mazo = new DeckOfCards.Mazo();

        //Agregamos carta por carta a la pila
        for (CartaInglesa carta:mazo.getCartas()){
            cartas.push(carta);
        }
        setCuantasCartasSeEntregan(3);
    }

    public void setCuantasCartasSeEntregan(int cuantasCartasSeEntregan) {
        this.cuantasCartasSeEntregan = cuantasCartasSeEntregan;
    }

    public int getCuantasCartasSeEntregan() {
        return cuantasCartasSeEntregan;
    }

    /**
     * Retirar una cantidad de cartas al inicio de la partida
     * para cargar las cartas de los tableaus.
     */
    public ArrayList<CartaInglesa> getCartas(int cantidad) {
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            //EL POP() QUITA LA CARTA DEL TOPE
            retiradas.add(cartas.pop());
        }
        return retiradas;
    }

    /**
     * Retira y entrega las cartas del monton.
     */
    public ArrayList<CartaInglesa> retirarCartas() {
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        int maximoARetirar = cartas.size() < cuantasCartasSeEntregan ? cartas.size() : cuantasCartasSeEntregan;

        for (int i = 0; i < maximoARetirar; i++){
            //EL POP() QUITA LA CARTA DEL TOPE
            CartaInglesa retirada = cartas.pop();
            retirada.makeFaceUp();
            retiradas.add(retirada);
        }
        return retiradas;
    }

    public boolean hayCartas() {
        //VERIfica si la pila está vacía
        return !cartas.isEmpty();
    }

    public CartaInglesa verCarta() {
        //ve el tope sin quitarlo
        return cartas.peek();
    }

    /**
     * Agrega las cartas recibidas al monton y las voltea boca abajo.
     */
    public void recargar(ArrayList<CartaInglesa> cartasAgregar) {
        //Vaciamos la pila y agregamos carta por carta
        cartas.clear();
        for (CartaInglesa carta : cartasAgregar){
            carta.makeFaceDown();
            //Agrega cada carta al tope de la pila
            cartas.push(carta);
        }
    }

    @Override
    public String toString() {
        if (cartas.isEmpty()) {
            return "-E-";
        }
        return "@";
    }
}