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
                repaint();     // repaint() likhle, internally paintComponent(Graphics g) method ke call kore, abar notun kore sob draw hoy
            } 
        }); 
 
        grid = new Color[ROWS][COLS];    //1 ta 2d array ,potita ghor e ekta rong thakbe,na thakle null
        loadHighScore();      // ei method ta ager highscore ta niye ashe
 
        // Use lambda expression for timer action 
        timer = new Timer(500, e -> {    //pottek 500 ms e block gula niche porbe,
            if (!gameOver) { 
                if (currentPiece.canMove(0, 1, grid)) {    //check kore 1 dhap niche namte parbe kina
                    currentPiece.move(0, 1, grid);         //namle namabo
                } else { 
                    placePiece();                          //jodi na pare, arekta piece asbe
                } 
                repaint();                                 //abar draw kore new display te ane
            } 
        }); 
// timer = new Timer(500, new ActionListener() {
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        if (!gameOver) {
        spawnPiece();          //notun ekta tetrimino piece toiri kore, ja upor theke porte thake,current piece hisebe set kore
        timer.start(); 
    } 
 
    private void spawnPiece() {                                 //er kaj notun tetromino piece toiri kora,current piece e set kore
        currentPiece = Tetromino.getRandomPiece();              //tetromino theke get random piece ke call kore I,L,T,O,Z,S
        if (!currentPiece.canMove(0, 0, grid)) {                //eta check korche block ta starting position e boshano sombhov kina,
            gameOver = true;                                    //na boshle game over
            timer.stop();                                       //timer o off
            saveHighScore();                                    // Save high score on game over   
        } 
    } 
// Ei method ta lage jokhon ekta piece ar niche namte pare na.
//    Tokhon amra oita ke permanently board e boshai, tarpor check kori kono full line ase kina. 
//            Jodi thake, clear kori, score barai, ebong notun ekta piece spawn kori.

    private void placePiece() {                               //current piece ke permanent kore boshay,jodi purno line thake oita clear kore
        for (Point p : currentPiece.getBlocks()) {    
            //currentPiece.getBlocks() method ta oi piece er 4 ta block er location (x, y) return kore.
//for loop ta oi 4 ta block er upor cholay, jate ami board er grid e oi location e color set korte pari.
            grid[p.y][p.x] = currentPiece.getColor();          //oi block tate color boshay
        } 
        clearLines();                                 //horizantal ekta line clear kore, uporer dhap niche neme ashe
        spawnPiece();                                 //abar arekta block pora shuru kore
    } 
 
    private void clearLines() {                      
        for (int row = 0; row < ROWS; row++) {              //pottekta row check kore
            boolean fullLine = true;                        //dhore nei purno line ache
            for (int col = 0; col < COLS; col++) {          //pottekta colum check kore
                if (grid[row][col] == null) {               //jodi kono colum khali thake
                    fullLine = false;                       //purno line false dhore break kori
                    break; 
                } 
            } 
            if (fullLine) {                                  
                for (int r = row; r > 0; r--) {                              //️ Eita loop chalay row theke uporer dike (backward)
                    System.arraycopy(grid[r - 1], 0, grid[r], 0, COLS);      //️ Row er upor er sob gula line niche copy kore.
                } 
                // System.arraycopy(grid[r - 1], 0, grid[r], 0, COLS);
//"row r-1 theke shuru kore, sob column (0 to COLS) copy kore row r te boshaw"
// Eitar fole jeta hoy — current full line ta delete hoye jay, ar upor er sob kichu 1 row kore niche neme ase.
                //Eta check kore je current row ta shob column filled kina (mane kono null nai).

                grid[0] = new Color[COLS];                     //grid[0] = new Color[COLS];
                                                               // Top er row (row 0) ekhon empty hoye jay.
                score += 100; 
                if (score > highScore) { 
                    highScore = score; 
                } 
            } 
        } 
    } 
 
    @Override 
    public void actionPerformed(ActionEvent e) {                //Actionlistener interface er method
        // Not used anymore since lambda timer used             //jokhn timer call kori tokhn automatic ei method call kore
    } 
 //Je je block already niche pore geche (fixed blocks) → grid[][]Ekhon je block niche portese → currentPiece
    @Override 
    protected void paintComponent(Graphics g) { 
        super.paintComponent(g);                              //purono drawing clear kore dey, notun draw er age
        for (int row = 0; row < ROWS; row++) {                
            for (int col = 0; col < COLS; col++) { 
                if (grid[row][col] != null) { 
                    g.setColor(grid[row][col]);               // oi row colum e jodi tetrimino boshe tahole color set kori
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);     //draw kori

                    g.setColor(Color.BLACK); 
                    g.drawRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);         //black border dei

                } 
            } 
        } 
// currentPiece er block gula ekekta Point
//Tar x, y value niye ekekta block draw kori
// p.x * BLOCK_SIZE → pixel e convert kore screen e correct jaygay bosay
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
 
