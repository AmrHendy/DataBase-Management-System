package files.control;

import java.io.File;
import java.util.ArrayList;

public interface IWriter {
	/**
	 * save modified data to a specific file.
	 * 
	 * @param file
	 *            file to save data to.
	 * @param data
	 *            the data after any modifications.
	 */
	public void save(File file, ArrayList<ArrayList<String>> data, ArrayList<String> coulmnNames,
			ArrayList<String> coulmnTypes, String tableName);

}
