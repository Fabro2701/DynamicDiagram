package diagram.translation;

import org.json.JSONObject;

import diagram.elements.GlobalElement;

public class GlobalTranslation {
	public static String translate(GlobalElement e,JSONObject ob) {
		JSONObject cjo = ob.getJSONArray("blocks").getJSONObject(1).getJSONArray("children").getJSONObject(0).getJSONArray("blocks").getJSONObject(0).getJSONArray("names").getJSONObject(0);
		String body = cjo.getJSONArray("blocks").getJSONObject(0).getString("text");
		StringBuilder sb = new StringBuilder();
		sb.append(e.getId()).append("()");
		sb.append("{\n");
		sb.append(body).append('\n');
		sb.append("}\n");
		return sb.toString();
	}
}
