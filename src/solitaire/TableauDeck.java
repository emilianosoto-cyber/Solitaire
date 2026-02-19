package solitaire;

import DeckOfCards.CartaInglesa;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Modela un montículo donde se ponen las cartas
 * de por valor, alternando el color.
 *
 * @author Cecilia M. Curlango
 * @version 2025
 */
public class TableauDeck {
    ArrayList<CartaInglesa> cartas = new ArrayList<>();

    /**
     * Carga las cartas iniciales y voltea la última.
     *
     * @param cartas iniciales
     */
    public void inicializar(ArrayList<CartaInglesa> cartas) {
        this.cartas = cartas;
        // voltear la última carta recibida
        CartaInglesa ultima = cartas.getLast();
        ultima.makeFaceUp();
    }

    /**
     * Remove cards starting from the one with a specified value.
     *
     * @param value of starting card to remove
     * @return removed cards or empty ArrayList if it is not possible to remove.
     */
    public ArrayList<CartaInglesa> removeStartingAt(int value) {
        ArrayList<CartaInglesa> removed = new ArrayList<>();
        Iterator<CartaInglesa> iterator = cartas.iterator();
        while (iterator.hasNext()) {
            CartaInglesa next = iterator.next();
            if (next.isFaceup()) {
                if (next.getValor() <= value) {
                    removed.add(next);
                    iterator.remove();
                }
            }
        }
        return removed;
    }

    public CartaInglesa viewCardStartingAt(int value) {
        CartaInglesa cartaConElValorDeseado = null;
        for (CartaInglesa next : cartas) {
            if (next.isFaceup()) {
                if (next.getValor() <= value) {
                    cartaConElValorDeseado = next;
                    break;
                }
            }
        }
        return cartaConElValorDeseado;
    }

    public boolean agregarCarta(CartaInglesa carta) {
        boolean agregado = false;

        if (sePuedeAgregarCarta(carta)) {
            carta.makeFaceUp();
            cartas.add(carta);
            agregado = true;
        }
        return agregado;
    }

    CartaInglesa verUltimaCarta() {
        CartaInglesa ultimaCarta = null;
        if (!cartas.isEmpty()) {
            ultimaCarta = cartas.getLast();
        }
        return ultimaCarta;
    }

    CartaInglesa removerUltimaCarta() {
        CartaInglesa ultimaCarta = null;
        if (!cartas.isEmpty()) {
            ultimaCarta = cartas.getLast();
            cartas.remove(ultimaCarta);
            if (!cartas.isEmpty()) {
                // voltea la siguiente carta del tableau
                cartas.getLast().makeFaceUp();
            }
        }
        return ultimaCarta;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (cartas.isEmpty()) {
            builder.append("---");
        } else {
            for (CartaInglesa carta : cartas) {
                builder.append(carta.toString());
            }
        }
        return builder.toString();
    }

    public boolean agregarBloqueDeCartas(ArrayList<CartaInglesa> cartasRecibidas) {
        boolean resultado = false;

        if (!cartasRecibidas.isEmpty()) {
            CartaInglesa primera = cartasRecibidas.getFirst();
            if (sePuedeAgregarCarta(primera)) {
                cartas.addAll(cartasRecibidas);
                resultado = true;
            }
        }
        return resultado;
    }

    public boolean isEmpty() {
        return cartas.isEmpty();
    }

    /**
     * Verifica si la carta que recibe puede ser la siguiente del tableau actual.
     *
     * @param cartaInicialDePrueba
     * @return true si puede ser la siguiente, false si no
     */
    public boolean sePuedeAgregarCarta(CartaInglesa cartaInicialDePrueba) {
        boolean resultado = false;
        if (!cartas.isEmpty()) {
            CartaInglesa ultima = cartas.getLast();
            if (!ultima.getColor().equals(cartaInicialDePrueba.getColor())) {
                if (ultima.getValor() == cartaInicialDePrueba.getValor() + 1) {
                    resultado = true;
                }
            }
        } else {
            // Está vacio el tableau, solo se puede agregar la carta si es rey (K)
            if (cartaInicialDePrueba.getValor() == 13) {
                resultado = true;
            }
        }
        return resultado;
    }

    public CartaInglesa getUltimaCarta() {
        CartaInglesa ultimaCarta = null;
        if (!cartas.isEmpty()) {
            ultimaCarta = cartas.getLast();
        }
        return ultimaCarta;
    }

    public ArrayList<CartaInglesa> getCards() {
        return cartas;
    }
}