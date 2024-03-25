public class Tablero {
    protected char casillas[];
    protected int jugadas;

    public Tablero() {
        casillas = new char[9];
        for (int i = 0; i < casillas.length; i++) {
            casillas[i] = '_';
        }
        jugadas = 0;
    }

    public void dibujarTablero() {
        System.out.println("Tablero              Posiciones disponibles");
        System.out.println(" " + casillas[0] + " | " + casillas[1] + " | " + casillas[2] + "            0 | 1 | 2 ");
        System.out.println("-----------          -----------");
        System.out.println(" " + casillas[3] + " | " + casillas[4] + " | " + casillas[5] + "            3 | 4 | 5 ");
        System.out.println("-----------          -----------");
        System.out.println(" " + casillas[6] + " | " + casillas[7] + " | " + casillas[8] + "            6 | 7 | 8 ");
        mostrarJugadasDisponibles();
    }

    public void dibujarEstadoTablero() {
        System.out.println(" " + casillas[0] + " | " + casillas[1] + " | " + casillas[2] + " ");
        System.out.println("-----------");
        System.out.println(" " + casillas[3] + " | " + casillas[4] + " | " + casillas[5] + " ");
        System.out.println("-----------");
        System.out.println(" " + casillas[6] + " | " + casillas[7] + " | " + casillas[8] + " ");
    }

private void mostrarJugadasDisponibles() {
        System.out.println("Jugadas disponibles: " + (9 - jugadas));
    }

    public boolean marcarJugada(int posicion, Jugador x) {
        if (posicion > 8 || posicion < 0)
            return false;
        boolean exito = false;
        if (casillas[posicion] == '_') {
            casillas[posicion] = x.ficha;
            exito = true;
            jugadas++;
        }
        return exito;
    }

    public boolean esGanador(Jugador x) {
        char f = x.ficha;
        if ((casillas[0] == f && casillas[4] == f && casillas[8] == f) ||
            (casillas[2] == f && casillas[4] == f && casillas[6] == f))
            return true;
        for (int i = 0; i < 3; i++) {
            if ((casillas[i] == f && casillas[i + 3] == f && casillas[i + 6] == f) ||
                (casillas[i * 3] == f && casillas[i * 3 + 1] == f && casillas[i * 3 + 2] == f))
                return true;
        }
        return false;
    }

    public boolean hayJugadas() {
        return jugadas < 9;
    }
}