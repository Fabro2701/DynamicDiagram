package diagram.elements;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.function.BiConsumer;

import org.json.JSONObject;

import diagram.CodePanel;
import diagram.elements.Element.Shape;
import diagram.elements.EntityElement.EntityShape;

public class PendingElement extends Element {
	Element father;
	BiConsumer<Element,Element>f;
	

	public PendingElement(Point point) {
		super(point);
		this.shape = new PendingShape(7, 7);
	}

	public void connect(Element e) {
		f.accept(father, e);
	}
	@Override
	public void write(CodePanel panel) {
		// TODO Auto-generated method stub
	}

	@Override
	public void load(JSONObject jo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics2D g2) {
		this.shape.draw(g2);
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	protected class PendingShape extends Shape{
		int w,h;
		protected PendingShape(int w, int h){
			this.w = w;
			this.h = h;
		}
		@Override
		public void draw(Graphics2D g2) {
			g2.drawOval(pos.x-w/2, pos.y-h/2, w, h);
		}

		@Override
		public boolean contains(Point point) {
			// square simplification
			return (point.x>=pos.x-w/2&&point.x<=pos.x+w/2)&&(point.y>=pos.y-h/2&&point.y<=pos.y+h/2);		
		}

		@Override
		public Point leftPoint() {
			return new Point(pos.x-w,pos.y);
		}

		@Override
		public Point rightPoint() {
			return new Point(pos.x+w,pos.y);
		}
		@Override
		public void update() {
			// TODO Auto-generated method stub
			
		}

		
	}
	@Override
	public Object clone() {
		System.err.println("PendingElement clonation not allowed");
		return null;
	}

	@Override
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void edit() {
		// TODO Auto-generated method stub
		
	}

	public void setF(BiConsumer<Element, Element> f) {
		this.f = f;
	}

}
