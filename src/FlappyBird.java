import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
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

    //In my opinion, classes should be seperated into other files
    class Bird {
        int x = birdx;
        int y = birdy;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
    }

    //Pipes
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }

        public Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
        }
    }

    //Game Logic
    Bird bird;
    int velocityY = 0;
    int velocityX = -4;
    int gravity = 1;
    boolean gameOver = false;
    double score = 0;
    Timer gameLoop;
    Timer placePipesTimer;
    
    ArrayList<Pipe> pipes;
    Random random = new Random();

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        //Load images
        backgroundImage = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipImage = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        // Create the bird by calling bird class
        bird = new Bird(birdImage);
        pipes = new ArrayList<Pipe>();

        //Placing pipes every 15000ms / 1.5 sec
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        placePipesTimer.start();  

        //Game Loop
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
          
    }

    public void placePipes() {
        int randomPipeY = (int) (pipeY - pipeHeight / 4 - Math.random()*(pipeHeight/2));
        int openSpace = boardHeight / 4;

        Pipe topPipe = new Pipe(topPipImage);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y = topPipe.y + pipeHeight + openSpace;
        pipes.add(bottomPipe);    
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
        //Background
        g.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);

        //Bird
        g.drawImage(birdImage, bird.x, bird.y, bird.width, bird.height, null);

        //Pipes 
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //Score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        } else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    /**
     * Move the bird
     */
    public void move() {
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        //Pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            //Points
            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5; //Due to there being two pipes we add 0.5 to equal one point
            }

            //Check for collisions
            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        //Prevent bird from going above screen
        if (bird.y > boardHeight + 10) {
            gameOver = true;
        }
    }

    /**
     * Check if the bird collides with a given pipe
     * @param a The bird
     * @param b A pipe
     * @return True if collided; false if otherwise
     */
    public boolean collision(Bird a, Pipe b) {
        return a.getBounds().intersects(b.getBounds());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();     
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            if (gameOver) { //Restart game
                bird.y = birdy;
                velocityY = 0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placePipesTimer.start();
            }
        }
    }

    //Unused
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
