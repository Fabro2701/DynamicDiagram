package diagram.elements;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import diagram.CodePanel;
import diagram.Diagram;
import setup.gui.block.BlockConstructionLauncher;

public class InteractionElement extends Element {
	private String id;
	private Element from, to;
	public InteractionElement(Point point) {
		super(point);
		this.shape = new InteractionShape(50,50);
		this.blockLauncher = new BlockConstructionLauncher("resources/interact.sklt");
	}

	@Override
	public String fileName() {
		return from.fileName()+id+to.fileName();
	}
	public static InteractionElement fromJSON(Diagram diagram, JSONObject ob) {
		JSONObject pos = ob.getJSONObject("pos");
		InteractionElement e = new InteractionElement(new Point(pos.getInt("x"),pos.getInt("y")));
		e.setDiagram(diagram);
		e.setId(ob.getString("id"));
		
		if(ob.has("from")) {
			e.setFrom(diagram.findElement(ob.getString("from")));
		}
		else {
			e.setFrom(new PendingElement(e.getShape().leftPoint()));
		}
		if(ob.has("to")) {
			e.setTo(diagram.findElement(ob.getString("to")));
		}
		else {
			e.setTo(new PendingElement(e.getShape().rightPoint()));
		}
		return e;
	}
	@Override
	public JSONObject toJSON() {
		return new JSONObject().put("pos", new JSONObject().put("x",pos.x).put("y", pos.y))
							   .put("id", id)
							   .put("from", from instanceof PendingElement?null:from.fileName())
							   .put("to", to instanceof PendingElement?null:to.fileName())
							   .put("type", "Interaction");
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
			Point r = to.shape.leftPoint();
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
	public List<PendingElement> getPendingElements(){
		List<PendingElement> l = new ArrayList<>();
		if(from instanceof PendingElement)l.add((PendingElement) from);
		if(to instanceof PendingElement)l.add((PendingElement) to);
		return l;
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
		
		PendingElement pe = new PendingElement(e.getShape().leftPoint());
		pe.setFather(e);
		pe.setF((Element father, Element connection)->((InteractionElement)father).setFrom(connection));
		e.setFrom(pe);
		
		pe = new PendingElement(e.getShape().rightPoint());
		pe.setFather(e);
		pe.setF((Element father, Element connection)->((InteractionElement)father).setTo(connection));
		e.setTo(pe);
		
		e.setPos(new Point(pos));
		return e;
	}

	@Override
	public void write(CodePanel panel) {
		panel.insertString(this.id+'\n');
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
