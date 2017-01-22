import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JTextArea;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JRadioButton;
import javax.swing.JLabel;

import javax.swing.JScrollPane;

/**
 * @author dukocuk
 *
 */

public class M3U2XML {

	CreateXML createXML;

	// Used for filereader.readline().
	String str;
	// Used to keep track of how many lines there are on the file being read.
	int counter = 0;

	// To keep track of how many channels were imported to ArrayList channel and
	// address
	int counter_channel = 0;
	int counter_address = 0;

	/*
	 * Two ArrayList for when lines in the file are read. ArrayList channel
	 * saves the channel names. ArrayList address saves the link addresses. Used
	 * ArrayList to be able to expand the array. It's unknown how many lines
	 * will be in the file.
	 */
	ArrayList<String> channelArrayList = new ArrayList<String>();
	ArrayList<String> addressArrayList = new ArrayList<String>();

	// Creates two Arrays which is the size of ArrayList channel and ArrayList
	// address.

	String[] channelArray = new String[channelArrayList.size()];
	String[] addressArray = new String[addressArrayList.size()];

	// Creates two ArrayList for the Turkish Channels

	ArrayList<String> channelTurkishArrayList = new ArrayList<String>();
	ArrayList<String> addressTurkishArrayList = new ArrayList<String>();

	// Creates two Arrays which is the size of ArrayList channelTurkishArrayList
	// and addressTurkishArrayList

	String[] addressTurkishArray = new String[addressTurkishArrayList.size()];
	String[] channelTurkishArray = new String[channelTurkishArrayList.size()];

	// Creates two ArrayList for the Arab Channels

	ArrayList<String> channelArabArrayList = new ArrayList<String>();
	ArrayList<String> addressArabArrayList = new ArrayList<String>();

	// Creates two Arrays which is the size of ArrayList channelTurkishArrayList
	// and addressTurkishArrayList

	String[] addressArabArray = new String[addressArabArrayList.size()];
	String[] channelArabArray = new String[channelArabArrayList.size()];

	// File currentDir and namePath for the importing file part. CurrentDir
	// shows the current directory when clicking open m3u file.
	File namePath, currentDir;

	// Filefilter for telling only to open certain filetypes.
	FileFilter filter;

	private JFrame frame;

	JTextArea textArea;
	JRadioButton rdbtnAllChannels, rdbtnTurkishChannels, rdbtnArabChannels;

	JLabel lblDone;

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
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		// sets the icon on the fram. Works for windows but not mac.
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));

		// Creates a text area on the window.
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(6, 33, 471, 248);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		Font font = textArea.getFont();
		float size = font.getSize() + 5.0f;
		textArea.setFont(font.deriveFont(size));
		frame.getContentPane().add(textArea);

		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setBounds(6, 4, 480, 250);
		frame.getContentPane().add(scroll);

		// creates the button for opening the file
		JButton btnOpen = new JButton("Open m3u file");
		btnOpen.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnOpen.setBounds(21, 413, 151, 31);
		frame.getContentPane().add(btnOpen);

		JButton btnCreate = new JButton("Create XML file");
		btnCreate.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnCreate.setBounds(312, 413, 166, 31);
		frame.getContentPane().add(btnCreate);

		/**
		 * Creating the radio button.
		 * 
		 */
		rdbtnAllChannels = new JRadioButton("All channels", true);
		rdbtnAllChannels.setFont(new Font("Calibri", Font.BOLD, 18));
		rdbtnTurkishChannels = new JRadioButton("Turkish Channels Only");
		rdbtnTurkishChannels.setFont(new Font("Calibri", Font.BOLD, 18));
		rdbtnArabChannels = new JRadioButton("Arab Channels Only");
		rdbtnArabChannels.setFont(new Font("Calibri", Font.BOLD, 18));
		rdbtnAllChannels.setBounds(0, 312, 159, 31);
		rdbtnTurkishChannels.setBounds(0, 340, 252, 31);
		rdbtnArabChannels.setBounds(0, 368, 345, 31);
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnAllChannels);
		group.add(rdbtnTurkishChannels);
		group.add(rdbtnArabChannels);
		frame.getContentPane().add(rdbtnAllChannels);
		frame.getContentPane().add(rdbtnTurkishChannels);
		frame.getContentPane().add(rdbtnArabChannels);

		JLabel lblImport = new JLabel("Import");
		lblImport.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblImport.setBounds(10, 280, 60, 25);
		frame.getContentPane().add(lblImport);

		btnCreate.setEnabled(false);
		// rdbtnTurkishChannels.setEnabled(false);

		/**
		 * Opens and import the file when button clicked.
		 */

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

				// checks if any file is selected
				int checker;
				checker = chooser.showOpenDialog(null);

				if (checker == JFileChooser.APPROVE_OPTION) {

					if (rdbtnAllChannels.isSelected()) {
						
						instantiateArrays();
					}
					
					else if (rdbtnTurkishChannels.isSelected()) {
						instantiateArrays();
						instantiateArrays(channelTurkishArrayList, addressTurkishArrayList, channelTurkishArray, addressTurkishArray);
					}
					
					else if (rdbtnArabChannels.isSelected()) {
						instantiateArrays();
						instantiateArrays(channelArabArrayList, addressArabArrayList, channelArabArray, addressArabArray);
					}
					
					


					

					btnCreate.setEnabled(true);

					namePath = chooser.getSelectedFile();

					// Reads the file
					try {

						FileReader fr = new FileReader(namePath);
						BufferedReader br = new BufferedReader(
								new InputStreamReader(new FileInputStream(namePath), "UTF-8"));

						while ((str = br.readLine()) != null) {

							if (str.contains("#EXTM3U")) {
								continue;
							}

							else if (counter % 2 == 0) {

								channelArrayList.add(str);

								counter_channel++;
							} else {

								addressArrayList.add(str);
								counter_address++;
							}
							counter++;
						}
						br.close();
						fr.close();

					} catch (IOException e) {
						System.out.println("File not found!");
						e.printStackTrace();
					}

					channelArrayList.replaceAll(String::toUpperCase);
					channelArray = channelArrayList.toArray(channelArray);
					addressArray = addressArrayList.toArray(addressArray);

					for (int i = 0; i < channelArray.length; i++) {

						String toSplit = channelArray[i];
						String[] splitted = toSplit.split(",", 2);
						channelArray[i] = splitted[1];

					}

					// All list

					if (rdbtnAllChannels.isSelected()) {

						for (int i = 0; i < addressArray.length; i++) {

							addressArray[i] = "plugin://plugin.video.f4mTester/?url=" + addressArray[i]
									+ "&streamtype=TSDOWNLOADER&name=" + channelArray[i];

						}

					}

					// Turkish list

					else if (rdbtnTurkishChannels.isSelected()) {

						for (int i = 0; i < channelArray.length; i++) {

							if (channelArray[i].matches("TK(.*)") || channelArray[i].matches("TR(.*)")
									|| channelArray[i].matches("TUR(.*)") || channelArray[i].matches("TURKISH(.*)")

									) {

								String toSplit = channelArray[i];
								String[] splitted = toSplit.split("[-|:|\\||\\W]" ,2);

								channelTurkishArrayList.add(splitted[1]);
								addressTurkishArrayList.add(addressArray[i]);

							}

						}

						channelTurkishArray = channelTurkishArrayList.toArray(channelTurkishArray);
						addressTurkishArray = addressTurkishArrayList.toArray(addressTurkishArray);

						for (int i = 0; i < addressTurkishArray.length; i++) {

							addressTurkishArray[i] = "plugin://plugin.video.f4mTester/?url=" + addressTurkishArray[i]
									+ "&streamtype=TSDOWNLOADER&name=" + channelTurkishArray[i];

						}

					}

					// Arab list

					else if (rdbtnArabChannels.isSelected()) {

						for (int i = 0; i < channelArray.length; i++) {

							if (channelArray[i].matches("AR(.*)")) {

								String toSplit = channelArray[i];
								String[] splitted = toSplit.split("[-|:|\\||\\W]" ,2);

								channelArabArrayList.add(splitted[1]);
								addressArabArrayList.add(addressArray[i]);

							}

						}

						channelArabArray = channelArabArrayList.toArray(channelArabArray);
						addressArabArray = addressArabArrayList.toArray(addressArabArray);

						for (int i = 0; i < addressArabArray.length; i++) {

							addressArabArray[i] = "plugin://plugin.video.f4mTester/?url=" + addressArabArray[i]
									+ "&streamtype=TSDOWNLOADER&name=" + channelArabArray[i];

						}

					}

					/*
					 * This part helps me to figure how it all went out.
					 */

					System.out.println(Arrays.toString(channelArray));
					System.out.println(Arrays.toString(addressArray));

					System.out.println(counter + " total lines");
					System.out.println(counter_channel + "total even lines");
					System.out.println(counter_address + " total odd lines");

					System.out.println("channelArray total size " + channelArray.length);
					System.out.println("addressArray total size " + addressArray.length);
					System.out.println("channelTurkishArray array total size " + channelTurkishArray.length);
					System.out.println("addressTurkishArray array total size " + addressTurkishArray.length);
					System.out.println("channelArabArray array total size " + channelArabArray.length);
					System.out.println("addressArabArray array total size " + addressArabArray.length);

					/*
					 * reset the counters
					 */

					counter_channel = 0;
					counter_address = 0;
					counter = 0;

				}

			}
		});

		/**
		 * Create the XML file when button clicked.
		 */

		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (rdbtnAllChannels.isSelected()) {

					createXML = new CreateXML(channelArray, addressArray, namePath.getName());
					createXML.createXML();
					textArea.setText("You created the file " + createXML.getFileName() + "!");
					clearArrays();

				}

				else if (rdbtnTurkishChannels.isSelected()) {

					createXML = new CreateXML(channelTurkishArray, addressTurkishArray, namePath.getName());
					createXML.createXML();
					textArea.setText("You created the file " + createXML.getFileName() + "!");
					clearArrays(channelTurkishArrayList, addressTurkishArrayList, channelTurkishArray,
							addressTurkishArray);
				}

				else if (rdbtnArabChannels.isSelected()) {

					createXML = new CreateXML(channelArabArray, addressArabArray, namePath.getName());
					createXML.createXML();
					textArea.setText("You created the file " + createXML.getFileName() + "!");
					clearArrays(channelArabArrayList, addressArabArrayList, channelArabArray, addressArabArray);
				}

				btnCreate.setEnabled(false);

			}

		});

	}

	/*
	 * Reset the arrays so it can be ready for next import
	 */

	private void clearArrays(ArrayList<String> channelArrayList, ArrayList<String> addressArrayList,
			String[] channelArray, String[] addressArray) {

		this.channelArrayList.clear();
		this.addressArrayList.clear();
		this.channelArray = null;
		this.addressArray = null;
		channelArrayList.clear();
		addressArrayList.clear();
		channelArray = null;
		addressArray = null;
	}

	private void clearArrays() {

		this.channelArrayList.clear();
		this.addressArrayList.clear();
		this.channelArray = null;
		this.addressArray = null;
	}
	
	
	private void instantiateArrays(ArrayList<String> channelArrayList, ArrayList<String> addressArrayList,
			String[] channelArray, String[] addressArray) {
		
		channelArrayList = new ArrayList<String>();
		addressArrayList = new ArrayList<String>();
		channelArray = new String[channelArrayList.size()];
		addressArray = new String[addressArrayList.size()];
		
	}
	
	private void instantiateArrays() {
		
		this.channelArrayList = new ArrayList<String>();
		this.addressArrayList = new ArrayList<String>();
		this.channelArray = new String[channelArrayList.size()];
		this.addressArray = new String[addressArrayList.size()];
		
	}
	
	
}

// class MyTextArea2 extends Thread {
//
// String text;
//
// public MyTextArea2(String text) {
// this.text = text;
// }
//
// public void run() {
// setLabel(text);
// }
//
// }
//
// public void setLabel(String text) {
// lblDone.setText(text);
// }
//
// class MyTextArea extends Thread {
// String channel;
// String address;
// int value;
//
// public MyTextArea(String channel, String address, int value) {
// this.channel = channel;
// this.address = address;
// this.value = value + 1;
// }
//
// public void run() {
//
// System.out.println(Arrays.toString(channelArray));
// System.out.println(Arrays.toString(addressArray));
//
//
// System.out.println(counter + " total lines");
// System.out.println(counter_channel + " total even lines");
// System.out.println(counter_address + " total odd lines");
//
// System.out.println("channelArray total size " +
// channelArray.length);
// System.out.println("addressArray total size " +
// addressArray.length);
//
//
//
// }
// }
// }
