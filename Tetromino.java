/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tetrisgame;
import java.awt.*; 
import java.util.Random; 
 
public class Tetromino { 
 
    private Point[] blocks;                 
    private Color color;                    
    private int[][] shape;                  
 
    private static final int[][][] SHAPES = {     
        {{1, 1, 1, 1}},                    //I
        {{1, 1}, {1, 1}},                  //O
        {{0, 1, 0}, {1, 1, 1}},            //T
        {{1, 1, 0}, {0, 1, 1}},            //S
        {{0, 1, 1}, {1, 1, 0}},            //Z
        {{1, 0, 0}, {1, 1, 1}},            //J
        {{0, 0, 1}, {1, 1, 1}}             //L
    }; 
 
    private static final Color[] COLORS = {              
        Color.CYAN, Color.YELLOW, Color.MAGENTA, 
        Color.GREEN, Color.RED, Color.BLUE, Color.ORANGE 
    }; 
 
    public Tetromino(int[][] shape, Color color) { 
        this.shape = shape; 
        this.color = color; 
        this.blocks = new Point[4];  
        updateBlocks();                
    } 
 
    public static Tetromino getRandomPiece() {          
        Random rand = new Random(); 
        int index = rand.nextInt(SHAPES.length);         
        return new Tetromino(SHAPES[index], COLORS[index]);   
    } 

    private void updateBlocks() {                
        int idx = 0;                                
        for (int row = 0; row < shape.length; row++) {     
            for (int col = 0; col < shape[0].length; col++) {  
                if (shape[row][col] == 1) {                  
                    blocks[idx++] = new Point(col + 3, row);      
                } 
            } 
        } 
    } 
 
    public void move(int dx, int dy, Color[][] grid) {   
        if (canMove(dx, dy, grid)) {    
            for (Point p : blocks) { 
                p.translate(dx, dy);  
            } 
        } 
    } 

    public boolean canMove(int dx, int dy, Color[][] grid) {  
        for (Point p : blocks) { 
            int newX = p.x + dx; 
            int newY = p.y + dy; 
            if (newX < 0 || newX >= grid[0].length || newY < 0 || newY >= grid.length || grid[newY][newX] != null) { 
                return false; 
            } 
        } 
        return true; 
    } 
 
    public void rotate(Color[][] grid) { 
        int[][] rotated = new int[shape[0].length][shape.length];  
        for (int row = 0; row < shape.length; row++) {              
                                                                    
            for (int col = 0; col < shape[0].length; col++) { 
                rotated[col][shape.length - 1 - row] = shape[row][col]; 
            } 
        } 
 
        int minX = Integer.MAX_VALUE; 
        int minY = Integer.MAX_VALUE; 
        for (Point p : blocks) { 
            minX = Math.min(minX, p.x); 
            minY = Math.min(minY, p.y); 
        } 
         

        Point[] newBlocks = new Point[4]; 
        int idx = 0; 
        for (int row = 0; row < rotated.length; row++) { 
            for (int col = 0; col < rotated[0].length; col++) { 
                if (rotated[row][col] == 1) { 
                    int x = minX + col;     
                    int y = minY + row; 
                    if (x < 0 || x >= grid[0].length || y < 0 || y >= grid.length || grid[y][x] != null) {  
                        return; 
                    } 
                    newBlocks[idx++] = new Point(x, y);   
                } 
            } 
        } 
        this.shape = rotated; 
        this.blocks = newBlocks; 
    } 
 
    public Point[] getBlocks() { 
        return blocks; 
    } 
 
    public Color getColor() { 
        return color; 
    }
}
