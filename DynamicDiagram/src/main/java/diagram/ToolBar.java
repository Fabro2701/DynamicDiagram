package diagram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


import diagram.elements.Element;
import diagram.elements.EntityElement;
import diagram.elements.GlobalElement;
import diagram.elements.GroupElement;
import diagram.elements.InteractionElement;
import diagram.elements.PendingElement;

public class ToolBar extends JPanel{
	Diagram diagram;
	List<Element>elements;
	Map<Element, Point>initpositions;
	public ToolBar() {
		this.setPreferredSize(new Dimension(800,100));
		this.setLayout(new BorderLayout());
		this.initElements();
		JButton compileButton = new JButton("compile");
		compileButton.addActionListener((a)->diagram.compile());
		this.add(compileButton,BorderLayout.LINE_END);
		
		CustomMouse mouse = new CustomMouse();
		this.addMouseListener(mouse);
		this.addMouseMotionListener(mouse);
	}
	private void initElements() {
		this.elements = new ArrayList<>();
		this.initpositions = new HashMap<>();
		int xshift = 80;
		
		Point p = new Point(50,50);
		Element e = createGlobal("global", p);
		initpositions.put(e, p);
		this.elements.add(e);
		
		p = new Point(50+xshift,50);
		e = createEntity(EntityElement.class, p);
		initpositions.put(e, p);
		this.elements.add(e);
		
		p = new Point(50+xshift*2,50);
		e = createGroup("att","value", p);
		initpositions.put(e, p);
		this.elements.add(e);
		
		p = new Point(50+xshift*3,50);
		e = createInteraction("int", p);
		initpositions.put(e, p);
		this.elements.add(e);
	}
	 @Override
	 public void paintComponent(Graphics g) {
		 Graphics2D g2 = (Graphics2D)g;
		 g2.setColor(new Color(200,200,200));
		 g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		 g2.setColor(Color.black);
		 for(Element e:elements) {
			 e.draw(g2);
		 }
		 
	 }
	public static InteractionElement createInteraction(String id, Point point) {
		InteractionElement e = new InteractionElement(point);
		e.setId(id);
		
		e.setFrom(e.createPendingFromChild());

		e.setTo(e.createPendingToChild());
		return e;
	}
	public static EntityElement createEntity(Class<?>clazz, Point point) {
		EntityElement e = new EntityElement(point);
		e.setClazz(clazz);
		return e;
	}
	public static GroupElement createGroup(String att, String value, Point point) {
		GroupElement e = new GroupElement(point);
		e.setAtt(att);
		e.setValue(value);
		
		e.setFather(e.createPendingChild());
		return e;
	}
	public static GlobalElement createGlobal(String id, Point point) {
		GlobalElement e = new GlobalElement(point);
		e.setId(id);
		return e;
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
			 currentPoint = null;
			 currentElement = null;
			 pressed = false;
		 }
		@Override
		public void mouseDragged(MouseEvent ev){
			Point p = ev.getPoint();
			if(pressed) {
				if(currentElement!=null) {
					if(p.y>=ToolBar.this.getHeight()) {
						MouseEvent me = new MouseEvent(ToolBar.this, MouseEvent.MOUSE_RELEASED,1,0,0,0,1,false);
						EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
						eventQueue.postEvent(me);
						diagram.insertElement((Element)currentElement.clone(), new Point(p.x,0), true);
						currentElement.setPos(ToolBar.this.initpositions.get(currentElement));
					}
					else {
						currentElement.setPos(p);
					}
					repaint();
				}
			}
		}
	 }
	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
	}
}
