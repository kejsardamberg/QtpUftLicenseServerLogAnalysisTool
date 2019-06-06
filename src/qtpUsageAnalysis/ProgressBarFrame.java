package qtpUsageAnalysis;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressBarFrame {
	private final JFrame frame = new javax.swing.JFrame();
	private final JProgressBar progressbar =		new javax.swing.JProgressBar();
	private final JPanel panel = new javax.swing.JPanel();
	private final JLabel label = new javax.swing.JLabel();
	
	public ProgressBarFrame (String caption, int min, int max, int width, int height) {
		prepare(caption, min, max, width, height);
	}
	
	private void prepare(String caption, int min, int max, int width, int height){
//		label.setText(caption);
//		label.setName("progressbarlabel");
		progressbar.setMinimum(min);
		progressbar.setMaximum(max);
		progressbar.setValue(min);
		progressbar.setName("progressbar");
		progressbar.setStringPainted(true);
		progressbar.setVisible(true);
		panel.add(this.label);
		frame.setTitle(caption);
		panel.add(this.progressbar);
		panel.setSize(width - 10, height - 10);
		frame.setSize(new Dimension(width, height));
		frame.setLocationRelativeTo(null);
        //this.frame.getContentPane().add(this.panel, BorderLayout.CENTER);
		frame.add(this.panel);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setVisible(true);		
	}
	
	
	public void update ( int value ){
		progressbar.setValue(value);
	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	//String caption = label.getText();
	        	int value = progressbar.getValue();
	            if (value <= progressbar.getMaximum() && value >= 0) {
	                //progressbar.setValue(value);
//	                frame.setTitle(caption + " " + value);
//	                label.setText(caption + " " + value);
	                progressbar.repaint();
	            } else {
	            	if (value == progressbar.getMaximum()) {
	            		frame.setVisible(false);
	            	}
	            }
	        }
	    });
	}
}
