package diagram.translation;

import org.json.JSONObject;

import diagram.elements.Element;
import diagram.elements.EntityElement;
import diagram.elements.GroupElement;

public class UpdatesTranslation {
	public static String translate(Element e, JSONObject ob) {
		String id = ob.getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(0).getJSONArray("blocks").getJSONObject(0).getString("text");
		JSONObject cjo = ob.getJSONArray("blocks").getJSONObject(1).getJSONArray("children").getJSONObject(0).getJSONArray("blocks").getJSONObject(0);
		String body = cjo.getString("text");
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		String c = null;
		boolean g = false;
		if(e instanceof EntityElement) {
			c = ((EntityElement)e).getClazz().getName();
		}
		else {
			c = ((EntityElement)((GroupElement)e).getFather()).getClazz().getName();
			g = true;
		}
		GroupElement ge = (GroupElement)e;
		sb.append("(\""+c+"\")");
		sb.append("{\n");
		if(g) {
			sb.append("if(this.getAttribute(\""+ge.getAtt()+"\").equals(\""+ge.getValue()+"\")){").append('\n');
			sb.append(body).append('\n');
			sb.append("}\n");
		}
		else {
			sb.append(body).append('\n');
		}
		
		sb.append("}\n");
		return sb.toString();
	}
}
