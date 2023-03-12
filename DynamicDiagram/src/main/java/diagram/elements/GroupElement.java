package diagram.elements;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;

import block_manipulation.Vector2D;
import block_manipulation.block.BlockManager;
import diagram.CodePanel;
import diagram.Diagram;
import setup.gui.block.BlockConstructionLauncher;

public class GroupElement extends Element {
	String att;
	String value;
	Element father;
	public GroupElement(Point point) {
		super(point);
		this.shape = new GroupShape(25,25);
		this.blockLauncher = new BlockConstructionLauncher("resources/updates.sklt");
	}
	@Override
	public String fileName() {
		return att+value+father.fileName();
	}

	public static GroupElement fromJSON(Diagram diagram, JSONObject ob) {
		JSONObject pos = ob.getJSONObject("pos");
		GroupElement e = new GroupElement(new Point(pos.getInt("x"),pos.getInt("y")));
		e.setDiagram(diagram);
		e.setAtt(ob.getString("att"));
		e.setValue(ob.getString("value"));
		
		if(ob.has("father")) {
			e.setFather(diagram.findElement(ob.getString("father")));
		}
		else {
			e.setFather(e.createPendingChild());
		}
		JSONArray arr = ob.getJSONArray("managers");
		e.blockLauncher.getEditor().loadBlocks(arr);
		for(int i=0;i<e.blockLauncher.getEditor().getManagers().size();i++) {
			BlockManager m = e.blockLauncher.getEditor().getManagers().get(i);
			m.setBase(new Vector2D(arr.getJSONObject(i).getJSONObject("base").getInt("x"),arr.getJSONObject(i).getJSONObject("base").getInt("y")));
			m.buildBlocks(e.blockLauncher.getEditor().getInitSymbols().get(i));
		}
		for(int i=0;i<arr.length();i++) {
			JSONObject o = arr.getJSONObject(i);
			e.blocks.add(o);
		}
		return e;
	}
	@Override
	protected void properties() {
		JDialog dialog = new JDialog();
		JPanel panel = new JPanel();
		dialog.setContentPane(panel);
		panel.setLayout(new BorderLayout());
		JPanel proppanel = new JPanel();
		proppanel.setLayout(new GridLayout(0,2));
		JLabel l = new JLabel("att");
		JTextField t = new JTextField(this.att);
		proppanel.add(l);proppanel.add(t);
		l = new JLabel("value");
		JTextField t2 = new JTextField(this.value);
		proppanel.add(l);proppanel.add(t2);
		l = new JLabel("to");
		JTextField t3 = new JTextField(this.father instanceof PendingElement?"":this.father.fileName());
		proppanel.add(l);proppanel.add(t3);
		panel.add(proppanel, BorderLayout.CENTER);
		JButton saveb = new JButton("save");
		saveb.addActionListener(a->{
			this.setAtt(t.getText());
			this.setValue(t2.getText());
			if(t3.getText().equals(""))this.setFather(this.createPendingChild());
			else {
				Element e = null;
				if((e=diagram.findElement(t3.getText()))!=null) {
					this.setFather(diagram.findElement(t3.getText()));
				}
			}
			dialog.setVisible(false);
			diagram.repaint();
		});
		panel.add(saveb, BorderLayout.PAGE_END);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}
	@Override
	public JSONObject toJSON() {
		JSONArray arr = new JSONArray();
		for(BlockManager m:this.blockLauncher.getEditor().getManagers()) {
			if(m.isComplete())arr.put(m.toJSON());
		}
		return new JSONObject().put("pos", new JSONObject().put("x",pos.x).put("y", pos.y))
							   .put("att", att)
							   .put("value", value)
							   .put("father", father instanceof PendingElement?null:father.fileName())
							   .put("type", "Group")
							   .put("managers", arr)
							   .put("blocks", new JSONArray(this.blocks));
	}
	public PendingElement createPendingChild() {
		PendingElement pe = new PendingElement(this.getShape().leftPoint());
		pe.setFather(this);
		pe.setF((Element father, Element connection)->((GroupElement)father).setFather(connection));
		return pe;
	}
	@Override
	public void draw(Graphics2D g2) {
		shape.draw(g2);
		if(father instanceof PendingElement)father.draw(g2);
		else {
			Point l = shape.leftPoint();
			Point r = father.shape.rightPoint();
			g2.drawLine(l.x, l.y, r.x, r.y);
		}
		g2.drawString(value, 
				pos.x-g2.getFontMetrics().stringWidth(value)/2, 
				pos.y+g2.getFontMetrics().getHeight()/4);
	}
	@Override
	public List<PendingElement> getPendingElements(){
		List<PendingElement> l = new ArrayList<>();
		if(father instanceof PendingElement)l.add((PendingElement) father);
		return l;
	}
	@Override
	public Element contains(Point point) {
		if(father instanceof PendingElement && father.contains(point)!=null) {
			return father;
		}
		else return this.shape.contains(point)?this:null;
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
		e.setFather(e.createPendingChild());
		e.setPos(pos);
		return e;
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
