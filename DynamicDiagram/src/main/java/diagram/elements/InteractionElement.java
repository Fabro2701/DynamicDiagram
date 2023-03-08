package diagram.elements;

import java.awt.Graphics2D;
import java.awt.Point;

import org.json.JSONObject;

import diagram.CodePanel;
import diagram.elements.Element.Shape;

public class InteractionElement extends Element {
	private String id;
	private Element from, to;
	public InteractionElement(Point point) {
		super(point);
		this.shape = new InteractionShape(50,50);
	}


	@Override
	public void draw(Graphics2D g2) {
		shape.draw(g2);
		g2.drawString(id, 
				pos.x-g2.getFontMetrics().stringWidth(id)/2, 
				pos.y);
		if(from instanceof PendingElement) {
			from.draw(g2);
		}
		else {
			Point l = shape.leftPoint();
			Point r = from.shape.rightPoint();
			g2.drawLine(l.x, l.y, r.x, r.y);
		}

		if(to instanceof PendingElement) {
			to.draw(g2);
		}
		else {
			Point l = shape.rightPoint();
			Point r = from.shape.leftPoint();
			g2.drawLine(l.x, l.y, r.x, r.y);
		}
	}
	@Override
	public Element contains(Point point) {
		if(from instanceof PendingElement && from.contains(point)!=null) {
			return from;
		}
		if(to instanceof PendingElement && to.contains(point)!=null) {
			return to;
		}
		else return this.shape.contains(point)?this:null;
	}
	@Override
	public void update() {
		this.shape.update();
		if(from instanceof PendingElement) {
			from.setPos(shape.leftPoint());
		}
		if(to instanceof PendingElement) {
			to.setPos(shape.rightPoint());
		}
	}
	@Override
	public void delete() {
		// TODO Auto-generated method stub
	}
	@Override
	public void edit() {
		// TODO Auto-generated method stub
	}
	protected class InteractionShape extends Shape{
		int w,h;
		protected InteractionShape(int w, int h){
			this.w = w;
			this.h = h;
		}
		@Override
		public void draw(Graphics2D g2) {
			g2.drawOval(pos.x-w/2, pos.y-h/2, w, h);
		}

		@Override
		public void update() {
			int a = 0;
		}

		@Override
		public boolean contains(Point point) {
			// square simplification
			return (point.x>=pos.x-w/2&&point.x<=pos.x+w/2)&&(point.y>=pos.y-h/2&&point.y<=pos.y+h/2);		
		}

		@Override
		public Point leftPoint() {
			return new Point(pos.x-w/2,pos.y);
		}

		@Override
		public Point rightPoint() {
			return new Point(pos.x+w/2,pos.y);
		}
		
	}
	@Override
	public Object clone() {
		InteractionElement e = new InteractionElement(this.pos);
		e.setId(id);
		e.setFrom(from);
		e.setTo(to);
		e.setPos(pos);
		return e;
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
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Element getFrom() {
		return from;
	}

	public void setFrom(Element from) {
		this.from = from;
	}

	public Element getTo() {
		return to;
	}

	public void setTo(Element to) {
		this.to = to;
	}

}