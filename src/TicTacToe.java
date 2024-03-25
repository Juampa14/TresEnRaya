import java.util.Scanner;
public class TicTacToe {
    public static void main(String[] args) {
        Tablero t = new Tablero();
        Jugador a;
        Jugador b;
        Jugador turno;

        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese su nombre: ");
        String nombre = sc.nextLine();

        a = new JugadorPersona(nombre, 'X');
        
        Scanner sca = new Scanner(System.in);
        System.out.print("Ingrese su nombre: ");
        String nom = sca.nextLine();
        b = new JugadorPC(nom, 'O');

        turno = a;

        boolean seguir = t.hayJugadas();
        while (seguir) {
            limpiarConsola();
            System.out.println("Jugador 1 " + a.nombre + " ficha: " + a.ficha);
            System.out.println("Jugador 2 " + b.nombre + " ficha: " + b.ficha);
            System.out.println("======================");
            t.dibujarTablero();

            System.out.println("Turno del Jugador " + turno.nombre);
            int jugada = turno.jugar();

            if (t.marcarJugada(jugada, turno)) {
                if (t.esGanador(turno)) {
                    limpiarConsola();
                    t.dibujarTablero();
                    System.out.println("\n-------------------------------");
                    System.out.println("¡GANASTE!!! FELICIDADES " + turno.nombre + "!");
                    System.out.println("-------------------------------");
                    seguir = false;
                } else {
                    turno = (turno == a) ? b : a;
                    seguir = t.hayJugadas();
                }
            } else {
                System.out.println("Posición ocupada. Por favor, elija otra.");
            }

            if (!seguir) {
                System.out.println("¿Desea jugar nuevamente? (s/n)");
                String r = sc.next();
                if (r.equalsIgnoreCase("s")) {
                    seguir = true;
                    t = new Tablero();
                    Jugador tmp = a;
                    a = b;
                    b = tmp;
                    turno = a;
                }
            }
        }
        sc.close();
    }
    public static void limpiarConsola() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}