import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Cronometro extends JPanel {
    private static final int TIEMPO_INICIAL = 10;
    private int tiempoRestante;
    private Timer timer;
    private JLabel etiquetaTiempo;

    public Cronometro() {
        tiempoRestante = TIEMPO_INICIAL;
        etiquetaTiempo = new JLabel(Integer.toString(tiempoRestante));
        etiquetaTiempo.setFont(new Font("Arial", Font.BOLD, 16));
        add(etiquetaTiempo);
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tiempoRestante--;
                if (tiempoRestante >= 0) {
                    etiquetaTiempo.setText(Integer.toString(tiempoRestante));
                } else {
                    detener();
                    JOptionPane.showMessageDialog(null, "¡Se acabó el tiempo!");
                    TicTacToe ticTacToe = (TicTacToe) SwingUtilities.getWindowAncestor(Cronometro.this);
                    ticTacToe.cambiarTurno();
                }
            }
        });
    }
    public void iniciar() {
        tiempoRestante = TIEMPO_INICIAL;
        etiquetaTiempo.setText(Integer.toString(tiempoRestante));
        timer.start();
    }
    public void detener() {
        timer.stop();
    }
    public void reiniciar() {
        detener();
        iniciar();
    }
}