package diagram;

import java.awt.Point;
import java.util.function.BiConsumer;

import javax.swing.JPanel;

import diagram.elements.Element;
import diagram.elements.EntityElement;
import diagram.elements.GroupElement;
import diagram.elements.InteractionElement;
import diagram.elements.PendingElement;

public class ToolBar extends JPanel{
	Diagram diagram;
	public static InteractionElement createInteraction(String id, Point point) {
		InteractionElement e = new InteractionElement(point);
		e.setId(id);
		
		PendingElement pe = new PendingElement(e.getShape().leftPoint());
		pe.setF((Element father, Element connection)->((InteractionElement)father).setFrom(connection));
		e.setFrom(pe);
		
		pe = new PendingElement(e.getShape().rightPoint());
		pe.setF((Element father, Element connection)->((InteractionElement)father).setTo(connection));
		e.setTo(pe);
		return e;
	}
	public static EntityElement createEntity(Class<?>clazz, Point point) {
		EntityElement e = new EntityElement(point);
		e.setClazz(clazz);
		return e;
	}
	public static GroupElement createGroup(String att, String value, Point point) {
		GroupElement e = new GroupElement(point);
		e.setAtt(att);
		e.setValue(value);
		e.setFather(new PendingElement(e.getShape().leftPoint()));
		return e;
	}
	public void setDiagram(Diagram diagram) {
		this.diagram = diagram;
	}
}
