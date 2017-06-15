package files.control;

import java.io.File;
import java.util.ArrayList;

public interface IReader {
	/**
	 * load data form a specific file and if the file didn't change we will
	 * return currentData variable.
	 * 
	 * @param file
	 * @return data from the xml file.
	 */
	public ArrayList<ArrayList<String>> load(File file);

	public ArrayList<String> getCoulmnNames();

	public ArrayList<String> getCoulmnTypes();
}
