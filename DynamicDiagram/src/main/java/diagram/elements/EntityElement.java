package diagram.elements;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import block_manipulation.Vector2D;
import block_manipulation.block.BlockManager;
import diagram.CodePanel;
import diagram.Diagram;
import diagram.translation.UpdatesTranslation;
import setup.gui.block.BlockConstructionLauncher;
 
public class EntityElement extends Element {

	Class<?> clazz;
	public EntityElement(Point point) {
		super(point);
		this.shape = new EntityShape(25,25);
		this.blockLauncher = new BlockConstructionLauncher("resources/updates.sklt");
	}
	@Override
	public String fileName() {
		return clazz.getSimpleName();
	}


	/*@Override
	public List<JSONObject> getBlocks() {
		panel.insertString("init := \n");
		for(JSONObject o:this.blocks) {
			if(o.getString("init").equals("INIT_DEF")) {
				panel.insertString("initImc"+"(\""+clazz.getName()+"\"){\n");
				String s = UpdatesTranslation.translate(o.getJSONObject("root"));
				panel.insertString(s+'\n');
				panel.insertString("}\n");
			}
		}
		panel.insertString(".\n");
		panel.insertString("updates := \n");
		for(JSONObject o:this.blocks) {
			if(o.getString("init").equals("UPDATE_DEF")) {
				panel.insertString("update"+"(\""+clazz.getName()+"\"){\n");
				String s = UpdatesTranslation.translate(o.getJSONObject("root"));
				panel.insertString(s+'\n');
				panel.insertString("}\n");
			}
		}
		panel.insertString(".\n");
	}*/


	public static EntityElement fromJSON(Diagram diagram, JSONObject ob) {
		JSONObject pos = ob.getJSONObject("pos");
		EntityElement e = new EntityElement(new Point(pos.getInt("x"),pos.getInt("y")));
		e.setDiagram(diagram);
		try {
			e.setClazz(Class.forName(ob.getString("clazz")));
		} catch (ClassNotFoundException | JSONException e1) {
			e1.printStackTrace();
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
	public JSONObject toJSON() {
		JSONArray arr = new JSONArray();
		for(BlockManager m:this.blockLauncher.getEditor().getManagers()) {
			if(m.isComplete())arr.put(m.toJSON());
			//arr.put(m.toJSON());
		}
		return new JSONObject().put("pos", new JSONObject().put("x",pos.x).put("y", pos.y))
							   .put("clazz", clazz.getName())
							   .put("type", "Entity")
							   .put("managers", arr)
							   .put("blocks", new JSONArray(this.blocks));
	}
	@Override
	protected void properties() {
		JDialog dialog = new JDialog();
		JPanel panel = new JPanel();
		dialog.setContentPane(panel);
		panel.setLayout(new BorderLayout());
		JPanel proppanel = new JPanel();
		proppanel.setLayout(new GridLayout(0,2));
		JLabel l = new JLabel("class");
		JTextField t = new JTextField(this.clazz.getName());
		proppanel.add(l);proppanel.add(t);
		panel.add(proppanel, BorderLayout.CENTER);
		JButton saveb = new JButton("save");
		saveb.addActionListener(a->{
			try {
				Class<?>c = Class.forName(t.getText());
				this.setClazz(c);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
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
	public void draw(Graphics2D g2) {
		shape.draw(g2);
		g2.drawString(clazz.getSimpleName(), 
				pos.x-g2.getFontMetrics().stringWidth(clazz.getSimpleName())/2, 
				pos.y+g2.getFontMetrics().getHeight()/4);
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
		EntityElement e = new EntityElement(this.pos);
		e.setClazz(this.clazz);
		e.setPos(pos);
		return e;
	}

	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

}
