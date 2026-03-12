package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

import java.util.ArrayList;

//ESTA CLASE GUARDA UNA COPIA DEL JUEGO, PARA QUE DESPUES SE PUEDA DESHACER MOVIMEINTOS
public class EstadoJuego{

    //Se hace una copia del DrawPile
    private ArrayList<CartaInglesa> draw;
    //Se hace ahora una copia del WastePile
    private ArrayList<CartaInglesa> waste;
    //Se hace una copia de cada columna en el tablero
    private ArrayList<ArrayList<CartaInglesa>> tableau;
    //Se hace una copia de cada foundation
    private ArrayList<ArrayList<CartaInglesa>> foundations;

    public EstadoJuego(ArrayList<CartaInglesa> draw, ArrayList<CartaInglesa> waste, ArrayList<ArrayList<CartaInglesa>> tableau, ArrayList<ArrayList<CartaInglesa>> foundations){

        this.draw = draw;
        this.waste = waste;
        this.tableau = tableau;
        this.foundations = foundations;
    }

    public ArrayList<CartaInglesa> getDraw(){
        return draw;
    }

    public ArrayList<CartaInglesa> getWaste(){
        return waste;
    }

    public ArrayList<ArrayList<CartaInglesa>> getTableau(){
        return tableau;
    }

    public ArrayList<ArrayList<CartaInglesa>> getFoundations(){
        return foundations;
    }

}