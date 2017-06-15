package files.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonReader implements IReader {
	private ArrayList<String> coulmnNames, coulmnTypes;

	public JsonReader() {
		coulmnNames = new ArrayList<String>();
		coulmnTypes = new ArrayList<String>();
	}

	@Override
	public ArrayList<ArrayList<String>> load(File file) {
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		JSONParser jsonParser = new JSONParser();
		Object object;
		try {
			FileReader filee = new FileReader(file);
			object = jsonParser.parse(filee);
			JSONObject jsonObject = (JSONObject) object;
			int sizeRow = Integer.parseInt(((String) jsonObject.get("sizeRow")));
			int sizeCol = Integer.parseInt(((String) jsonObject.get("sizeCol")));
			getAttribute(jsonObject, sizeCol);
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < sizeRow; i++) {
				jsonArray = (JSONArray) jsonObject.get("Row" + i);
				ArrayList<String> row = new ArrayList<String>();
				for (int j = 0; j < sizeCol; j++) {
					row.add((String) jsonArray.get(j));
				}
				data.add(row);
			}
			filee.close();
		} catch (Exception e) {
			throw new RuntimeException();
		}
		return data;
	}

	@Override
	public ArrayList<String> getCoulmnNames() {
		return coulmnNames;
	}

	@Override
	public ArrayList<String> getCoulmnTypes() {
		return coulmnTypes;
	}

	private JSONObject getAttribute(JSONObject jsonObject, int sizeCol) {
		this.coulmnNames.clear();
		this.coulmnTypes.clear();
		for (int i = 0; i < sizeCol; i++) {
			String temp = (String) jsonObject.get("column" + i);
			this.coulmnNames.add(i, temp.substring(0, temp.indexOf(":")));
			this.coulmnTypes.add(i, temp.substring(temp.indexOf(":") + 1, temp.length()));
		}
		return jsonObject;
	}
}