import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class CreateXML {

	String[] channel;
	String[] address;
	String newFile;
	File xmlFile;
	

	// Declare the File
//	String fileNameWithOutExt = FilenameUtils.removeExtension(namePath);
	
//	String newFile = namePath + ".xml";
	
	
	
	public CreateXML(String[] channel, String[] address, String namePath) {


		this.channel = channel;
		this.address = address;
		this.newFile = namePath + ".xml";
		this.xmlFile = new File(newFile);
		

	}
	
	public void createXML() {

	// DocumentBuilderFactory
	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	// DocumentBuilder
	DocumentBuilder docBuilder = null; {
	
	try
	{
		docBuilder = docFactory.newDocumentBuilder();
	}catch(
	ParserConfigurationException e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	// Document
	Document xmlDoc = docBuilder.newDocument();

	// Build XML Elements and Text Nodes
	// <streamingInfos>
	// <item>
	// <title>title or channel name</title>
	// <link>link address</link>
	// </item>
	// </streamingInfos>

	Element rootElement = xmlDoc.createElement("streamingInfos");

	int size = this.channel.length;

	for(
	int i = 0;i<size;i++)
	{

		Element mainElement = xmlDoc.createElement("item");
		// mainElement.setAttribute("sku", "123456");

		Element title = xmlDoc.createElement("title");
		Text titleText = xmlDoc.createTextNode(this.channel[i]);
		title.appendChild(titleText);

		Element link = xmlDoc.createElement("link");
		Text linkText = xmlDoc.createTextNode(this.address[i]);
		link.appendChild(linkText);

		mainElement.appendChild(title);
		mainElement.appendChild(link);

		rootElement.appendChild(mainElement);
	}

	xmlDoc.appendChild(rootElement);

	// Set OutputFormat
	OutputFormat outFormat = new OutputFormat(xmlDoc);outFormat.setIndenting(true);


	// Declare the FileOutputStream
	FileOutputStream outStream = null;
	
	try
	{
		outStream = new FileOutputStream(xmlFile);
	}catch(
	FileNotFoundException e1)
	{
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

	// XMLSerializer to serialize the XML data with
	XMLSerializer serializer = new XMLSerializer(outStream, outFormat);

	// the specific OutputFormat
	try
	{
		serializer.serialize(xmlDoc);
		

		outStream.close();
	}catch(
	IOException e2)
	{
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}

	}


}
	
	public String getFileName() {
		return newFile;
	}
	


}
