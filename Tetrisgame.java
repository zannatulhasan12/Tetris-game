/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tetrisgame;

import javax.swing.*;    
 
public class Tetrisgame {              
    public static void main(String[] args) { 
        
        SwingUtilities.invokeLater(() -> {                                                              
                                                                       
            JFrame frame = new JFrame("Tetris Game");                  
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            
            frame.getContentPane().add(new Board());                   
            frame.pack();                                              
            frame.setLocationRelativeTo(null);                         
            frame.setVisible(true);                                    
        }); 
    } 
} 

