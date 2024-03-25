import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TicTacToe extends JFrame {
    private Tablero tablero;
    private JButton[] botones;
    private JLabel mensajeLabel;
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador turno;
    private Cronometro cronometro;
    private JLabel cronometroLabel;

    public TicTacToe() {
        super("Tres en Raya");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLayout(new BorderLayout());

        // Solicitar nombres de los jugadores
        String nombreJugador1 = JOptionPane.showInputDialog(this, "Nombre del Jugador 1:");
        String nombreJugador2 = JOptionPane.showInputDialog(this, "Nombre del Jugador 2:");

        // Inicializar el tablero y los jugadores
        tablero = new Tablero();
        jugador1 = new JugadorPersona(nombreJugador1, 'X');
        jugador2 = new JugadorPersona(nombreJugador2, 'O');
        turno = jugador1;

        // Crear botones para representar el tablero
        JPanel panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(3, 3));
        botones = new JButton[9];
        for (int i = 0; i < 9; i++) {
            botones[i] = new JButton();
            botones[i].setFont(new Font("Arial", Font.PLAIN, 40));
            final int index = i;
            botones[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hacerJugada(index);
                }
            });
            panelTablero.add(botones[i]);
        }

        // Etiqueta para mensajes
        mensajeLabel = new JLabel("Turno de " + turno.nombre);

        // Temporizador
        cronometroLabel = new JLabel("Tiempo restante: ");
        cronometro = new Cronometro();
        cronometro.iniciar(); // Iniciar cronometro al inicio del juego

        // Agregar componentes al marco
        add(panelTablero, BorderLayout.CENTER);
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(mensajeLabel, BorderLayout.NORTH);
        panelInferior.add(cronometroLabel, BorderLayout.CENTER);
        panelInferior.add(cronometro, BorderLayout.SOUTH);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private void hacerJugada(int index) {
        cronometro.detener(); // Detener cronometro al hacer una jugada
        if (tablero.marcarJugada(index, turno)) {
            botones[index].setText(String.valueOf(turno.ficha));
            if (tablero.esGanador(turno)) {
                mostrarMensaje("¡GANASTE! FELICIDADES " + turno.nombre + "!");
                 try {
                        FileWriter escritura = new FileWriter("HistorialDeJuego.txt",true);
                        escritura.write("\nLa partida fue entre "+jugador1.nombre+" VS "+jugador2.nombre+" y el ganador fue "+ turno.nombre);
                        escritura.close();
                    } catch(IOException excepcion) {
                        excepcion.printStackTrace(System.out);
                    }
                reiniciarJuego();
            } else {
                if (!tablero.hayJugadas()) {
                    mostrarMensaje("¡Empate!");
                    try {
                        FileWriter escritura = new FileWriter("HistorialDeJuego.txt",true);
                        escritura.write("\nLa partida fue entre "+jugador1.nombre+" VS "+jugador2.nombre+" y hubo empate");
                        escritura.close();
                    } catch(IOException excepcion) {
                        excepcion.printStackTrace(System.out);
                    }
                    reiniciarJuego();
                } else {
                    cambiarTurno();
                }
            }
        } else {
            mostrarMensaje("Posición ocupada. Por favor, elija otra.");
        }
    }

    private void reiniciarJuego() {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea jugar nuevamente?", "Reiniciar juego", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            tablero = new Tablero();
            for (JButton boton : botones) {
                boton.setText("");
            }
            turno = jugador1;
            mensajeLabel.setText("Turno de " + turno.nombre);
            cronometro.iniciar(); // Iniciar temporizador para el nuevo turno
        }else{
            System.exit(0);
        }
    }

    public void cambiarTurno() {
        cronometro.detener(); // Detener temporizador al cambiar de turno
        turno = (turno == jugador1) ? jugador2 : jugador1;
        mensajeLabel.setText("Turno de " + turno.nombre);
        cronometro.reiniciar(); // Reiniciar temporizador para el nuevo turno
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TicTacToe().setVisible(true);
            }
        });
    }
}