package com.company;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

    static final int SZEROKOSC_EKRANU = 1900;
    static final int WYSOKOSC_EKRANU = 1000;
    static final int UNIT_SIZE = 25;
    static final int ROZMIAR_OKNA_GRY = (SZEROKOSC_EKRANU*WYSOKOSC_EKRANU)/UNIT_SIZE;
    static final int PREDKOSC_WEZA = 75;
    final int x[] = new int[ROZMIAR_OKNA_GRY];
    final int y[] = new int[ROZMIAR_OKNA_GRY];
    int RozmiarWeza = 6;
    int ZjedzoneJablko;
    int JablkoX;
    int JablkoY;
    char Kierunek = 'R';
    boolean GraWlaczona = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SZEROKOSC_EKRANU,WYSOKOSC_EKRANU));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGry();
    }
    public void startGry() {
        noweJablko();
        GraWlaczona = true;
        timer = new Timer(PREDKOSC_WEZA,this);
        timer.start();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        rysowanie(g);
    }
    public void rysowanie(Graphics g){

        if(GraWlaczona) {
            //POKAZUJE SIATKE NA EKRANIE
            /*
            for (int i = 0; i < WYSOKOSC_EKRANU / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, WYSOKOSC_EKRANU);
                g.drawLine(0, i * UNIT_SIZE, SZEROKOSC_EKRANU, i * UNIT_SIZE);
            }
            */
            g.setColor(Color.red);
            g.fillOval(JablkoX, JablkoY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < RozmiarWeza; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    //WAZ KOLOROWY//
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    //^WAZ KOLOROWY^//
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont( new Font("Ink Free",Font.BOLD, 40 ));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+ZjedzoneJablko, (SZEROKOSC_EKRANU - metrics.stringWidth("Wynik: "+ZjedzoneJablko))/2, g.getFont().getSize());
        }
        else{
            gameOver(g);

        }
        }
    public void noweJablko(){
        JablkoX = random.nextInt((int)(SZEROKOSC_EKRANU/UNIT_SIZE))*UNIT_SIZE;
        JablkoY = random.nextInt((int)(WYSOKOSC_EKRANU/UNIT_SIZE))*UNIT_SIZE;
    }
    public void Ruch(){
        for(int i = RozmiarWeza;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(Kierunek){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }
    public void ZjedzoneJablko() {
        if((x[0] == JablkoX) && (y[0] == JablkoY)) {
            RozmiarWeza++;
            ZjedzoneJablko++;
            noweJablko();
        }
    }
    public void SprawdzKolizje(){
        //sprawdza czy głowa uderza się z ciałem
        for(int i = RozmiarWeza;i>0;i--){
            if((x[0] == x[i])&& (y[0] == y[i])){
                GraWlaczona = false;
            }
        }
        //sprawdza czy głowa uderza o lewą ściankę
        if(x[0] < 0){
            GraWlaczona = false;
        }
        //sprawdza czy głowa uderza o prawą ściankę
        if(x[0] > SZEROKOSC_EKRANU){
            GraWlaczona = false;
        }
        //sprawdza czy głowa uderza o górną ściankę
        if(y[0] < 0){
            GraWlaczona = false;
        }
        //sprawdza czy głowa uderza o dolną ściankę
        if(y[0] > WYSOKOSC_EKRANU) {
            GraWlaczona = false;
        }

        if(!GraWlaczona){
            timer.stop();
        }
    }
    public void gameOver(Graphics g){
        //Wynik
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 40 ));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+ZjedzoneJablko, (SZEROKOSC_EKRANU - metrics1.stringWidth("Score: "+ZjedzoneJablko))/2, g.getFont().getSize());
        //Game Over
        g.setColor(Color.red);
        g.setFont( new Font("Ink Free",Font.BOLD, 75 ));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SZEROKOSC_EKRANU - metrics2.stringWidth("Game Over"))/2, WYSOKOSC_EKRANU/2);
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(GraWlaczona){
            Ruch();
            ZjedzoneJablko();
            SprawdzKolizje();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                if(Kierunek != 'R'){
                    Kierunek = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(Kierunek != 'L'){
                    Kierunek = 'R';
                }
                break;
            case KeyEvent.VK_UP:
                if(Kierunek != 'D'){
                    Kierunek = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if(Kierunek != 'U'){
                    Kierunek = 'D';
                }
                break;
            }
        }
    }
}
