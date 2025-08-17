package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; //recebe imputs do teclado

public class KeyHandler implements KeyListener{

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    int code = e.getKeyCode(); //recebe o número da tecla pressionada (ver tabela caso precise)
    if (code == KeyEvent.VK_W){

    }
     if (code == KeyEvent.VK_A){
        
    }
     if (code == KeyEvent.VK_S){
        
    }
     if (code == KeyEvent.VK_D){
        
    }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

}