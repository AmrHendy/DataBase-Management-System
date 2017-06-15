package files.control;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonWriter implements IWriter {

	public JsonWriter() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void save(File file, ArrayList<ArrayList<String>> data, ArrayList<String> coulmnNames,
			ArrayList<String> coulmnTypes, String tableName) {
		// TODO Auto-generated method stub

		int sizeCol = coulmnNames.size();
		int sizeRow = data.size();
		Gson gsonObject = new GsonBuilder().setPrettyPrinting().create();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sizeCol", "" + sizeCol);
		jsonObject.put("sizeRow", "" + sizeRow);
		jsonObject = makeAttribute(jsonObject, coulmnNames, coulmnTypes);
		JSONArray array;
		for (int i = 0; i < data.size(); i++) {
			ArrayList<String> row = data.get(i);
			array = new JSONArray();
			for (int j = 0; j < row.size(); j++) {
				array.add("" + row.get(j));

			}
			jsonObject.put("Row" + i, array);
		}
		try {
			FileWriter x = new FileWriter(file);
			// pretty printing using GSON object
			x.write(gsonObject.toJson(jsonObject));
			x.close();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	@SuppressWarnings("unchecked")
	private JSONObject makeAttribute(JSONObject object, ArrayList<String> coulmnNames, ArrayList<String> coulmnTypes) {
		for (int i = 0; i < coulmnNames.size(); i++) {
			object.put("column" + i, "" + coulmnNames.get(i) + ":" + coulmnTypes.get(i));
		}
		return object;

	}

}
