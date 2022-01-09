import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 700;
    static final int SCREEN_HEIGHT = 700;
    static final int UNIT_SIZE = 20;
    static final int DELAY = 75;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten = 0;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(255, 184, 95));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (running) {
            // draws apples
            g.setColor(new Color(255, 122, 90));
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // draws snake
            for (int i = 0; i < bodyParts; i++) {
                // snake head
                if (i == 0) {
                    g.setColor(new Color(0, 170, 160));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    // snake body
                } else {
                    // celebration color when reached multiples of 10
                    if (applesEaten % 10 == 0 && applesEaten != 0) {
                        g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                    // regular color
                    else {
                        g.setColor(new Color(142, 210, 201));
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }

            // draw score at the top of the panel
            g.setColor(new Color(70, 32, 102));
            g.setFont(new Font("Chalkduster", Font.BOLD, 30));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("" + applesEaten,
                    (SCREEN_WIDTH - metrics.stringWidth("" + applesEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    // generate apples on the field
    public void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
    }

    public void move() {

        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        // decides where to add new body part
        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // checks if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // checks if head touches left border
        if (x[0] < 0) {
            running = false;
        }

        // checks if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }

        // checks if head touches top border
        if (y[0] < 0) {
            running = false;
        }

        // checks if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // display score
        g.setColor(new Color(70, 32, 102));
        g.setFont(new Font("Chalkduster", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("" + applesEaten,
                (SCREEN_WIDTH - metrics1.stringWidth("" + applesEaten)) / 2, g.getFont().getSize());

        // game over text
        g.setColor(new Color(70, 32, 102));
        g.setFont(new Font("Chalkduster", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER", (SCREEN_WIDTH - metrics2.stringWidth("GAME OVER")) / 2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    // moving with arrow keys
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if (direction != 'R') {
                        direction = 'L';
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if (direction != 'L') {
                        direction = 'R';
                    }
                }
                case KeyEvent.VK_UP -> {
                    if (direction != 'D') {
                        direction = 'U';
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if (direction != 'U') {
                        direction = 'D';
                    }
                }
            }
        }
    }
}
