package widgets;


import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import util.awt.ADelegateFrame;
import util.awt.DelegateFramePainter;

public class ACircledCharacterDrawer extends JFrame implements MouseListener, KeyListener  {
	final static int CARAT_LENGTH = 10; 
	final static int X_OFFSET = 10;
	final static int Y_OFFSET = 15;
	final static int DIAMETER = 20;
	protected int charX, charY;
	protected char lastChar = ' ';;
	public ACircledCharacterDrawer() {
		addMouseListener(this);
		addKeyListener(this);
	}	
	// called when an enqueued paint event for this component is dequeued
	public void paint (Graphics g) {
		super.paint(g); // clears the window
		// better to use FontMetrics to center circle
		g.drawOval(charX - X_OFFSET, charY - Y_OFFSET, DIAMETER, DIAMETER);
		g.drawLine(charX, charY, charX, charY - CARAT_LENGTH);
		g.drawString("" + lastChar, charX, charY);
	}
	
	public void keyTyped(KeyEvent event) {
		setChar(event.getKeyChar());	
	}
	public char getChar() {
		return lastChar;
	}
	public void setChar(char newValue) {
		lastChar = newValue;	
		repaint();	// enqueues a paint event	
	}
	public void mousePressed(MouseEvent event) {
		charX = event.getX();
		charY = event.getY();
		repaint(); // enqueues a paint event	
	}
	
	public void mouseEntered(MouseEvent event) {	}	
	public void mouseExited(MouseEvent event) {}	
	public void mouseReleased(MouseEvent event) {}	
	public void keyPressed(KeyEvent event) {}	
	public void keyReleased(KeyEvent event) {} 	
	public void mouseClicked(MouseEvent event) {}
	
	
	public static void main (String[] args) {
		ACircledCharacterDrawer aDrawer = new ACircledCharacterDrawer();
		aDrawer.setSize(300, 200);
		aDrawer.setVisible(true);
		
	}
}
