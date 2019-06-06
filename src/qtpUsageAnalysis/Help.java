package qtpUsageAnalysis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.StringWriter;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

public class Help {
	
	public JFrame displayInFrame(){
		
        JEditorPane jEditorPane = new JEditorPane();
        
        // make it read-only
        jEditorPane.setEditable(false);
        
        // create a scrollpane; modify its attributes as desired
        JScrollPane scrollPane = new JScrollPane(jEditorPane);
        
        // add an html editor kit
        HTMLEditorKit kit = new HTMLEditorKit();
        jEditorPane.setEditorKit(kit);
        
 
        // create some simple html as a string
        String htmlString = helpText();
        
        // create a document, set it on the jeditorpane, then add the html
        Document doc = kit.createDefaultDocument();
        jEditorPane.setDocument(doc);
        jEditorPane.setText(htmlString);
        

        // now add it all to a frame
        JFrame j = new JFrame("HtmlEditorKit Test");
        j.getContentPane().add(scrollPane, BorderLayout.CENTER);

        
        // display the frame
        j.setSize(new Dimension(300,200));
        
        // pack it, if you prefer
        //j.pack();
        
        // center the jframe, then make it visible
        j.setLocationRelativeTo(null);
        j.setVisible(true);
        j.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		return j;
	}

	private String helpText(){
		StringWriter help = new StringWriter();
		help.append("<html>\n" +
				" <head>\n" +
				" </head>\n" +
				" <body>\n" +
				"  <h1>QTP/UFT log file analyzer help file</h1>\n" +
				"<p>\n" +
				"This utility operates on a log file generated on the QTP license server. " +
				"It is created since the log file from the QTP/UFT license server is not really easy to see license utilization over time." +
				"Hence it produces a csv file that is reformatted to easier produce graphs over license utilzation over time, with the ability to filter out license types your are not interrested in." +
				"The output file is then meant to be opened in Excel to produce a graph.\n" +
				"</p>\n" +
				"  <p>\n" +
				"This utility could be used both from a CLI or with a GUI. " +
				"The functionality is slightly more feature rich from the command prompt.\n" +
				"</p>\n" +
				"<p>\n" +
				"Usage: Invoke this file with no arguments to produce the GUI. If you want the " +
				"possiblity for debugging you might want to include the <i>\"<path_to_java_bin_folder>\\java.exe -jar QtpUsageAnalysis.jar\"</i>\n" +
				"</p>\n" +
				"<p>\n" +
				"It need zero, three, or four arguments to run.\n" +
				"</p>" +
				
				"<h2>Usage example</h2>\n" +
				"<p>\n" +
				"<i>java.exe -jar QtpLogUsageGraph.jar lservstat.csv outputFile.csv comma,separated,list,of,interresting,license,types</i>\n" +
				"</p>\n" +
				"<p>\n" +
				"A GUI should be visible if this utility is run with no arguments.\n" +
				"</p>\n" +
				"<p>\n" +
				"(Default is \"recorded\" as mechanism, since this is most accurate.)\n" +
				"</p>" +
				
				"<h3>Producing the log file</h3>\n" +
				"<p>\n" +
				"In order to generate the log file on the QTP license server, execute the following commands:<br>\n" +
				"1). Go to the \"utils\" sub-folder of your license server program folder of your QTP/UFT license server machine.<br>\n" +
				"2). Execute the following command: <br>\n" +
				"   <i>lsusage.exe -l C:\\Windows\\SysWOW64\\lservsta -c C:\\Windows\\SysWOW64\\lservstat.csv</i><br>\n" +
				"Your log file now resides in the <i>\"C:\\Windows\\SysWOW64\\lservstat.csv\"</i> file and can be viewed in for example Notepad.<br>\n" +
				"</p>" +
				
				
				"<h2>License restrictions<h2>" +
				"<p>None. Feel free to add to the backlog, and to implement in the backlog.</p>" +
				
				"<h2>Backlog</h2>" +
				"<p>" +
				"* Create graph plotter (JFreeGraph or similar<br>\n" +
				"* Bug: Check why calculated results are funky<br>\n" +
				"* Enable \"blocked\" and \"calculated\" types in GUI<br>\n" +
				"* Investigate if %TEMP% or similar could be used OS independent<br>\n" +
				"* Explain \"blocked\" mode where applicable<br>\n" +
				"* Include possibility to run the lsusage.exe from this tool if HP agreement says yes<br>\n" +
				"* Make help file more clear and pretty<br>\n" +
				"* Bug: Make progress bars work<br>\n" +
				"* Bug: Take care of not-a-QTP-logfile-exception<br>\n" +
				"* Progress bar in its own frame so it doesn't distort layout<br>\n" +
				"* Include self-checking auto tests<br>\n" +
				"* Prettyfy the GUI<br>\n" +
				"  </p>\n" +
				" </body>\n" +
				"</html>\n" );
		return help.toString();
	}
	
}
