import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


/**
 * Created by Gaua on 11.04.2017.
 */
public class Main extends JFrame implements ActionListener{

    private static JTextArea LogTextField;
    private JCheckBox SaveCheckBox;
    private static Server srv;

    private static JTextField AddTextField;
    private static JTextField DeleteTextField;
    private static JScrollPane scroll;

    public static void main(String args[]) throws IOException {
        Server.print("To jest pierwsza linia programu \r\n to jest druga linia");
        Server.mn = new Main();
    }

    public Main(){
        super("IoT Serwer");
        setSize(400,200);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(null);

        JButton AddButton = new JButton("Dodaj Sensor");
        AddButton.setBounds(220,10,150,50);
        AddButton.addActionListener(this);
        add(AddButton);

        AddTextField = new JTextField();
        AddTextField.setBounds(220,60,150,20);
        add(AddTextField);

        JButton DeleteButton = new JButton("Usuń Sensor");
        DeleteButton.setBounds(220,100,150,50);
        DeleteButton.addActionListener(this);
        add(DeleteButton);

        DeleteTextField = new JTextField();
        DeleteTextField.setBounds(220,150,150,20);
        add(DeleteTextField);

        JButton LogButton = new JButton("Pokaż Log");
        LogButton.setBounds(10,10,150,50);
        LogButton.addActionListener(this);
        add(LogButton);

        SaveCheckBox = new JCheckBox("Zapisac zmiany w bazie?");
        SaveCheckBox.setBounds(10, 100, 200,50);
        add(SaveCheckBox);

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(Server.mn,
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
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String name = e.getActionCommand();
        Server.print(e.getActionCommand());
        switch (name) {
            case "Dodaj Sensor":
                if (LogTextField != null)
                    LogTextField.setText(new Czas().getTime() + "\t Wcisnieto start\n\r" + LogTextField.getText());
                break;
            case "Usuń Sensor":
                if (LogTextField != null)
                    LogTextField.setText(new Czas().getTime() + "\t Wcisnieto stop\n\r" + LogTextField.getText());
                break;
            case "Pokaż Log":
                if (LogTextField != null)
                    LogTextField.setText(new Czas().getTime() + "\t Pokaż Log\n\r" + LogTextField.getText());
                createLogWindow();
                break;
        }

    }

    private static void createLogWindow(){
        JFrame frame = new JFrame ("Log");
        frame.setSize(500,500);
        frame.setResizable(true);
        LogTextField = new JTextArea();
        LogTextField.setSize(400,400);
        LogTextField.setLineWrap(true);
        LogTextField.setEditable(false);
        LogTextField.setVisible(true);
        Server.LogTextField = LogTextField;
        JScrollPane scroll = new JScrollPane (LogTextField);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scroll);
        Point spawnPoint = Server.mn.getLocation();
        spawnPoint.setLocation(spawnPoint.getX()+400,spawnPoint.getY());
        frame.setLocation(spawnPoint);
        frame.setVisible(true);

    }
}
