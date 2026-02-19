package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

import java.util.ArrayList;

public class FoundationDeck {
    private Palo palo;
    ArrayList<CartaInglesa> cartas = new ArrayList<>();

    public FoundationDeck(Palo palo) {
        this.palo = palo;
    }

    public FoundationDeck(CartaInglesa carta) {
        palo = carta.getPalo();
        if (carta.getValorBajo() == 1) {
            cartas.add(carta);
        }
    }

    public boolean agregarCarta(CartaInglesa carta) {
        boolean agregado = false;
        if (carta.tieneElMismoPalo(palo)) {
            if (cartas.isEmpty()) {
                if (carta.getValorBajo() == 1) {
                    cartas.add(carta);
                    agregado = true;
                }
            } else {
                CartaInglesa ultimaCarta = cartas.getLast();
                if (ultimaCarta.getValorBajo() + 1 == carta.getValorBajo()) {
                    cartas.add(carta);
                    agregado = true;
                }
            }
        }
        return agregado;
    }

    CartaInglesa removerUltimaCarta() {
        CartaInglesa ultimaCarta = null;
        if (!cartas.isEmpty()) {
            ultimaCarta = cartas.getLast();
            cartas.remove(ultimaCarta);
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

    public boolean estaVacio() {
        return cartas.isEmpty();
    }

    public CartaInglesa getUltimaCarta() {
        CartaInglesa ultimaCarta = null;
        if (!cartas.isEmpty()) {
            ultimaCarta = cartas.getLast();
        }
        return ultimaCarta;
    }

    public Palo getPalo() {
        return palo;
    }
}