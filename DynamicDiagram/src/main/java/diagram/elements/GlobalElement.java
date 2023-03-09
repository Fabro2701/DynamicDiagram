package diagram.elements;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

import org.json.JSONObject;

import diagram.CodePanel;
import diagram.Diagram;
import setup.gui.block.BlockConstructionLauncher;
 
public class GlobalElement extends Element {

	String id;
	public GlobalElement(Point point) {
		super(point);
		this.shape = new EntityShape(25,25);
		this.blockLauncher = new BlockConstructionLauncher("resources/global.sklt");
	}
	@Override
	public String fileName() {
		return "Global"+id;
	}
	
	@Override
	public void write(CodePanel panel) {
		panel.insertString("");
	}

	public static GlobalElement fromJSON(Diagram diagram, JSONObject ob) {
		JSONObject pos = ob.getJSONObject("pos");
		GlobalElement e = new GlobalElement(new Point(pos.getInt("x"),pos.getInt("y")));
		e.setDiagram(diagram);
		e.setId(ob.getString("id"));
		return e;
	}

	@Override
	public JSONObject toJSON() {
		return new JSONObject().put("pos", new JSONObject().put("x",pos.x).put("y", pos.y))
							   .put("id", id)
							   .put("type", "Global");
	}
	@Override
	public void draw(Graphics2D g2) {
		shape.draw(g2);
		g2.drawString(id, 
				pos.x-g2.getFontMetrics().stringWidth(id)/2, 
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
	public Object clone() {
		GlobalElement e = new GlobalElement(this.pos);
		e.setId(id);
		e.setPos(pos);
		return e;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
