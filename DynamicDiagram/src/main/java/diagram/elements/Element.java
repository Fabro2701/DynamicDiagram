package diagram.elements;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONTokener;

import block_manipulation.block.BlockManager;
import diagram.CodePanel;
import diagram.Diagram;
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
	}
	protected abstract void properties();
	public abstract void write(CodePanel panel);
	public final void load() {
		String filename = "resources/"+this.fileName()+".json";
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
	}
	public final void save(String filename) {
		List<BlockManager> mgs = blockLauncher.getEditor().getManagers();
		JSONArray arr = new JSONArray();
		for(BlockManager m:mgs)arr.put(m.toJSON());
		try {
	         FileWriter file = new FileWriter("resources/"+Element.directory +filename+".json");
	         file.write(arr.toString(4));
	         file.close();
	      } catch (IOException e) {
	         e.printStackTrace();
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
		b.addActionListener((a)->save(this.fileName()));
		p.add(b, BorderLayout.PAGE_END);
		dialog.pack();
		dialog.setVisible(true);
	}
	public abstract void delete();
	public void copy() {
		this.clone();
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
