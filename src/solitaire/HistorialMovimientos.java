package solitaire;

//Esta clase guarda el historial de los estados del juego usando la clase pila
//Si se hace un movimiento, se guarda el estado que estaba anteriormente
//si se deshace un movimiento, solo se utiliza el [ultimo estado guardado

public class HistorialMovimientos{

    //la piila de estados anteriores del juego
    private Pila<EstadoJuego> historial = new Pila<>();

    //Guarda el estado actual antes de hacer un movimiento
    public void guardar(EstadoJuego estado){
        historial.push(estado);
    }

    //Regresa el estado anterior (para deshacer)
    public EstadoJuego deshacer(){
        return historial.pop();
    }

    //ES el que dice si hay movimientos que se pueden deshacer
    public boolean hayMovimientos(){
        return !historial.isEmpty();
    }

    //Vacía el historial (para cuadno se inicie una partida nueva) >:) nyehehe
    public void limpiar(){
        historial.clear();
    }
}