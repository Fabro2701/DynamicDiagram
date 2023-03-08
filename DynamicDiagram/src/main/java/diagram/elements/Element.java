package diagram.elements;

import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.json.JSONObject;

import diagram.CodePanel;
import diagram.Diagram;

public abstract class Element implements Cloneable{
	protected Point pos;
	protected Diagram diagram;
	protected Shape shape;
	
	JMenuItem editMenu;
	JMenuItem copyMenu;
	JMenuItem deleteMenu;
	
	public Element(Point point) {
		this.pos = point;
		this.editMenu = new JMenuItem("edit");
		this.editMenu.addActionListener((a)->{Element.this.edit();});
		this.copyMenu = new JMenuItem("copyMenu");
		this.copyMenu.addActionListener((a)->{Element.this.copy();});
		this.deleteMenu = new JMenuItem("delete");
		this.deleteMenu.addActionListener((a)->{Element.this.delete();});
	}
	public abstract void write(CodePanel panel);
	public abstract void load(JSONObject jo);
	public abstract void draw(Graphics2D g2);
	public abstract void edit();
	public abstract void delete();
	public void copy() {
		this.clone();
	}
	public void move(Point point) {
		this.setPos(new Point(pos.x+point.x, pos.y+point.y));
	}
	public void openMenu(Point point) {
		JPopupMenu pm = new JPopupMenu();
		pm.add(editMenu);
		pm.add(copyMenu);
		pm.add(deleteMenu);
		pm.show(diagram, point.x, point.y);
	}
	public abstract JSONObject toJSON();
	@Override
	public abstract Object clone();
	public Element contains(Point point) {
		if(shape.contains(point))return this;
		return null;
	}
	
	public abstract class Shape{
		public abstract void draw(Graphics2D g2);
		public abstract void update();
		public abstract boolean contains(Point point);
		public abstract Point leftPoint();
		public abstract Point rightPoint();
	}

	public Point getPos() {
		return pos;
	}
	public void setPos(Point pos) {
		this.pos = pos;
		this.update();
	}
	public void update() {
		this.shape.update();
	}
	public Shape getShape() {
		return shape;
	}
	public void setShape(Shape shape) {
		this.shape = shape;
	}
	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
	}
}
