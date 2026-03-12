package solitaire;

import java.util.ArrayList;

//Clase que implementa una Pila
public class Pila<T> {

    //ArrayList para guardar los elementos
    private ArrayList<T> elementos = new ArrayList<>();

    //Agrega un elemento al tope de la pila
    public void push(T elemento){
        elementos.add(elemento);
    }

    //Quita el elemento del tope
    public T pop(){
        if (isEmpty()){
            //null si esta vac[ia
            return null;
        }

        //tope
        return elementos.remove(elementos.size() - 1);
    }

    //ve el elemento del tope sin quitarlo
    public T peek(){
        if (isEmpty()){
            return null;
        }
        return elementos.get(elementos.size() - 1);
    }

    //Dice si la pila está vacía
    public boolean isEmpty(){
        return elementos.isEmpty();
    }

    //Regresa cuántos elementos hay en la pila
    public int size(){
        return elementos.size();
    }

    //Vacía la pila por completo
    public void clear(){
        elementos.clear();
    }
}