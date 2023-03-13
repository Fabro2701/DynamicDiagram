package diagram.elements;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.json.JSONObject;

import block_manipulation.block.BlockManager;
import diagram.Diagram;
import diagram.Pair;
import setup.gui.block.BlockConstructionLauncher;

public abstract class Element implements Cloneable{
	public static String directory = "test1/";
	protected Point pos;
	protected Diagram diagram;
	protected Shape shape;
	
	JMenuItem editMenu;
	JMenuItem copyMenu;
	JMenuItem deleteMenu;
	JMenuItem propertiesMenu;
	
	BlockConstructionLauncher blockLauncher;
	
	List<JSONObject>blocks;
	
	public Element(Point point) {
		this.pos = point;
		this.editMenu = new JMenuItem("edit");
		this.editMenu.addActionListener((a)->{Element.this.edit();});
		this.copyMenu = new JMenuItem("copyMenu");
		this.copyMenu.addActionListener((a)->{Element.this.copy();});
		this.deleteMenu = new JMenuItem("delete");
		this.deleteMenu.addActionListener((a)->{Element.this.delete();});
		this.propertiesMenu = new JMenuItem("properties");
		this.propertiesMenu.addActionListener((a)->{Element.this.properties();});
		
		blocks = new ArrayList<>();
	}
	protected abstract void properties();
	public Map<String, List<Pair<Element,JSONObject>>> getBlocks(){
		Map<String, List<JSONObject>> bs = this.blocks.stream().filter((JSONObject ob)->ob.getBoolean("complete")).collect(Collectors.groupingBy((JSONObject ob)->ob.getString("init")));
		 Map<String, List<Pair<Element,JSONObject>>> m = new HashMap<>();
		 for(Entry<String,List<JSONObject>>entry:bs.entrySet()) {
			 for(JSONObject o:entry.getValue()) {
				 m.computeIfAbsent(entry.getKey(), s->new ArrayList<>()).add(new Pair<Element,JSONObject>(this, o));
			 }
		 }
		 return m;
	}
	/*public final void load() {
		String filename = "resources/"+directory+this.fileName()+".json";
		JSONArray arr = null;
		try {
			arr = new JSONArray(new JSONTokener(new FileInputStream(filename)));
		} catch (FileNotFoundException e) {
			System.out.println(filename+" not available to load");
			return;
		}catch (JSONException e) {
			e.printStackTrace();
		}
		this.blockLauncher.getEditor().loadBlocks(arr);
		
		for(int i=0;i<arr.length();i++) {
			blocks.add(arr.getJSONObject(i).getJSONObject("root"));
		}
	}*/
	public final void save() {
		blocks.clear();
		List<BlockManager> mgs = blockLauncher.getEditor().getManagers();
		for(BlockManager m:mgs) {
			if(m.isComplete())blocks.add(m.toJSON());			
		}
	}
	public abstract String fileName();
	public abstract void draw(Graphics2D g2);
	
	public final void edit() {
		JDialog dialog = new JDialog();
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		dialog.setContentPane(p);
		p.add(this.blockLauncher, BorderLayout.CENTER);
		JButton b = new JButton("save");
		b.addActionListener((a)->save());
		p.add(b, BorderLayout.PAGE_END);
		dialog.pack();
		dialog.setVisible(true);
	}
	public final void delete() {
		diagram.deleteElement(this);
	}
	public void copy() {
		//this.clone();
	}
	public List<PendingElement> getPendingElements(){
		return List.of();
	}
	public void move(Point point) {
		this.setPos(new Point(pos.x+point.x, pos.y+point.y));
	}
	public void openMenu(Point point) {
		JPopupMenu pm = new JPopupMenu();
		pm.add(editMenu);
		pm.add(copyMenu);
		pm.add(deleteMenu);
		pm.add(propertiesMenu);
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
