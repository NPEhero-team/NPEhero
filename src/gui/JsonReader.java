package gui;

import java.io.Closeable;

public interface JsonReader extends Closeable
{
	public void close();
	public JsonStructure read();
	
}
