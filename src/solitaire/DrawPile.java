package solitaire;

import DeckOfCards.CartaInglesa;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * Modela un mazo de cartas de solitario.
 * @author Cecilia Curlango
 * @version 2025
 */
public class DrawPile {

    // Pila Deque para trabajar solo con el tope del mazo
    private Deque<CartaInglesa> cartas = new ArrayDeque<>();
    private int cuantasCartasSeEntregan = 3;

    public DrawPile() {
        DeckOfCards.Mazo mazo = new DeckOfCards.Mazo();

        // Agregamos carta por carta a la pila con el push()
        for (CartaInglesa carta : mazo.getCartas()){
            cartas.push(carta);
        }
        setCuantasCartasSeEntregan(3);
    }

    /**
     * Establece cuantas cartas se sacan cada vez.
     * Puede ser 1 o 3 normalmente.
     * @param cuantasCartasSeEntregan
     */
    public void setCuantasCartasSeEntregan(int cuantasCartasSeEntregan) {
        this.cuantasCartasSeEntregan = cuantasCartasSeEntregan;
    }

    /**
     * Regresa la cantidad de cartas que se sacan cada vez.
     * @return cantidad de cartas que se entregan
     */
    public int getCuantasCartasSeEntregan() {
        return cuantasCartasSeEntregan;
    }

    /**
     * Retirar una cantidad de cartas. Este método se utiliza al inicio
     * de una partida para cargar las cartas de los tableaus.
     * Si se tratan de remover más cartas de las que hay,
     * se provocará un error.
     * @param cantidad de cartas que se quieren retirar
     * @return cartas retiradas
     */
    public ArrayList<CartaInglesa> getCartas(int cantidad) {
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            // Se quita la carta del tope de la pila
            retiradas.add(cartas.pop());
        }
        return retiradas;
    }

    /**
     * Retira y entrega las cartas del monton. La cantidad que retira
     * depende de cuántas cartas quedan en el montón y serán hasta el máximo
     * que se configuró inicialmente.
     * @return Cartas retiradas.
     */
    public ArrayList<CartaInglesa> retirarCartas() {
        ArrayList<CartaInglesa> retiradas = new ArrayList<>();

        // Cartas calculadas que se van a retirar
        int maximoARetirar = cartas.size() < cuantasCartasSeEntregan ? cartas.size() : cuantasCartasSeEntregan;

        for (int i = 0; i < maximoARetirar; i++) {

            // pop() quita la carta del tope
            CartaInglesa retirada = cartas.pop();
            retirada.makeFaceUp();
            retiradas.add(retirada);
        }
        return retiradas;
    }

    /**
     * Indica si aún quedan cartas para entregar.
     * @return true si hay cartas, false si no.
     */
    public boolean hayCartas() {
        // Verificamos si la pila está vacía con un isEmpty()
        return !cartas.isEmpty();
    }

    /**
     * Ve la carta del tope sin quitarla.
     * @return carta del tope, o null si está vacía
     */
    public CartaInglesa verCarta() {
        // Se ve el tope de la pila y regresa null si está vacía
        // peek() hace lo mismo pero de forma más sencilla:
        return cartas.peek();
    }

    /**
     * Agrega las cartas recibidas al monton y las voltea
     * para que no se vean las caras.
     * @param cartasAgregar cartas que se agregan
     */
    public void recargar(ArrayList<CartaInglesa> cartasAgregar) {
        // vaciamos la pila y luego agregamos carta por carta
        cartas.clear();
        for (CartaInglesa carta : cartasAgregar){
            carta.makeFaceDown(); // volteamos cada carta boca abajo
            // agrega las cartas al tope de la pila
            cartas.push(carta);
        }
    }

    @Override
    public String toString() {
        if (cartas.isEmpty()){
            return "-E-";
        }
        return "@";
    }
}