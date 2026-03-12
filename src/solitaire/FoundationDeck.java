package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;
import java.util.ArrayList;


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


    //Método que regresa todas las cartas en una Arraylist
    //guardamos el estado del juego antes de un movimiento
    public ArrayList<CartaInglesa> getCartasComoLista(){

        ArrayList<CartaInglesa> lista = new ArrayList<>();
        //pila para no perder el orden original (pila temp.)
        Pila<CartaInglesa> temp = new Pila<>();

        //copiamos la pila original en una temporal
        while (!cartas.isEmpty()){

            temp.push(cartas.pop());
        }

        //devolvemos las cartas a la pila original y al mismo tiempo las agregamos a la lista
        while (!temp.isEmpty()){

            CartaInglesa carta = temp.pop();
            cartas.push(carta);
            lista.add(carta);
        }

        return lista;
    }

    //Regresa foundation desde una lista anteriormente guardada
    public void restaurar(ArrayList<CartaInglesa> lista){
        //Vaciamos la pila actual
        cartas.clear();

        // Agregamos carta por carta desde la lista guardada
        for (CartaInglesa carta : lista){

            cartas.push(carta);
        }

    }


}