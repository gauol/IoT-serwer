import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


/**
 * Created by Gaua on 11.04.2017.
 */
public class Main extends JFrame implements ActionListener{

    private JTextArea LogTextField;

    public static void main(String args[]) throws IOException {
        System.out.println("To jest pierwsza linia programu \r\n to jest druga linia");
        new Main().setVisible(true);
    }

    private Main(){
        super("Nazwa Aplikacji");
        setSize(620,200);
        //setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        JButton StartButton = new JButton("Start");
        JButton StopButton = new JButton("Stop");
        //StartButton.setSize(100,100);
       // StopButton.setSize(100,100);
        LogTextField = new JTextArea();
        LogTextField.setBounds(100,0,400,200);
        //LogTextField.setEnabled(false);
        LogTextField.setEditable(false);
        StartButton.setBounds(0,0,100,100);
        StopButton.setBounds(500,0,100,100);
        StartButton.addActionListener(this);
        StopButton.addActionListener(this);
        add(StartButton);
        add(StopButton);
        add(LogTextField);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String name = e.getActionCommand();
        if(name.equals("Start")){
            LogTextField.setText(new Czas().getTime() + "\t Wcisnieto start\n\r"+LogTextField.getText());
            LogTextField.setCaretPosition(LogTextField.getDocument().getLength());
        }else if(name.equals("Stop")){
            LogTextField.setText(new Czas().getTime() + "\t Wcisnieto stop\n\r"+LogTextField.getText());
            LogTextField.setCaretPosition(LogTextField.getDocument().getLength());
        }

    }
}
