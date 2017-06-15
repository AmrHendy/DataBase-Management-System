package files.control;

public class ReaderFactory {
	public ReaderFactory() {
	}

	public IReader getReader(String writerType) {
		if (writerType.equalsIgnoreCase("json")) {
			return new JsonReader();
		} else if (writerType.equalsIgnoreCase("xml")) {
			return new XmlReader();
		} else {
			return null;
		}
	}
}
