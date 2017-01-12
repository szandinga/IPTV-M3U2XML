import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.io.FilenameUtils;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JTextArea;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JRadioButton;

/**
 * @author dukocuk
 *
 */

public class M3U2XML {

	// Used for filereader.readline().
	String str;
	// Used to keep track of how many lines there are on the file being read.
	int counter = 0;

	// To keep track of how many channels were imported to ArrayList channel and address
	int counter_channel = 0;
	int counter_address = 0;

	/*
	 * Two ArrayList for when lines in the file are read. ArrayList channel
	 * saves the channel names. ArrayList address saves the link addresses. Used
	 * ArrayList to be able to expand the array. It's unknown how many lines
	 * will be in the file.
	 */
	ArrayList<String> channel = new ArrayList<String>();
	ArrayList<String> address = new ArrayList<String>();

	// Creates two Arrays which is the size of ArrayList channel and ArrayList
	// address.

	String[] channelArray = new String[channel.size()];
	String[] addressArray = new String[address.size()];

	/*
	 * Two ArrayList to sort only the working channels. ArrayList channelSorted
	 * saves the working channel names. ArrayList addressSorted saves the
	 * working link addresses. Used ArrayList to be able to expand the array.
	 * It's unknown how many lines there will be left after channels are sorted.
	 */
	ArrayList<String> channelSorted = new ArrayList<String>();
	ArrayList<String> addressSorted = new ArrayList<String>();

	// Creates two Arrays which is the size of ArrayList channelSorted and
	// ArrayList addressSorted.

	String[] addressArraySorted = new String[addressSorted.size()];
	String[] channelArraySorted = new String[channelSorted.size()];

	// File currentDir and namePath for the importing file part. CurrentDir
	// shows the current directory when clicking open m3u file.
	File namePath, currentDir;

	// Filefilter for telling only to open certain filetypes.
	FileFilter filter;

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					M3U2XML window = new M3U2XML();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */

	public M3U2XML() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 429, 375);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		// sets the icon on the fram. Works for windows but not mac.
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
		// Creates a text area on the window.
		JTextArea textArea = new JTextArea();
		textArea.setBounds(15, 16, 393, 201);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		Font font = textArea.getFont();
		float size = font.getSize() + 5.0f;
		textArea.setFont(font.deriveFont(size));
		frame.getContentPane().add(textArea);

		// creates the button for opening the file
		JButton btnOpen = new JButton("Open m3u file");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				// creates a chooser so you can chose the file yourself.
				JFileChooser chooser = new JFileChooser();
				// shows the current directory when you want to choice the file.
				currentDir = new File(".");
				// sets a filter to only show m3u files when you open files.
				filter = new FileNameExtensionFilter("M3U File", "m3u");
				// activates the filter for chooser.
				chooser.setFileFilter(filter);
				// activates the current directory.
				chooser.setCurrentDirectory(currentDir);
				// sets the title for open window.
				chooser.setDialogTitle("Open m3u file");

				int checker;
				checker = chooser.showOpenDialog(null);

				if (checker == JFileChooser.APPROVE_OPTION) {
					namePath = chooser.getSelectedFile();

					// Read file
					try {

						FileReader fr = new FileReader(namePath);
						BufferedReader br = new BufferedReader(
								new InputStreamReader(new FileInputStream(namePath), "UTF-8"));

						while ((str = br.readLine()) != null) {

							if (str.contains("#EXTM3U")) {
								continue;
							}

							else if (counter % 2 == 0) {

								channel.add(str);

								counter_channel++;
							} else {

								address.add(str);
								counter_address++;
							}
							counter++;
						}

						br.close();
						fr.close();

						channelArray = channel.toArray(channelArray);
						addressArray = address.toArray(addressArray);

						// Ping the URL

						

						for (int i = 0; i < addressArray.length; i++) {

							pingUrl(channelArray[i], addressArray[i]);

						}

						addressArraySorted = addressSorted.toArray(addressArraySorted);
						channelArraySorted = channelSorted.toArray(channelArraySorted);
						// public static boolean pingUrl(final String address) {
						// try {
						// final URL url = new URL(address);
						// final HttpURLConnection urlConn = (HttpURLConnection)
						// url.openConnection();
						// urlConn.setConnectTimeout(1000 * 10); // mTimeout is
						// in seconds
						// final long startTime = System.currentTimeMillis();
						// urlConn.connect();
						// final long endTime = System.currentTimeMillis();
						// if (urlConn.getResponseCode() ==
						// HttpURLConnection.HTTP_OK) {
						// System.out.println("Time (ms) : " + (endTime -
						// startTime));
						// System.out.println("Ping to " + address + " was
						// success");
						// return true;
						// }

						for (int i = 0; i < channelArraySorted.length; i++) {

							channelArraySorted[i] = channelArraySorted[i].replace("#EXTINF:-1,", "");
							channelArraySorted[i] = channelArraySorted[i].replace("#EXTINF:0,", "");
							channelArraySorted[i] = channelArraySorted[i].replace("(TR) ,", "");
							channelArraySorted[i] = channelArraySorted[i].replace("[ TR ] ", "");

						}

						for (int i = 0; i < addressArraySorted.length; i++) {

							addressArraySorted[i] = "plugin://plugin.video.f4mTester/?url=" + addressArraySorted[i]
									+ "&streamtype=TSDOWNLOADER&name=" + channelArraySorted[i];

						}

						System.out.println(Arrays.toString(channelArray));
						System.out.println(Arrays.toString(addressArray));
						System.out.println(Arrays.toString(channelArraySorted));
						System.out.println(Arrays.toString(addressArraySorted));

						System.out.println(counter + " total lines");
						System.out.println(counter_channel + " total even lines");
						System.out.println(counter_address + " total odd lines");

						System.out.println("channelArray total size " + channelArray.length);
						System.out.println("addressArray total size " + addressArray.length);
						System.out.println("channelArraySorted array total size " + channelArraySorted.length);
						System.out.println("addressArraySorted array total size " + addressArraySorted.length);

						textArea.setText("You have imported and truncated non-working channels in file \n" + namePath
								+ "\n" + "Please click \"Create XML File\"");

					} catch (IOException e) {
						System.out.println("File not found!");
						e.printStackTrace();
					}

				}

			}
		});
		btnOpen.setBounds(15, 269, 140, 50);
		frame.getContentPane().add(btnOpen);

		JButton btnCreate = new JButton("Create XML file");
		
		
		/**
		 *  Create the XML file when button clicked.
		 */
		
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// DocumentBuilderFactory
				DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				// DocumentBuilder
				DocumentBuilder docBuilder = null;
				try {
					docBuilder = docFactory.newDocumentBuilder();
				} catch (ParserConfigurationException e) {
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

				int size = channelSorted.size();

				Element rootElement = xmlDoc.createElement("streamingInfos");

				for (int i = 0; i < size; i++) {

					Element mainElement = xmlDoc.createElement("item");
					// mainElement.setAttribute("sku", "123456");

					Element title = xmlDoc.createElement("title");
					Text titleText = xmlDoc.createTextNode(channelArraySorted[i]);
					title.appendChild(titleText);

					Element link = xmlDoc.createElement("link");
					Text linkText = xmlDoc.createTextNode(addressArraySorted[i]);
					link.appendChild(linkText);

					mainElement.appendChild(title);
					mainElement.appendChild(link);

					rootElement.appendChild(mainElement);
				}

				xmlDoc.appendChild(rootElement);

				// Set OutputFormat
				OutputFormat outFormat = new OutputFormat(xmlDoc);
				outFormat.setIndenting(true);

				// Declare the File
				String fileNameWithOutExt = FilenameUtils.removeExtension(namePath.getName());
				String newFile = fileNameWithOutExt + ".xml";
				File xmlFile = new File(newFile);

				// Declare the FileOutputStream
				FileOutputStream outStream = null;
				try {
					outStream = new FileOutputStream(xmlFile);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// XMLSerializer to serialize the XML data with
				XMLSerializer serializer = new XMLSerializer(outStream, outFormat);

				// the specific OutputFormat
				try {
					serializer.serialize(xmlDoc);
					textArea.setText("You created the file " + newFile + "!");

					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		btnCreate.setBounds(268, 269, 140, 50);
		frame.getContentPane().add(btnCreate);
		
		/**
		 *  Creating the radio button.
		 *  
		 */
		JRadioButton rdbtnAllChannels = new JRadioButton("All channels", true);
		JRadioButton rdbtnTurkishChannels = new JRadioButton("Turkish Channels Only");
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnAllChannels);
		group.add(rdbtnTurkishChannels);
		
		frame.getContentPane().add(rdbtnAllChannels);
		frame.getContentPane().add(rdbtnTurkishChannels);
		
		rdbtnAllChannels.setBounds(25, 229, 109, 23);
		rdbtnTurkishChannels.setBounds(146, 229, 195, 23);
		
		
		

	}

	public void pingUrl(final String channel, final String address) {
		try {
			final URL url = new URL(address);
			final HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setConnectTimeout(1000 * 10); // mTimeout is in seconds
			final long startTime = System.currentTimeMillis();
			urlConn.connect();
			final long endTime = System.currentTimeMillis();
			if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				System.out.println("Time (ms) : " + (endTime - startTime));
				System.out.println("Ping to " + address + " was success");
				channelSorted.add(channel);
				addressSorted.add(address);
				// return true;
			} else {
				System.out.println("Ping to " + address + " not success");
				// return false;
			}

		} catch (final MalformedURLException e1) {
			// e1.printStackTrace();
			System.out.println("MalformedURLException");
		} catch (final IOException e) {
			// e.printStackTrace();
			System.out.println("IOException");
		}
	}
}
