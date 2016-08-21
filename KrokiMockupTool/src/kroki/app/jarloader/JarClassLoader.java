package kroki.app.jarloader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JarClassLoader extends MultiClassLoader  {
	private JarResources	jarResources;

	public JarClassLoader (String jarName) throws IOException	{
		// Create the JarResource and suck in the .jar file.
		jarResources = new JarResources (jarName);
	}

	protected byte[] loadClassBytes (String className)	{
		// Support the MultiClassLoader's class name munging facility.
		className = formatClassName (className);
		// Attempt to get the class data from the JarResource.
		return (jarResources.getResource (className));
	}

	public InputStream getResourceAsStream(String aName) {
		byte[] res = jarResources.getResource(aName);
		return (res == null) ? null : new ByteArrayInputStream(res);
	}
}
