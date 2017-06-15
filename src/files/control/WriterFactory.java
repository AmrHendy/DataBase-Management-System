package files.control;

public class WriterFactory {
	public WriterFactory() {
	}

	public IWriter getWriter(String writerType) {
		if (writerType.equalsIgnoreCase("json")) {
			return new JsonWriter();
		} else if (writerType.equalsIgnoreCase("xml")) {
			return new XmlWriter();
		} else {
			return null;
		}
	}
}
