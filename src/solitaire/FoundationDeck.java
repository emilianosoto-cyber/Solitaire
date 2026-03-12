package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

public class FoundationDeck {

    private Palo palo;

    //Implementamos pilas (clase)
    Pila<CartaInglesa> cartas = new Pila<>();

    public FoundationDeck(Palo palo) {
        this.palo = palo;
    }

    public FoundationDeck(CartaInglesa carta) {
        palo = carta.getPalo();
        if (carta.getValorBajo() == 1){
            //El push coloca el As en el tope
            cartas.push(carta);
        }
    }

    public boolean agregarCarta(CartaInglesa carta) {
        boolean agregado = false;

        if (carta.tieneElMismoPalo(palo)) {
            if (cartas.isEmpty()) {
                if (carta.getValorBajo() == 1){
                    //El push coloca el As en el tope de la pila vacía
                    cartas.push(carta);
                    agregado = true;
                }
            } else {
                //ve carta sin quitar
                CartaInglesa ultimaCarta = cartas.peek();
                if (ultimaCarta.getValorBajo() + 1 == carta.getValorBajo()){
                    //se apila una carta nueva encima de la que estaba anteriormente
                    cartas.push(carta);
                    agregado = true;
                }
            }
        }
        return agregado;
    }

    CartaInglesa removerUltimaCarta() {
        //quita y regresa el tope en una fundacion
        return cartas.pop();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (cartas.isEmpty()) {
            builder.append("---");
        } else {
            // El for-each funciona igual que nuestra pila
            for (int i = 0; i < cartas.size(); i++){
                builder.append(cartas.peek().toString());
            }
        }
        return builder.toString();
    }

    public boolean estaVacio() {
        return cartas.isEmpty();
    }

    public CartaInglesa getUltimaCarta() {
        //ve el tope - null si esta vacia
        return cartas.peek();
    }

    public Palo getPalo() {
        return palo;
    }
}