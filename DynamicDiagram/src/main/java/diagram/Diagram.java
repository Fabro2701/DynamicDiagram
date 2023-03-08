package diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import diagram.elements.Element;
import diagram.elements.EntityElement;
import diagram.elements.PendingElement;

public class Diagram extends JPanel{
	 private ToolBar toolBar;
	 List<Element>elements;
	 CustomMouse mouse;
	 public Diagram(ToolBar toolBar) {
		 this.setPreferredSize(new Dimension(800,500));
		 elements = new ArrayList<>();
		 this.toolBar = toolBar;
		 toolBar.setDiagram(this);
		 this.insertElement(ToolBar.createInteraction("int1", new Point(50,50)), new Point(150,50));
		 this.insertElement(ToolBar.createEntity(EntityElement.class, new Point(50,50)), new Point(50,50));
		 this.insertElement(ToolBar.createEntity(EntityElement.class, new Point(250,100)), new Point(250,100));

		 mouse = new CustomMouse();
		 this.addMouseListener(mouse);
		 this.addMouseMotionListener(mouse);
		 this.repaint();
	 }
	 @Override
	 public void paintComponent(Graphics g) {
		 Graphics2D g2 = (Graphics2D)g;
		 g2.setColor(Color.white);
		 g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		 g2.setColor(Color.black);
		 for(Element e:elements) {
			 e.draw(g2);
			 for(PendingElement pe:e.getPendingElements()) {
				 Point f = pe.getConnection();
				 Point i = pe.getPos();
				 if(f!=null)g2.drawLine(i.x, i.y, f.x, f.y);
			 }
		 }
		 
	 }
	 public void insertElement(Element element, Point point) {
		 //Element e = (Element)element.clone();
		 Element e = element;
		 e.setPos(point);
		 e.setDiagram(this);
		 elements.add(e);
		 this.repaint();
	 }
	 private class CustomMouse extends MouseAdapter{
		 Point currentPoint;
		 boolean pressed = false;
		 Element currentElement=null;
		 @Override
		 public void mouseClicked(MouseEvent ev) {
			 Point p = ev.getPoint();
			 if(SwingUtilities.isLeftMouseButton(ev)) {
				 
			 }
			 else if(SwingUtilities.isRightMouseButton(ev)) {
				 for(Element e:elements) {
					 if(e.contains(p)!=null) {
						 e.openMenu(p);
						 break;
					 }
				 }
			 }
		 }
		 @Override
		 public void mousePressed(MouseEvent ev) {
			 currentPoint = ev.getPoint();
			 pressed = true;
			 for(Element e:elements) {
				 Element s = null;
				 if((s=e.contains(currentPoint))!=null) {
					 currentElement = s;
					 break;
				 }
			 }
		 }
		 @Override
		 public void mouseReleased(MouseEvent ev) {
			 if(currentElement instanceof PendingElement) {
				 boolean found = false;
				 for(Element e:elements) {
					 Element s = null;
					 if((s=e.contains(ev.getPoint()))!=null) {
						 ((PendingElement)currentElement).connect(s);
						 found = true;
						 break;
					 }
				 }
				 if(!found)((PendingElement) currentElement).setConnection(null);
				 repaint();
			 }
			 currentPoint = null;
			 currentElement = null;
			 pressed = false;
		 }
		 @Override
		 public void mouseDragged(MouseEvent ev){
			 Point p = ev.getPoint();
			 Point change = new Point(-currentPoint.x+p.x,-currentPoint.y+p.y);
			 //System.out.println(currentPoint+"  "+p+"  "+change);
			 change.x*=0.1;
			 change.y*=0.1;
			 if(change.x!=0)change.x/=Math.abs(change.x);
			 if(change.y!=0)change.y/=Math.abs(change.y);
			// System.out.println(currentPoint+"  "+p+"  "+change);
			 if(pressed) {
				 if(currentElement!=null) {
					 if(currentElement instanceof PendingElement) {
						 ((PendingElement)currentElement).setConnection(p);
					 }
					 else {
						 currentElement.move(change);
					 }
					 repaint();
				 }
				 else{//navigate
					 for(Element e:elements) {
						 e.setPos(new Point(e.getPos().x+change.x, e.getPos().y+change.y)); 
					 }
					 repaint();
				 }
			 }
		 }
	 }
	 public static void main(String args[]) {
		 SwingUtilities.invokeLater(()->{
			 Diagram d = new Diagram(new ToolBar());
			 JFrame frame = new JFrame();
			 frame.add(d);
			 frame.setLocationRelativeTo(null);
			 frame.pack();
			 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			 frame.setVisible(true);
		 });
	 }
}
