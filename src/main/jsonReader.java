
package main;

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@SuppressWarnings("unchecked")
public class jsonReader {

	private HashMap<String, Color[]> colours = new HashMap<String, Color[]>();

	jsonReader() {
		JSONParser parser = new JSONParser();
		try {
			JSONObject a = (JSONObject) parser.parse(new FileReader("presets.json"));
			Set<String> names = (Set<String>)a.keySet();

			for (String s : names) {
				JSONArray values = (JSONArray) ((JSONObject) a.get(s)).get("Colours");
				Color[] col = new Color[values.size()];
				for (int i = 0; i < values.size(); i++) {
					JSONArray vals = (JSONArray) values.get(i);
					col[i] = new Color(((Long) vals.get(0)).intValue(), ((Long) vals.get(1)).intValue(), ((Long) vals.get(2)).intValue());
				}
				colours.put(s, col);

			}
		} catch (IOException | ParseException e) {

		}

		//System.out.println(colours.keySet());

	}

	protected HashMap<String, Color[]> returnCombos() {
		return colours;
	}
}