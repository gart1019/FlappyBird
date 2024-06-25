import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener {
    int boardWidth = 360;
    int boardHeight = 640;

    Image backgroundImage;
    Image birdImage;
    Image topPipImage;
    Image bottomPipeImage;

    //Bird variables
    int birdx = boardWidth / 8;
    int birdy = boardHeight / 2;
    int birdWidth = 34;
    int birdHeight = 24;
    

    //Unecessary class bruh
    class Bird {
        int x = birdx;
        int y = birdy;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    //Game Logic
    Bird bird;
    int velocityY;
    int velocityX;
    Timer gameLoop;
    
    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));

        //Load images
        backgroundImage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipImage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        // Create the bird by calling bird class
        bird = new Bird(birdImage);

        //Game Loop
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Draw the background, etc
     * @param g G is used to draw the image to the JPanel
     */
    public void draw(Graphics g) {
        System.out.println("Draw");
        //Background
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);

        //Bird
        g.drawImage(birdImage, bird.x, bird.y, bird.width, bird.height, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
