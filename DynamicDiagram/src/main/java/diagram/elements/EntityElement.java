package diagram.elements;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Arrays;

import org.json.JSONObject;

import diagram.CodePanel;

public class EntityElement extends Element {

	Class<?> clazz;
	public EntityElement(Point point) {
		super(point);
		this.shape = new EntityShape(25,25);
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
		g2.drawString(clazz.getSimpleName(), 
				pos.x-g2.getFontMetrics().stringWidth(clazz.getSimpleName())/2, 
				pos.y+g2.getFontMetrics().getHeight()/4);
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}
	protected class EntityShape extends Shape{
		int w,h;
		Polygon p;//square
		protected EntityShape(int w, int h){
			this.w = w;
			this.h = h;
			this.p = new Polygon(new int[] {pos.x-w,pos.x+w,pos.x+w,pos.x-w},
					   			 new int[] {pos.y-h,pos.y-h,pos.y+h,pos.y+h},
					   			 4);
		}
		@Override
		public void draw(Graphics2D g2) {
			g2.drawPolygon(p);
		}
		@Override
		public void update() {
			this.p.xpoints[0] = pos.x-w;
			this.p.xpoints[1] = pos.x+w;
			this.p.xpoints[2] = pos.x+w;
			this.p.xpoints[3] = pos.x-w;
			this.p.ypoints[0] = pos.y-h;
			this.p.ypoints[1] = pos.y-h;
			this.p.ypoints[2] = pos.y+h;
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
	public void edit() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object clone() {
		EntityElement e = new EntityElement(this.pos);
		e.setClazz(this.clazz);
		e.setPos(pos);
		return e;
	}

	@Override
	public JSONObject toJSON() {
		// TODO Auto-generated method stub
		return null;
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

}
