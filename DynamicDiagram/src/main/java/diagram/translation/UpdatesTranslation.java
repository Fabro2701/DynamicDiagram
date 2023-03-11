package diagram.translation;

import org.json.JSONObject;

public class UpdatesTranslation {
	public static String translate(JSONObject ob) {
		JSONObject cjo = ob.getJSONArray("blocks").getJSONObject(1).getJSONArray("children").getJSONObject(0).getJSONArray("blocks").getJSONObject(0);
		return cjo.getString("text");
	}
}
