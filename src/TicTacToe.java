import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class TicTacToe extends JFrame {
    
    private static final String RANKING_FILE = "HistorialDeJuego.txt";
    private Map<String, Integer> victoriasJugadores;
    private ArrayList<String> rankingJugadores;
    private Tablero tablero;
    private JPanel panelTablero;
    private JButton[] botones;
    private JLabel mensajeLabel;
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador turno;
    private Cronometro cronometro;
    private JLabel cronometroLabel;
    private JTextArea rankingTextArea;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                TicTacToe juego = new TicTacToe();
                juego.mostrarMenu();
                juego.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        juego.guardarRanking();
                    }
                });
                juego.setLocationRelativeTo(null);
            }
        });
    }

    public TicTacToe() {
        super("TresEnRaya");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400); // Cambiamos el tamaño para que se ajuste el ranking
        setLayout(new BorderLayout());

        victoriasJugadores = new HashMap<>();
        rankingJugadores = new ArrayList<>();

        cronometroLabel = new JLabel("Tiempo restante: ");
        cronometro = new Cronometro();

        cargarRanking();
    }

    private void hacerJugada(int index) {
    if (tablero.marcarJugada(index, turno)) {
        cronometro.detener();
        // Cambiar el color de las fichas
        if (turno.ficha == 'X') {
            botones[index].setForeground(Color.RED); // Para 'X'
        } else {
            botones[index].setForeground(Color.BLUE); // Para 'O'
        }
        botones[index].setText(String.valueOf(turno.ficha));
        if (tablero.esGanador(turno)) {
            mostrarMensaje("¡GANASTE! FELICIDADES " + turno.nombre + "!");
            registrarPartida(turno);
            actualizarRanking();
            reiniciarJuego();
        } else {
            if (!tablero.hayJugadas()) {
                mostrarMensaje("¡Empate!");
                registrarPartida(null);
                actualizarRanking();
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
            //System.exit(0);
            return;
        }
    }


    private void crearTablero() {
        panelTablero = new JPanel();
        panelTablero.setLayout(new GridLayout(3, 3));
        botones = new JButton[9];
        for (int i = 0; i < 9; i++) {
            botones[i] = new JButton();
            botones[i].setFont(new Font("Arial", Font.BOLD, 40));
            botones[i].setBackground(Color.WHITE);
            final int index = i;
            botones[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hacerJugada(index);
                }
            });
            panelTablero.add(botones[i]);
        }

        mensajeLabel = new JLabel("Turno de " + turno.nombre);
        
        // Botón para cerrar el juego
        JButton cerrarJuegoButton = new JButton("Cerrar Juego");
        cerrarJuegoButton.setBackground(Color.RED);
        cerrarJuegoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirmacion = JOptionPane.showConfirmDialog(TicTacToe.this, "¿Estás seguro que deseas salir del juego?", "Cerrar Juego", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    guardarRanking();
                    System.exit(0);
                }
            }
        });
        
        // Botón para actualizar ranking
        JButton rankingButton = new JButton("Actualizar Ranking");
        rankingButton.setBackground(Color.CYAN);
        rankingButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            mostrarRanking();
           }
        });
        
        // Crear panel para el ranking
        JPanel panelRanking = new JPanel(new BorderLayout());

        // Crear el título del ranking y establecer la alineación
        JLabel tituloRanking = new JLabel("Ranking de jugadores", SwingConstants.CENTER);
        tituloRanking.setFont(new Font("Arial", Font.BOLD, 16));

        // Agregar el título al centro del panel
        panelRanking.add(tituloRanking, BorderLayout.NORTH);

        // Agregar el JTextArea dentro de un JScrollPane
        rankingTextArea = new JTextArea();
        JScrollPane scrollPaneRanking = new JScrollPane(rankingTextArea);
        scrollPaneRanking.setPreferredSize(new Dimension(200, getHeight())); // Establecer el ancho del ranking

        // Agregar el JScrollPane al panel
        panelRanking.add(scrollPaneRanking, BorderLayout.CENTER);

        // Dividir el contenedor en dos paneles (tablero y ranking) usando JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelTablero, panelRanking);
        splitPane.setResizeWeight(0.75); // Establecer el ancho inicial del juego
        add(splitPane, BorderLayout.CENTER);

        
        // Panel inferior para el mensaje, cronómetro y botón de cerrar juego
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(mensajeLabel, BorderLayout.NORTH);
        panelInferior.add(cronometroLabel, BorderLayout.CENTER);
        panelInferior.add(cronometro, BorderLayout.SOUTH);
        panelInferior.add(cerrarJuegoButton, BorderLayout.EAST); // Añadir el botón de cierre al este
        add(panelInferior, BorderLayout.SOUTH);
        panelInferior.add(rankingButton, BorderLayout.WEST); // Añadir el botón de ranking al oeste
        add(panelInferior, BorderLayout.SOUTH);
    
        cronometro.iniciar();
        tablero = new Tablero();
        mostrarRanking();

    }

    void cambiarTurno() {
        cronometro.detener();
        turno = (turno == jugador1) ? jugador2 : jugador1;
        mensajeLabel.setText("Turno de " + turno.nombre);
        cronometro.reiniciar();
    }

    private void mostrarMenu() {
    JDialog menuDialog = new JDialog(this, "Menú", true);
    menuDialog.setSize(400, 400);
    menuDialog.setLayout(new GridLayout(2, 1));

    JButton jugarPartidaButton = new JButton("Empezar Partida");
    jugarPartidaButton.setFont(new Font("Arial", Font.BOLD, 25));
    jugarPartidaButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            menuDialog.dispose();
            solicitarNombres();
        }
    });
    menuDialog.add(jugarPartidaButton);

    JButton cerrarJuegoButton = new JButton("Cerrar Juego");
    cerrarJuegoButton.setFont(new Font("Arial", Font.BOLD, 25));
    cerrarJuegoButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            int confirmacion = JOptionPane.showConfirmDialog(TicTacToe.this, "¿Estás seguro que deseas salir del juego?", "Cerrar Juego", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                guardarRanking();
                System.exit(0);
            }
        }
       });
    
    menuDialog.add(cerrarJuegoButton);
    
    menuDialog.setLocationRelativeTo(null);
    menuDialog.setVisible(true);
    }
    
    private void solicitarNombres() {
        String nombreJugador1 = JOptionPane.showInputDialog(this, "Nombre del Jugador 1:");
        String nombreJugador2 = JOptionPane.showInputDialog(this, "Nombre del Jugador 2:");

        jugador1 = new JugadorPersona(nombreJugador1, 'X');
        jugador2 = new JugadorPersona(nombreJugador2, 'O');
        turno = jugador1;

        crearTablero();
        setVisible(true);
    }

    private void mostrarRanking() {
    try (BufferedReader reader = new BufferedReader(new FileReader(RANKING_FILE))) {
        StringBuilder ranking = new StringBuilder();
        String linea;
        while ((linea = reader.readLine()) != null) {
            ranking.append(linea).append("\n");
        }
        rankingTextArea.setText(ranking.toString()); // Asigna el texto al JTextArea
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error al leer el archivo de ranking", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void registrarPartida(Jugador ganador) {
        if (ganador != null) {
            int victorias = victoriasJugadores.getOrDefault(ganador.nombre, 0);
            victoriasJugadores.put(ganador.nombre, victorias + 1);
            guardarRanking();
        }   
    }

    private void actualizarRanking() {
        rankingJugadores.clear();
        rankingJugadores.addAll(victoriasJugadores.keySet());
        rankingJugadores.sort(Comparator.comparingInt(victoriasJugadores::get).reversed());
        guardarRanking();
    }

    private void cargarRanking() {
        try (BufferedReader reader = new BufferedReader(new FileReader(RANKING_FILE))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(": ");
                if (partes.length == 2) {
                    String jugador = partes[0];
                    int victorias = Integer.parseInt(partes[1]);
                    victoriasJugadores.put(jugador, victorias);
                    rankingJugadores.add(jugador);
                }
            }
            
            actualizarRanking();
        } catch (IOException e) {
            // Manejar el error de lectura del archivo
        }
    }

    private void guardarRanking() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(RANKING_FILE))) {
            for (String jugador : rankingJugadores) {
                writer.println(jugador + ": " + victoriasJugadores.get(jugador));
            }
        } catch (IOException e) {
            // Manejar el error de escritura del archivo
        }
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}