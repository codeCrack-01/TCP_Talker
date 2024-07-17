package TCP_Program;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSide implements ActionListener {

    JFrame frame;
    JPanel panel;
    JTextField inputField;
    JButton sendButton;

    TitledBorder tb;
    Font myFont = new Font("Roboto", Font.BOLD, 16); Font inFont = new Font("Cooper Black", Font.BOLD, 27);

    static JTextArea consoleText;
    public String clientName;

    BufferedReader in_socket;
    PrintWriter out_socket;

    public ClientSide() throws Exception {

        tb = BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(1, Color.GRAY, Color.DARK_GRAY), "Terminal");
        tb.setTitleColor(Color.LIGHT_GRAY);

        frame = new JFrame("Client Window");
        frame.setResizable(false);

        frame.setBounds(120,100,600, 350);
        frame.setLayout(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusable(false);

        panel = new JPanel();
        panel.setBounds(10, 90, 565, 210);

        panel.setBorder(tb);
        panel.setBackground(Color.BLACK);
        // =============================================== //

        inputField = new JTextField();
        inputField.setBounds(15, 30, 500, 50);

        inputField.setFont(inFont);

        // =============================================== //

        consoleText = new JTextArea();
        consoleText.setBounds(10,90,560,100);

        consoleText.setEditable(false);
        consoleText.setText("Client Console : \n");

        for (int i = 0; i < 135; i++) {
            consoleText.setText(consoleText.getText() + "-");
        }

        consoleText.setBackground(new Color(24, 24,24));
        consoleText.setForeground(new Color(15, 120, 30));

        JScrollPane sp = new JScrollPane(consoleText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setPreferredSize(new Dimension(550, 175));

        // =============================================== //

        sendButton = new JButton("»");
        sendButton.setBounds(520, 30, 55, 50);

        sendButton.addActionListener(this);
        sendButton.setFont(myFont);

        sendButton.setFocusable(false);

        // =============================================== //

        panel.add(sp);

        frame.add(sendButton);
        frame.add(inputField);

        frame.add(panel);
        frame.setVisible(true);

        // ================================================================================== //

        Socket socket = new Socket("localhost", 2023); // Enter your own IP Address (of Server Machine)

        // I/O Streams
        in_socket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out_socket = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        // ==========================================================//
        clientName = JOptionPane.showInputDialog ("Enter your name :");
        out_socket.println(clientName);
        // ==========================================================//

        consoleText.setText(consoleText.getText() + "\nSuccessful connection to the server ...\n");

        //socket.close();
        //System.out.println("Socket is closed... ");

        // ================================================================================== //

        for (int i = 0; i < 1; i--) {
            String messageFromServer = in_socket.readLine();
            consoleText.setText(consoleText.getText() + "\nServer says : " + messageFromServer);

            if (i <= -25)
                i = 0;
        }
    }

    public static void main(String[] args) {
        try {
            ClientSide client = new ClientSide();
        }
        catch (Exception e) {
            e.printStackTrace();
            ErrorMessageToConsole();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == sendButton) {
            String message = inputField.getText();
            inputField.setText("");

            consoleText.setText(consoleText.getText() + "\nYou : " + message);
            out_socket.println(message);
        }
    }

    public static void ErrorMessageToConsole () {
        consoleText.setText(consoleText.getText() + "\n INVALID ERROR ! Please make sure server is running...");
    }
}
