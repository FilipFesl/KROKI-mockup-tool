package kroki.app.jarloader;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public final class JarResources
{

	// jar resource mapping tables
	private Hashtable htSizes=new Hashtable();
	private Hashtable htJarContents=new Hashtable();

	// a jar file
	private String jarFileName;

	/**
	 * creates a JarResources. It extracts all resources from a Jar
	 * into an internal hashtable, keyed by resource names.
	 * @param jarFileName a jar or zip file
	 * @throws IOException 
	 */
	public JarResources(String jarFileName) throws IOException	{
		this.jarFileName=jarFileName;
		init();
	}

	/**
	 * Extracts a jar resource as a blob.
	 * @param name a resource name.
	 */
	public byte[] getResource(String name)	{
		return (byte[])htJarContents.get(name);
	}

	/** initializes internal hash tables with Jar file resources.  
	 * @throws IOException */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void init() throws IOException
	{
		// extracts just sizes only.
		ZipFile zf=new ZipFile(jarFileName);
		Enumeration e=zf.entries();
		while (e.hasMoreElements())			{
			ZipEntry ze=(ZipEntry)e.nextElement();
			htSizes.put(ze.getName(),new Integer((int)ze.getSize()));
		}
		zf.close();

		// extract resources and put them into the hashtable.
		FileInputStream fis=new FileInputStream(jarFileName);
		BufferedInputStream bis=new BufferedInputStream(fis);
		ZipInputStream zis=new ZipInputStream(bis);
		ZipEntry ze=null;
		while ((ze=zis.getNextEntry())!=null)
		{
			if (ze.isDirectory())
			{
				continue;
			}
			int size=(int)ze.getSize();
			// -1 means unknown size.
			if (size==-1)
			{
				size=((Integer)htSizes.get(ze.getName())).intValue();
			}
				byte[] b=new byte[(int)size];
			int rb=0;
			int chunk=0;
			while (((int)size - rb) > 0)
			{
				chunk=zis.read(b,rb,(int)size - rb);
				if (chunk==-1)
				{
					break;
				}
				rb+=chunk;
			}
				// add to internal resource hashtable
			htJarContents.put(ze.getName(),b);
		}
	}
}	// End of JarResources class.
