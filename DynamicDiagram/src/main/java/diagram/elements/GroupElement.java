package diagram.elements;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

import org.json.JSONObject;

import diagram.CodePanel;
import diagram.elements.Element.Shape;
import diagram.elements.EntityElement.EntityShape;

public class GroupElement extends Element {
	String att;
	String value;
	Element father;
	public GroupElement(Point point) {
		super(point);
		this.shape = new GroupShape(25,25);
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
		shape.draw(g2);
		if(father instanceof PendingElement)father.draw(g2);
		g2.drawString(value, 
				pos.x-g2.getFontMetrics().stringWidth(value)/2, 
				pos.y+g2.getFontMetrics().getHeight()/4);
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}
	@Override
	public Object clone() {
		GroupElement e = new GroupElement(this.pos);
		e.setAtt(att);
		e.setValue(value);
		e.setFather(father);
		e.setPos(pos);
		return e;
	}

	@Override
	public void edit() {
		// TODO Auto-generated method stub
		
	}
	protected class GroupShape extends Shape{
		int w,h;
		private Polygon p;
		protected GroupShape(int w, int h){
			this.w = w;
			this.h = h;
			this.p = new Polygon(new int[] {pos.x-w,pos.x,pos.x+w,pos.x},
					   new int[] {pos.y,pos.y-h,pos.y,pos.y+h},
					   4);
		}
		@Override
		public void draw(Graphics2D g2) {
			g2.drawPolygon(p);
		}

		@Override
		public void update() {
			this.p.xpoints[0] = pos.x-w;
			this.p.xpoints[1] = pos.x;
			this.p.xpoints[2] = pos.x+w;
			this.p.xpoints[3] = pos.x;
			this.p.ypoints[0] = pos.y;
			this.p.ypoints[1] = pos.y-h;
			this.p.ypoints[2] = pos.y;
			this.p.ypoints[3] = pos.y+h;
		}
		@Override
		public boolean contains(Point point) {
			return (point.x>=pos.x-w&&point.x<=pos.x+w)&&(point.y>=pos.y-h&&point.y<=pos.y+h);		
		}

		@Override
		public Point leftPoint() {
			return new Point(pos.x-w,pos.y);
		}

		@Override
		public Point rightPoint() {
			return new Point(pos.x+w,pos.y);
		}
		
	}

	@Override
	public void update() {
		this.shape.update();
		if(father instanceof PendingElement) {
			father.setPos(shape.leftPoint());
		}
	}
	@Override
	public Element contains(Point point) {
		if(father instanceof PendingElement && father.contains(point)!=null) {
			return father;
		}
		else return this.shape.contains(point)?this:null;
	}
	@Override
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getAtt() {
		return att;
	}
	public void setAtt(String att) {
		this.att = att;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Element getFather() {
		return father;
	}
	public void setFather(Element father) {
		this.father = father;
	}

}
