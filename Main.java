import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


/**
 * Created by Gaua on 11.04.2017.
 */
public class Main extends JFrame implements ActionListener{

    private JTextArea LogTextField;
    private JCheckBox SaveCheckBox;
    private static Server srv;
    private static Main mn;
    public static void main(String args[]) throws IOException {
        Server.print("To jest pierwsza linia programu \r\n to jest druga linia");
        mn = new Main();
    }

    private Main(){
        super("IoT Serwer");
        setSize(600,600);
        //setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        JButton StartButton = new JButton("Dodaj Sensor");
        JButton StopButton = new JButton("Usuń Sensor");
        SaveCheckBox = new JCheckBox("Zapisac zmiany?");
        SaveCheckBox.setBounds(0, 200, 150,50);
        LogTextField = new JTextArea();
        LogTextField.setBounds(150,0,400,600);
        //LogTextField.setEnabled(false);
        LogTextField.setEditable(false);
        StartButton.setBounds(0,0,150,50);
        StopButton.setBounds(0,100,150,50);
        StartButton.addActionListener(this);
        StopButton.addActionListener(this);
        add(StartButton);
        add(StopButton);
        add(SaveCheckBox);
        add(LogTextField);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(mn,
                        "Are you sure to close this window?", "Really Closing?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                    if (SaveCheckBox.isSelected())
                            Server.jdb.closeStatements();
                    Server.jdb.closeDatabase();
                    System.exit(0);
                }
            }
        });
        setVisible(true);
        srv = new Server(90, LogTextField);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String name = e.getActionCommand();
        Server.print(e.getActionCommand());
        if(name.equals("Start")){
            LogTextField.setText(new Czas().getTime() + "\t Wcisnieto start\n\r"+LogTextField.getText());
            LogTextField.setCaretPosition(LogTextField.getDocument().getLength());
        }else if(name.equals("Stop")){
            LogTextField.setText(new Czas().getTime() + "\t Wcisnieto stop\n\r"+LogTextField.getText());
            LogTextField.setCaretPosition(LogTextField.getDocument().getLength());
        }

    }
}
