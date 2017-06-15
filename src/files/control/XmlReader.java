package files.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlReader implements IReader {

	private ArrayList<String> coulmnNames, coulmnTypes;

	public XmlReader() {
		coulmnNames = new ArrayList<String>();
		coulmnTypes = new ArrayList<String>();
	}

	@Override
	public ArrayList<ArrayList<String>> load(File file) {
		Document document;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(file);
			Element tableName = document.getDocumentElement();
			String sizeAtt = tableName.getAttribute("sizeRow");
			int sizeRow = Integer.parseInt(sizeAtt);
			sizeAtt = tableName.getAttribute("sizeCol");
			int sizeCol = Integer.parseInt(sizeAtt);
			coulmnTypes = (ArrayList<String>) FindTypesAttributes(tableName, sizeCol).clone();
			ArrayList<String> colName = loadDTD(file);
			coulmnNames = (ArrayList<String>) colName.clone();
			Element docElements = document.getDocumentElement();
			NodeList data = docElements.getElementsByTagName("row");
			for (int j = 0; j < sizeRow; j++) {
				Node node = data.item(j);
				Element element = (Element) node;
				tableData.add(toArrayList(element, colName));
			}

		} catch (Exception e) {
			throw new RuntimeException();
		}
		return tableData;
	}

	private ArrayList<String> FindTypesAttributes(Element rootElement, int sizeCol) {
		ArrayList<String> types = new ArrayList<String>();
		for (int i = 0; i < sizeCol; i++) {
			String type = rootElement.getAttribute("coulmn" + i);
			types.add(type.substring(type.indexOf(":") + 1, type.length()));
		}
		return types;
	}

	private ArrayList<String> toArrayList(Element row, ArrayList<String> colName) {
		ArrayList<String> rowData = new ArrayList<String>();
		NodeList cells = null;
		Node cell = null;
		Element cellVal = null;
		for (int i = 0; i < colName.size(); i++) {
			cells = row.getElementsByTagName(colName.get(i));
			cell = cells.item(0);
			cellVal = (Element) cell;
			rowData.add(cellVal.getTextContent());
		}
		return rowData;
	}

	private ArrayList<String> loadDTD(File file) {
		String fileName = file.getName();
		fileName = fileName.substring(0, fileName.length() - 4);
		String tableName = fileName;
		fileName += ".dtd";
		String path = file.getPath();
		path = file.getParent();
		file = new File(path, fileName);
		ArrayList<String> colNames = new ArrayList<String>();
		try {
			InputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			try {
				br.readLine();
				String firstLine = br.readLine();
				firstLine = firstLine.substring(15, firstLine.length() - 2);
				int first = 0;
				for (int i = 0; i < firstLine.length(); i++) {
					if (firstLine.charAt(i) == ',') {
						colNames.add(firstLine.substring(first, i));
						first = i + 1;
					}
				}
				colNames.add(firstLine.substring(first, firstLine.length()));
				fis.close();
				isr.close();
				br.close();
			} catch (IOException e) {
				throw new RuntimeException();
			}

		} catch (Exception e) {
			throw new RuntimeException();
		}
		return colNames;
	}

	@Override
	public ArrayList<String> getCoulmnNames() {
		return (ArrayList<String>) coulmnNames.clone();
	}

	@Override
	public ArrayList<String> getCoulmnTypes() {
		return (ArrayList<String>) coulmnTypes.clone();
	}

}
