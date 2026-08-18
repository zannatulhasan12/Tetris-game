/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tetrisgame;
import java.awt.*; 
import java.util.Random; 
 
public class Tetromino { 
 
    private Point[] blocks;                 //array of point objects,pottekta block 4 ta point diye toiri, x,y coordinate er x,y
    private Color color;                    //block ta board e ki color dekhabe  (piant component er method diye call kora hoy)
    private int[][] shape;                  //2d array, eta bhujay kon kon position e block thakbe
 
    private static final int[][][] SHAPES = {     // 3D int array declare kora hoise, ja Tetromino-r sob possible shape define kore.
        {{1, 1, 1, 1}},                    //I
        {{1, 1}, {1, 1}},                  //O
        {{0, 1, 0}, {1, 1, 1}},            //T
        {{1, 1, 0}, {0, 1, 1}},            //S
        {{0, 1, 1}, {1, 1, 0}},            //Z
        {{1, 0, 0}, {1, 1, 1}},            //J
        {{0, 0, 1}, {1, 1, 1}}             //L
    }; 
 
    private static final Color[] COLORS = {              //pottek index er jonno 1 ta kore color
        Color.CYAN, Color.YELLOW, Color.MAGENTA, 
        Color.GREEN, Color.RED, Color.BLUE, Color.ORANGE 
    }; 
 
    public Tetromino(int[][] shape, Color color) { 
        this.shape = shape; 
        this.color = color; 
        this.blocks = new Point[4];    // Tetromino block 4 ta square diye toiri.point array toiri korchi size 4, Point object x, y coordinate dhore rakhbe
        updateBlocks();                 //updateBlocks() method call kore ei blocks gulo kon kon position-e thakbe ta calculate kori.
    } 
 
    public static Tetromino getRandomPiece() {           //Method er kaj holo randomly ekta Tetromino block return kora.
        Random rand = new Random(); 
        int index = rand.nextInt(SHAPES.length);         //shape.length holo 7, 0-6 porjonto randomly ekta number generate korbe
        return new Tetromino(SHAPES[index], COLORS[index]);   //dhorlam 2, index=2, ei 2 index er jonno shape ar color hobe
    } 
 //Method call hoy jokhon shape change hoy (e.g., notun Tetromino create hole, rotate hole).
    private void updateBlocks() {                // Private method, jar kaj holo current Tetromino shape-er coordinate gulo blocks[] array te update kora.
        int idx = 0;                                 // Eta ekta counter.Eta blocks[] array er index bole dibe
        for (int row = 0; row < shape.length; row++) {     //kotoguala row ache
            for (int col = 0; col < shape[0].length; col++) {  //row er vitor colum er loop
                if (shape[row][col] == 1) {                   //check kore oi sell e block ache kina
                    blocks[idx++] = new Point(col + 3, row);       //jodi thake ekta Point object toiri kore (x = col + 3, y = row) col+3 karon jate majkhan theke block pore
                } 
            } 
        } 
    } 
 
    public void move(int dx, int dy, Color[][] grid) {   //Eta ekta public method, jeta piece ke move korar jonno use hoy.
        if (canMove(dx, dy, grid)) {    //dx mane x direction e kotogula move,grid board e color matrix jeta diye check kora hoy move possible kina
            for (Point p : blocks) { 
                p.translate(dx, dy);  //translate mane point er position change kora
            } 
        } 
    } 
 //canMove() check kore je Tetromino ke ekta direction e move korle kono problem hobe kina.
    //Jodi grid er baire chole jay ba onno block-er upor chole jay, tahole move kora jabena.
    public boolean canMove(int dx, int dy, Color[][] grid) { //Move possible kina check kore canMove() method diye. 
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
        int[][] rotated = new int[shape[0].length][shape.length];   //Eta new 2D array rotated banay, jekhane rotated shape ta thakbe.
        for (int row = 0; row < shape.length; row++) {              //Old cell from shape[row][col] → goes to
                                                                    //New cell at rotated[col][shape.length - 1 - row]
            for (int col = 0; col < shape[0].length; col++) { 
                rotated[col][shape.length - 1 - row] = shape[row][col]; 
            } 
        } 
 
        int minX = Integer.MAX_VALUE; 
        int minY = Integer.MAX_VALUE; 
        for (Point p : blocks) { 
            minX = Math.min(minX, p.x); 
            minY = Math.min(minY, p.y); 
        } //Rotation er por block gulo jodi kono bame ba upore chole jay (negative ba grid er baire), tahole setake abar shift korte hoy.
         //minX, minY ber kore bujhte pari kotota move kore grid er moddhe ante hobe.

         
         //"Rotation er pore shape ta grid e fit koranor jonne, new x, y calculate kore check kora hoy je shape ta grid er vitore ase kina,
         //ar kono conflict ase kina. Jodi na thake, taile oi new block position save kora hoy."
        Point[] newBlocks = new Point[4]; 
        int idx = 0; 
        for (int row = 0; row < rotated.length; row++) { 
            for (int col = 0; col < rotated[0].length; col++) { 
                if (rotated[row][col] == 1) { 
                    int x = minX + col;     //Eta rotated shape ke grid er moddhe correct position e set korar calculation.
                    int y = minY + row; 
                    if (x < 0 || x >= grid[0].length || y < 0 || y >= grid.length || grid[y][x] != null) {   //Eta check kore rotate korle shape ta valid kina.
                        return; 
                    } 
                    newBlocks[idx++] = new Point(x, y);   //Eta valid hole new block er position save kore newBlocks array te.
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
// minX & minY er kaj ki?
//👉 minX and minY use kora hoy rotate korar por block gula ke previous position e abar grid-er moddhe set korar jonno.
//🔹 Keno dorkar?
//Jokhon tumi ekta shape rotate koro, shape er pattern change hoy. Kintu tumi chao je block gulo thik ager moto jaygay rotate hoye thakuk.
//Seta korar jonno, ager block gulo grid-er kon position e chilo ta ber korte hoy — tar lowest (min) x & y.
//🔹 Kivabe kaj kore?
//int minX = Integer.MAX_VALUE;
//int minY = Integer.MAX_VALUE;
//for (Point p : blocks) {
//    minX = Math.min(minX, p.x);
//    minY = Math.min(minY, p.y);
//🔸 blocks array er modhhe 4 ta point thake (block er x, y position).
//🔸 Loop chaliye prottekta point er moddhe lowest x & y khuje ber kora hoy.
//🔸 Oigula holo minX & minY.
//🔹 Ei minX & minY diye ki hoy?
//int x = minX + col;
//int y = minY + row;
//🔸 Eta use kore new rotated shape ta grid-er exact ager position e boshano hoy.