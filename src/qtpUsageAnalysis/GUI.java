package qtpUsageAnalysis;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.ui.RefineryUtilities;

import qtpUsageAnalysis.GraphCsvFile;
import qtpUsageAnalysis.GraphCsvFile.ConversionType;
import qtpUsageAnalysis.QtpLicenseServerLogFile;
/**
 * Creates the GUI for the QTP/UFT license resorting utility. Utility could also be used from CLI.
 * @author Jörgen Damberg, Claremont AB
 *
 */
public class GUI extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
 
	public GUI (){
		initComponents();
	}
 
	private void initComponents() {
		//InputStream stream = this.getClass().getClassLoader().getResourceAsStream("/images/image.jpg");
		
		//frame					= new javax.swing.JFrame();
		filePathTextField 		= new javax.swing.JTextField();
        filePathLabel 			= new javax.swing.JLabel();
        filePickerButton 		= new javax.swing.JButton();
        convertButton			= new javax.swing.JButton();
//        progressBar 			= new javax.swing.JProgressBar();
//        progressBarText		 	= new javax.swing.JLabel();
        listModel 				= new javax.swing.DefaultListModel<>();
        licenseTypeList 		= new javax.swing.JList<String>();
        licenseMultiChoiceLabel = new javax.swing.JLabel();
        statusText 				= new javax.swing.JLabel();
        outputFilePathLabel 	= new javax.swing.JLabel();
        bragText 				= new javax.swing.JLabel();
        versionLabel			= new javax.swing.JLabel();
        outputFilePath 			= new javax.swing.JTextField();
        helpButton				= new javax.swing.JButton();
        displayGraphButton		= new javax.swing.JButton();
        licenseUsageList		= new LicenseUsageList();

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		screenWidth = gd.getDisplayMode().getWidth();
		screenHeight = gd.getDisplayMode().getHeight();
		
        filePathTextField.setText("c:\\temp\\qtp_log.csv");
        
        picLabel = new JLabel ( new ImageIcon ( getImageFromJar ( "logo.png" ) ) );
        add ( picLabel );

		this.setIconImage ( getImageFromJar ( "icon.png" ));
        
        setDefaultCloseOperation ( javax.swing.WindowConstants.EXIT_ON_CLOSE );
        setTitle ( "HP UFT/QTP license server log file reformatter" );

        //initial state of labels, guiding the user
        filePathLabel.setText 			( "1a). File path to license server log file" );
        filePickerButton.setText 		( "Browse..." );
//        progressBarText.setText 		( "Progress bar for conversion progress below" );
        displayGraphButton.setText		( "3). Display usage graph");
        convertButton.setText			( "4). Generate Excel output file" );
        licenseMultiChoiceLabel.setText ( "2). License types found in file. Select relevant ones." );
        statusText.setText 				( "Please select a UFT/QTP server log file" );
        outputFilePathLabel.setText 	( "1b). Generated output file name" );
        outputFilePath.setText 			( "C:\\temp\\qtp_log_output_file.csv" );
        bragText.setText 				( "Utility created by Jörgen Damberg. Use at your own risk." );

        versionLabel.setText			( "Version " + Main.version );
        versionLabel.setName			( "versionLabel" );
        versionLabel.setFont			( new Font ( "Courier New", Font.ITALIC, 10 ) );
        versionLabel.setForeground		( Color.GRAY );
 
        licenseMultiChoiceLabel.setEnabled	(false);
        convertButton.setEnabled			(false);
        displayGraphButton.setEnabled		(false);
//        progressBarText.setVisible			(false);
 
        statusText.setFont(new Font("Courier New", Font.BOLD, 18));
        statusText.setForeground(Color.BLUE);
 
        bragText.setFont(new Font("Courier New", Font.ITALIC, 10));
        bragText.setForeground(Color.GRAY);
  /*      
        progressBar.setVisible(false);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setMaximum(100);
 */
        listModel = new DefaultListModel<>();
        licenseTypeList = new JList<String>(listModel);
        licenseTypeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        licenseTypeList.setSelectedIndex(0);
        licenseTypeList.addListSelectionListener(null);
        licenseTypeList.setVisibleRowCount(5);
        listScrollPane = new JScrollPane(licenseTypeList);
 
        filePickerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JFrame frame = new JFrame("QTP/UFT license server log file graph presenter");
                frame.setSize(500, 700);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                String filePath = filePicker(frame);
                filePathTextField.setText ( filePath );
            }
        });
 
        helpButton.setText("Help");
        helpButton.setName("helpButton");
        helpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Help help = new Help();
            	help.displayInFrame().setSize(500, 700);
            }
        });

        convertButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Thread toRecordsThread = new Thread() {
            	      public void run() {
            	    	  convertButtonActionPerformed();
            	      }
            	    };
            	    toRecordsThread.start();
            }
        });
 
        displayGraphButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	Thread toRecordsThread = new Thread() {
            	      public void run() {
            	    	  displayGraphActionPerformed();
            	      }
            	    };
            	    toRecordsThread.start();
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());

        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup ( layout.createSequentialGroup()
                .addContainerGap()
                .addGroup ( layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                		.addComponent(statusText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
		               .addGroup(layout.createSequentialGroup()
		                       .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                       .addComponent(filePathLabel)
		                       .addComponent(filePathTextField)
		                       .addComponent(filePickerButton)
		               )
		               .addGroup(layout.createSequentialGroup()
		                       .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                       .addComponent(outputFilePathLabel)
		                       .addComponent(outputFilePath)
		               )
		               .addGroup(layout.createSequentialGroup()
		            		   .addComponent(licenseMultiChoiceLabel)
		    		   )
		               .addGroup(layout.createSequentialGroup()
		                       .addComponent(listScrollPane)
		               )
		               .addGroup(layout.createSequentialGroup()
		                       .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
		                       .addComponent(convertButton)
		                       .addComponent(displayGraphButton)
		                       .addComponent(bragText)
		               )
//		                    .addComponent(progressBarText)
//		                    .addComponent(progressBar)
		               .addGroup(layout.createSequentialGroup()
		            		   .addComponent(picLabel)
		            		   .addComponent(helpButton)
		            		   .addComponent(versionLabel)
		               )
	              )
              )
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {convertButton});

        layout.setVerticalGroup(
            layout.createParallelGroup (javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusText)
                )
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filePathLabel )
                    .addComponent(filePathTextField)
                    .addComponent(filePickerButton)
                )
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(outputFilePathLabel)
                .addComponent(outputFilePath)
                )
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(licenseMultiChoiceLabel)
                )
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(listScrollPane)
                )
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(displayGraphButton)                    
                    .addComponent(convertButton)
                    .addComponent(bragText)
                )
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                	.addComponent(picLabel)
                	.addComponent(helpButton)
                	.addComponent(versionLabel)
                )	
                
//                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                    .addComponent(progressBarText)
//                    )
//                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
//                    .addComponent(progressBar)
//                    )
           )
        );
		setLookAndFeel(screenHeight);

        setSize(screenWidth/2,screenHeight/2);

	}
	
   /**
    * Open a file picker dialogue, and when a log file is chosen it analyses the file for what license types is in it so they can be listed. 
    * @param frame The JFrame
    * @return
    */
   private String filePicker ( JFrame frame ) {
	   String filePath = null;
       chooser = new JFileChooser();
       FileNameExtensionFilter filter = new FileNameExtensionFilter( "Log files", "log", "csv");
       chooser.setFileFilter(filter);
       int returnVal = chooser.showOpenDialog(frame);
       if(returnVal == JFileChooser.APPROVE_OPTION) {
           System.out.println("You chose to open this file: " +
            chooser.getSelectedFile().getPath());
           filePath = chooser.getSelectedFile().getPath();
        }
        QtpLicenseServerLogFile logFile = new QtpLicenseServerLogFile();
        listModel.clear();
        licenseUsageList.records = logFile.toRecords ( filePath );
        List<String> licenseTypes = logFile.getLicensesUsedInFile ( licenseUsageList.records );
        for ( String licenseType : licenseTypes ) {
        	listModel.addElement( licenseType );
        }
        if ( listModel.size() > 0 ){
        	licenseMultiChoiceLabel.setEnabled(true);
        	statusText.setText ( "Please select relevant license types" );
        	convertButton.setEnabled(true);
        	displayGraphButton.setEnabled(true);
        }
        return filePath;
    }
   
   private void setLookAndFeel(int screenHeight){
		Font f = new Font(Font.SANS_SERIF, 3, screenHeight/70);
		convertButton.setFont(f);
		//listModel.setFont(f);
		licenseMultiChoiceLabel.setFont(f);
		statusText.setFont(f);
		versionLabel.setFont(f);
		bragText.setFont(f);
		displayGraphButton.setFont(f);
		helpButton.setFont(f);
		picLabel.setFont(f);
		listScrollPane.setFont(f);
		outputFilePath.setFont(f);
		outputFilePathLabel.setFont(f);
		filePathLabel.setFont(f);
		filePathTextField.setFont(f);
		licenseTypeList.setFont(f);
		filePickerButton.setFont(f);
		//filePicker.setFont(f);
		
   }
 
   
	public static void scaleCheckBoxIcon(JCheckBox checkbox){
	    boolean previousState = checkbox.isSelected();
	    checkbox.setSelected(false);
	    FontMetrics boxFontMetrics =  checkbox.getFontMetrics(checkbox.getFont());
	    Icon boxIcon = UIManager.getIcon("CheckBox.icon");
	    BufferedImage boxImage = new BufferedImage(
	        boxIcon.getIconWidth(), boxIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB
	    );
	    Graphics graphics = boxImage.createGraphics();
	    try{
	        boxIcon.paintIcon(checkbox, graphics, 0, 0);
	    }finally{
	        graphics.dispose();
	    }
	    ImageIcon newBoxImage = new ImageIcon(boxImage);
	    Image finalBoxImage = newBoxImage.getImage().getScaledInstance(
	        boxFontMetrics.getHeight(), boxFontMetrics.getHeight(), Image.SCALE_SMOOTH
	    );
	    checkbox.setIcon(new ImageIcon(finalBoxImage));

	    checkbox.setSelected(true);
	    Icon checkedBoxIcon = UIManager.getIcon("CheckBox.icon");
	    BufferedImage checkedBoxImage = new BufferedImage(
	        checkedBoxIcon.getIconWidth(),  checkedBoxIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB
	    );
	    Graphics checkedGraphics = checkedBoxImage.createGraphics();
	    try{
	        checkedBoxIcon.paintIcon(checkbox, checkedGraphics, 0, 0);
	    }finally{
	        checkedGraphics.dispose();
	    }
	    ImageIcon newCheckedBoxImage = new ImageIcon(checkedBoxImage);
	    Image finalCheckedBoxImage = newCheckedBoxImage.getImage().getScaledInstance(
	        boxFontMetrics.getHeight(), boxFontMetrics.getHeight(), Image.SCALE_SMOOTH
	    );
	    checkbox.setSelectedIcon(new ImageIcon(finalCheckedBoxImage));
	    checkbox.setSelected(false);
	    checkbox.setSelected(previousState);
	}

   /** 
    * Runs the actual conversion of license file
    * @param evt
    */
	private void convertButtonActionPerformed() {
		String filePath = filePathTextField.getText();
		String interrestingLicenseTypes = "";
		if ( filePath.length() < 1 ){
			statusText.setText ("Please point a proper log file out");
		} else {
			if ( licenseTypeList.getSelectedValuesList().size() > 0 ){
//				progressBarText.setText("Creating output file");
//				progressBarText.setVisible(true);
//				progressBar.setVisible(true);
				for ( String licenseType : licenseTypeList.getSelectedValuesList()){
					interrestingLicenseTypes = interrestingLicenseTypes + licenseType + ",";
					System.out.println ( licenseType );
				}
				interrestingLicenseTypes = interrestingLicenseTypes.substring(0, interrestingLicenseTypes.length() - 1 );
				QtpLicenseServerLogFile inputFile = new QtpLicenseServerLogFile();
				statusText.setText ( "Generating output file" );
				GraphCsvFile.create ( outputFilePath.getText(), inputFile.toRecords ( filePath ), interrestingLicenseTypes, ConversionType.FileUsageValues );
				statusText.setText ( "Output file generated" );
//				progressBarText.setVisible(false);
//				progressBar.setVisible(false);
			} else {
				System.out.println ( "Please select some license types" );
			}
		}
	}
		
	   /** 
	    * Runs the actual conversion of license file
	    * @param evt
	    */
		private void displayGraphActionPerformed() {
			List<String> interrestingLicenseTypes = new ArrayList<>();
			if ( licenseTypeList.getSelectedValuesList().size() > 0 ){
				interrestingLicenseTypes.addAll(licenseTypeList.getSelectedValuesList());
				statusText.setText ( "Removing un-interresting license records" );
				LicenseUsageList usageOfInterrestingLicenseTypes = new LicenseUsageList(); 
				usageOfInterrestingLicenseTypes.records.addAll(licenseUsageList.getInterresting(interrestingLicenseTypes));
				statusText.setText ( "Generating graph" );
				//LicenseUsageGraph graphPanel = new LicenseUsageGraph(screenWidth/2, screenHeight/2, usageOfInterrestingLicenseTypes, interrestingLicenseTypes);
				statusText.setText ( "Graph generated" );
				//JFrame graphWindow = new JFrame();
				//JScrollPane scrollPanel = new JScrollPane(graphPanel);
				//graphWindow.add(scrollPanel);
				//graphWindow.pack();
				//graphWindow.setVisible(true);
				ChartPanel2 chartFrame = new ChartPanel2("QTP/UFT license usage", usageOfInterrestingLicenseTypes, interrestingLicenseTypes);
				chartFrame.pack();
				RefineryUtilities.centerFrameOnScreen(chartFrame);
				chartFrame.setVisible(true);
				chartFrame.setSize(screenWidth/2, screenHeight/2);
			} else {
				statusText.setText ( "Please select some license types" );
				System.out.println ( "Please select some license types" );
			}
		}

		/**
	 * Read image from within jar file
	 * @param pathInJar The path to the image in the jar file. Default is in the src folder.
	 * @return the image (BufferedImage class)
	 */
	private BufferedImage getImageFromJar ( String pathInJar ) {
        URL resourceURL = getClass().getResource(pathInJar);
        BufferedImage resource = null;
        try {
			resource = ImageIO.read(resourceURL);
		} catch (IOException e) {
			System.out.println("Cannot get image " + pathInJar );
			e.printStackTrace();
		}  
        return resource;
        
	}
	
 
	private javax.swing.JLabel 						filePathLabel;
	private javax.swing.JLabel 						versionLabel;
    private javax.swing.JButton 					filePickerButton;
    private javax.swing.JButton 					convertButton;
    private javax.swing.JTextField 					filePathTextField;
//    public static javax.swing.JProgressBar 			progressBar;
//    public javax.swing.JLabel 						progressBarText;
    private javax.swing.JLabel 						licenseMultiChoiceLabel;
    public javax.swing.JList<String> 				licenseTypeList;
    private javax.swing.DefaultListModel<String> 	listModel;
    private javax.swing.JLabel 						statusText;
    private javax.swing.JLabel 						bragText;
    private javax.swing.JLabel 						picLabel;
    private javax.swing.JLabel 						outputFilePathLabel;
    private javax.swing.JTextField 					outputFilePath;
    private javax.swing.JButton 					helpButton;
    private javax.swing.JButton						displayGraphButton;
    private javax.swing.JScrollPane					listScrollPane;
    private JFileChooser 							chooser;
    private LicenseUsageList						licenseUsageList;
    private int screenWidth = 0;
    private int screenHeight = 0;
}
