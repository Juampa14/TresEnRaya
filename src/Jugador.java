public abstract class Jugador {
    String nombre;
    char ficha;

    public Jugador(String n, char f) {
        this.nombre = n;
        this.ficha = f;
    }

    abstract int jugar();
}