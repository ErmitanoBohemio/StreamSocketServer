/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 *
 * @author OscarFabianHP
 */
//Fig. 28.3: Server.java 
//Server portion of a client/server stream-socket connection

public class Server extends JFrame {
    
    private JTextField enterField; //inputs message from user
    private JTextArea displayArea; //display information to user
    
    private ObjectOutputStream output; //output stream to client
    private ObjectInputStream input; //input stream from client 
    private ServerSocket server; //server socket
    private Socket connection; //connection to client
    private int counter = 1; //counter of number of connections

    //set up GUI
    public Server() {
        super("Server");
        
        enterField = new JTextField(); //create enterField
        enterField.setEditable(true);
        
        //when the user of the server application enters a String in the text field and presses the Enter key
        enterField.addActionListener(new ActionListener() {
            //send message to client 
            @Override
            public void actionPerformed(ActionEvent event) {
                sendData(event.getActionCommand()); //read the String from the text field and calls utility method sendData to send the String to the client
                enterField.setText("");
            }
        });
        add(enterField, BorderLayout.NORTH);
        
        displayArea = new JTextArea(); //create displayArea
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        
        setSize(300, 150); //set size of window
        setVisible(true); //show window
    }
    
    //set up and run server
    //server receives a connection, processes it, closes it and waits for the next connection
    public void runSever(){
        try{
            server = new ServerSocket(12345, 100); //create ServerSocket
            
            while(true){
                try{
                    waitForConnection(); //wait for a connection
                    getStreams(); //get input and output streams
                    processConnections(); //process connection
                }
                catch(EOFException eofException){
                     displayMessage("\nServer terminated connection");
                }
                finally {
                    closeConnection(); //close connection
                    ++counter;
                }
            }
        }
        catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    //wait for connection to arrive, then display connection info
    private void waitForConnection() throws IOException {
        displayMessage("waiting for connection...\n");
        connection = server.accept(); //allow server accept connection
        displayMessage("Connection " + counter + "received from: " + connection.getInetAddress().getHostName());
    }

    //get streams to send and receive data
    private void getStreams() throws IOException {
        
        //always create the ObjectOutputStream first and flush the stream so that the client's ObjectInputStream can prepare to receive the data
        //set up output stream for objects
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush(); //cause the ObjectOutputStream on the server to send a stream header to the corresponding client's ObjectInputStream
                        //this information is required by the ObjectInputStream so that it can prepare to receive those objects correctly
        //set up input stream for objects
        input = new ObjectInputStream(connection.getInputStream());
        
        displayMessage("\nGot I/O streams\n");
    }

    //process connection with client
    private void processConnections() throws IOException {
        String message = "Connection Successful";
        sendData(message); //send connection successful message
        
        //enable enterField so server user can send messages
        setTextFieldEditable(true);
        
        do{  //process messages sent from client
            try { //read message and display it
                
            message = (String) input.readObject(); //read new message
            displayMessage("\n" + message); //display message
            
            } 
            catch (ClassNotFoundException ex) {
                displayMessage("\nUnknown object type received");
            }
        }
        while(!message.equals("CLIENT>>> TERMINATE"));
    }

    //manipulates enterField in the event-dispatch thread (this ensures that we dont modify a GUI component from a thread 
    //other than the event-dispatch thread, which is important since Swing GUI components are not thread safe)
    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(new Runnable() {
            //updates displayArea
            @Override
            public void run() {
                displayArea.append(messageToDisplay); //append message
            }
        });
    }
    
    //close streams and socket
    private void closeConnection(){
        displayMessage("\nTerminating connection\n");
        setTextFieldEditable(false); //disable enterField
        
        try {
            output.close(); //close output stream
            input.close(); //close input stream
            connection.close(); //close socket
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        
    }
    
    //send message to client
    //method sendData writes the object, flushes the output buffer and appends the same String to the text area in the server windows
     private void sendData(String message) {
         try{
         output.writeObject("SERVER>>> "+message);
         output.flush(); //flush output to client //cause the ObjectOutputStream on the server to send a stream header to the corresponding client's ObjectInputStream
         displayMessage("\nSERVER>>> "+message);
         }
         catch(IOException ioException){
             displayArea.append("Error writing object");
         }
     }

     
    //manipulates enterField in the event-dispatch thread (this ensures that we dont modify a GUI component from a thread 
    //other than the event-dispatch thread, which is important since Swing GUI components are not thread safe)
    private void setTextFieldEditable(final boolean editable) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() { //set enterField's editability
                enterField.setEditable(editable);
            }
        });
    }
    
}
