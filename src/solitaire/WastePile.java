package solitaire;

import DeckOfCards.CartaInglesa;

import java.util.ArrayList;

/**
 * Modela el montículo donde se colocan las cartas
 * que se extraen de Draw pile.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
public class WastePile {

    //Implementamos pilas (clase)
    private Pila<CartaInglesa> cartas = new Pila<>();

    //public WastePile() {
        //la pila ya se inicializo, ya no es necesario


    public void addCartas(ArrayList<CartaInglesa> nuevas) {
        //Como la Pila no tiene addAll, agregamos carta por carta con el push()
        for (CartaInglesa carta : nuevas){
            cartas.push(carta);
        }
    }

    public ArrayList<CartaInglesa> emptyPile() {
        ArrayList<CartaInglesa> pile = new ArrayList<>();
        if (!cartas.isEmpty()){

            //sacamos carta por carta con pop() hasta vaciar la pila
            while (!cartas.isEmpty()){
                pile.add(cartas.pop());
            }
            //La pila ya quedó vacía después del while, no necesitamos limpiarla
        }
        return pile;
    }

    /**
     * Obtener la última carta sin removerla.
     * @return Carta que está encima. Si está vacía, es null.
     */
    public CartaInglesa verCarta(){
        //el peek ve el tope sin quitarlo y regresa null si está vacía
        return cartas.peek();
    }

    public CartaInglesa getCarta() {
        //pop quita y regresa el tope, regresa null si está vacía (nuestra clase Pila regresa null en pop() cuando está vacía)
        return cartas.pop();
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        if (cartas.isEmpty()) {
            stb.append("---");
        } else {
            //peek ve el tope sin quitarlo
            CartaInglesa regresar = cartas.peek();
            regresar.makeFaceUp();
            stb.append(regresar.toString());
        }
        return stb.toString();
    }

    public boolean hayCartas() {
        return !cartas.isEmpty();
    }
}