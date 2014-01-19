package UserInterface;

import UserInterface.abstractInterface.Dialog;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class HelpDialogSwing extends JFrame implements Dialog {

    private final String text;

    public HelpDialogSwing() throws HeadlessException {
        super();
        this.text = "Este juego consiste en despejar todas las casillas de la pantalla que no oculten una mina.\n"
                + "Algunas casillas tienen un número, este número indica las minas que suman todas las casillas circundantes. \n"
                + "Así, si una casilla tiene el número 3, significa que de las ocho casillas que hay alrededor (si no es en una esquina o borde) "
                + "hay 3 con minas y 5 sin minas. \n"
                + "Si se descubre una casilla sin número indica que ninguna de las casillas vecinas tiene mina y estas se descubren automáticamente.\n"
                + "Si se descubre una casilla con una mina se pierde la partida.";
    }

    @Override
    public void showDialog() {
        JOptionPane.showMessageDialog(this, text, "How to play", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void hidDialog() {
    }
}
