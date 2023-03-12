package diagram.translation;

import org.json.JSONObject;

import diagram.elements.EntityElement;

public class UpdatesTranslation {
	public static String translate(EntityElement e, JSONObject ob) {
		String id = ob.getJSONArray("blocks").getJSONObject(0).getJSONArray("right").getJSONObject(0).getJSONArray("blocks").getJSONObject(0).getString("text");
		JSONObject cjo = ob.getJSONArray("blocks").getJSONObject(1).getJSONArray("children").getJSONObject(0).getJSONArray("blocks").getJSONObject(0);
		String body = cjo.getString("text");
		StringBuilder sb = new StringBuilder();
		sb.append(id).append("(\""+e.getClazz().getName()+"\")");
		sb.append("{\n");
		sb.append(body).append('\n');
		sb.append("}\n");
		return sb.toString();
	}
}
