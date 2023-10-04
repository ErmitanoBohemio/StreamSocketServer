/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import javax.swing.JFrame;

/**
 *
 * @author OscarFabianHP
 */
public class ServerTest {
    public static void main(String[] args) {
        Server application = new Server();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.runSever();
    }
}
