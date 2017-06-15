package files.control;

import java.io.File;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.management.RuntimeErrorException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlWriter implements IWriter {
	public XmlWriter() {
	}

	@Override
	public void save(File file, ArrayList<ArrayList<String>> data, ArrayList<String> coulmnNames,
			ArrayList<String> coulmnTypes, String tableName) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element rootElement = document.createElement(tableName);
			rootElement.setAttribute("sizeRow", data.size() + "");
			rootElement.setAttribute("sizeCol", coulmnNames.size() + "");
			makeTypesAttributes(rootElement, coulmnNames, coulmnTypes);
			document.appendChild(rootElement);
			for (int i = 0; i < data.size(); i++) {
				ArrayList<String> row = data.get(i);
				Element rowElement = getElement(document, row, i, coulmnNames);
				rootElement.appendChild(rowElement);
			}

			// XML file will be written on console
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, tableName + ".dtd");

			// To make XML File on Hard
			DOMSource source = new DOMSource(document);
			StreamResult resultFile = new StreamResult(file);
			transformer.transform(source, resultFile);

			// Save .dtd
			saveDTD(coulmnNames, tableName, file.getAbsolutePath());
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	private Element getElement(Document document, ArrayList<String> row, int id, ArrayList<String> coulmnNames) {
		Element rowElement = document.createElement("row");
		rowElement.setAttribute("id", "" + id);
		for (int i = 0; i < row.size(); i++) {
			Element cellElement = document.createElement(coulmnNames.get(i));
			cellElement.appendChild(document.createTextNode(row.get(i)));
			rowElement.appendChild(cellElement);
		}
		return rowElement;
	}

	private Element makeTypesAttributes(Element rootElement, ArrayList<String> coulmnNames,
			ArrayList<String> coulmnTypes) {
		for (int i = 0; i < coulmnNames.size(); i++) {
			rootElement.setAttribute("coulmn" + i, coulmnNames.get(i) + ":" + coulmnTypes.get(i));
		}
		return rootElement;
	}

	private void saveDTD(ArrayList<String> colName, String tableName, String path) {
		path = path.substring(0, path.length() - 4 - tableName.length());
		try {
			File file = new File(path, "" + tableName + ".dtd");
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			// * zer0 or more.
			String data = "<!ELEMENT " + tableName + " (row*)>";
			writer.println(data);
			data = "<!ELEMENT row (";
			for (int i = 0; i < colName.size(); i++) {
				data += colName.get(i) + ",";
			}
			data = data.substring(0, data.length() - 1);
			data += ")>";
			writer.println(data);
			for (int i = 0; i < colName.size(); i++) {
				data = "<!ELEMENT " + colName.get(i) + " (#PCDATA)>";
				writer.println(data);
			}
			writer.println();
			data = new String();
			for (int i = 0; i < colName.size(); i++) {
				data = "<!ATTLIST " + tableName + " coulmn" + i + " CDATA #REQUIRED >";
				writer.println(data);
			}
			writer.println("<!ATTLIST " + tableName + " sizeCol CDATA #REQUIRED >");
			writer.println("<!ATTLIST " + tableName + " sizeRow CDATA #REQUIRED >");
			writer.println();
			writer.println("<!ATTLIST row id CDATA #REQUIRED >");

			writer.close();
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
