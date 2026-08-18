/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tetrisgame;

 
import javax.swing.*; 
import java.awt.*;                           
import java.awt.event.*;                     
import java.io.*;                            


public class Board extends JPanel implements ActionListener { 
    private final int ROWS = 20; 
    private final int COLS = 10; 
    private final int BLOCK_SIZE = 30; 
 
    private Timer timer;                  
    private Tetromino currentPiece;       
    private Color[][] grid;               
    private boolean gameOver = false; 
    private int score = 0; 
    private int highScore = 0; 
 
    public Board() { 
        setPreferredSize(new Dimension(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE)); 

        setBackground(Color.BLACK);  
        setFocusable(true); 
        addKeyListener(new KeyAdapter() {    
            @Override 
            public void keyPressed(KeyEvent e) {    
                if (!gameOver) { 
                    switch (e.getKeyCode()) {         
                        case KeyEvent.VK_LEFT: 
                            currentPiece.move(-1, 0, grid); 
                            break; 
                        case KeyEvent.VK_RIGHT: 
                            currentPiece.move(1, 0, grid); 
                            break; 
                        case KeyEvent.VK_DOWN: 
                            currentPiece.move(0, 1, grid); 
                            break; 
                        case KeyEvent.VK_UP: 
                            currentPiece.rotate(grid); 
                            break; 
                    } 
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) { 
                    restartGame(); 
                } 
                repaint();    
            } 
        }); 
 
        grid = new Color[ROWS][COLS];   
        loadHighScore();      
        timer = new Timer(500, e -> {    
            if (!gameOver) { 
                if (currentPiece.canMove(0, 1, grid)) {    
                    currentPiece.move(0, 1, grid);         
                } else { 
                    placePiece();                          
                } 
                repaint();                                 
            } 
        }); 

        spawnPiece();         
        timer.start(); 
    } 
 
    private void spawnPiece() {                                 
        currentPiece = Tetromino.getRandomPiece();              
        if (!currentPiece.canMove(0, 0, grid)) {                
            gameOver = true;                                    
            timer.stop();                                       
            saveHighScore();                                     
        } 
    } 


    private void placePiece() {                              
        for (Point p : currentPiece.getBlocks()) {    
    
            grid[p.y][p.x] = currentPiece.getColor();          
        } 
        clearLines();                                 
        spawnPiece();                                 
    } 
 
    private void clearLines() {                      
        for (int row = 0; row < ROWS; row++) {             
            boolean fullLine = true;                        
            for (int col = 0; col < COLS; col++) {          
                if (grid[row][col] == null) {             
                    fullLine = false;                       
                    break; 
                } 
            } 
            if (fullLine) {                                  
                for (int r = row; r > 0; r--) {                              
                    System.arraycopy(grid[r - 1], 0, grid[r], 0, COLS);      
                } 
              

                grid[0] = new Color[COLS];                    
                                                               
                score += 100; 
                if (score > highScore) { 
                    highScore = score; 
                } 
            } 
        } 
    } 
 
    @Override 
    public void actionPerformed(ActionEvent e) {                
       
    } 
    @Override 
    protected void paintComponent(Graphics g) { 
        super.paintComponent(g);                              
        for (int row = 0; row < ROWS; row++) {                
            for (int col = 0; col < COLS; col++) { 
                if (grid[row][col] != null) { 
                    g.setColor(grid[row][col]);               
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);     

                    g.setColor(Color.BLACK); 
                    g.drawRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);         

                } 
            } 
        } 

        if (currentPiece != null) { 
            g.setColor(currentPiece.getColor()); 
            for (Point p : currentPiece.getBlocks()) { 
                g.fillRect(p.x * BLOCK_SIZE, p.y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE); 
                g.setColor(Color.BLACK); 
                g.drawRect(p.x * BLOCK_SIZE, p.y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE); 
                g.setColor(currentPiece.getColor()); 
            } 
        } 
 
        g.setColor(Color.WHITE); 
        g.setFont(new Font("Arial", Font.BOLD, 18)); 
        g.drawString("Score: " + score, 10, 20); 
        g.drawString("High Score: " + highScore, 10, 40); 
 
        if (gameOver) { 
            g.setColor(Color.WHITE); 
            g.setFont(new Font("Arial", Font.BOLD, 28)); 
            g.drawString("Game Over!", 60, 200); 
            g.setFont(new Font("Arial", Font.BOLD, 18)); 
            g.drawString("Press 'SPACE’ to try again", 50, 240); 
        } 
    } 
 
    private void restartGame() { 
        grid = new Color[ROWS][COLS]; 
        score = 0; 
        gameOver = false; 
        spawnPiece(); 
        timer.start(); 
        repaint(); 
    } 
 
    // File Handling Methods 
 
    private void saveHighScore() { 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"))) { 
            writer.write(String.valueOf(highScore)); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
 
    private void loadHighScore() { 
        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) { 
            String line = reader.readLine(); 
            if (line != null) { 
                highScore = Integer.parseInt(line); 
            } 
        } catch (IOException | NumberFormatException e) { 
            highScore = 0; // default if no file or error 
        } 
    } 
}  
 

 
