package nl.bioinformatics.cylineup.gui.windows;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.gui.LayoutHelper;
import nl.bioinformatics.cylineup.gui.SwingHelper;

public class ImportWindow extends JFrame {
	
	private static final long serialVersionUID = 7801003791321846971L;
	
	public ImportWindow(final CyLineUpReferences refs) {
		
		// Create the import window
		SwingHelper.centerWindow(this, 640, 360);
		
		// Create the section where you can choose your import file
		JLabel importHeaderLbl = new JLabel("<html><b>Choose import file</b></html>");
		final JLabel selectedFileLbl = new JLabel("No file selected");
		JButton browseBtn = new JButton("Browse...");
		
		// Set browse action
		final ImportWindow me = this;
		browseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ImportWindow.this.setVisible(false);
				
				boolean isMac = System.getProperty("os.name").toLowerCase().startsWith("mac os");
				String file;
				String dir;
				
				if(isMac) {
					
					JFileChooser fd = new JFileChooser();
					fd.showOpenDialog(ImportWindow.this);
					
					File f = fd.getSelectedFile();
					file = f.getName();
					dir = f.getParent() + File.separator;
					
				} else {
				
					FileDialog fd = new FileDialog(me, "Choose a file", FileDialog.LOAD);
					fd.setFilenameFilter(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String name) {
							return name.endsWith(".csv");
						}
					});
					
					SwingHelper.centerWindow(fd, 250, 350);
					fd.pack();
					fd.setVisible(true);
					fd.setAlwaysOnTop(true);
					
					file = fd.getFile();
					dir = fd.getDirectory();
				}
				
				if(file != null) {
					selectedFileLbl.setText(dir + file);
				}
				
				ImportWindow.this.setVisible(true);
			}
		});
		
		// Create next and cancel buttons
		JButton nextBtn = new JButton("Next");
		JButton cancelBtn = new JButton("Cancel");
		
		// Set next button action listener
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String file = selectedFileLbl.getText();
				File f = new File(file);
				if(f.exists() && !f.isDirectory()) {
					new ParserWindow(f, refs);
					dispose();
				} else {
					JOptionPane.showMessageDialog(me,
							"Please choose a valid CSV file.", "LineUp",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		// Set cancel button action listener
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImportWindow.this.dispose();
			}
		});
		
		// Create layout
		LayoutHelper layout = new LayoutHelper(this);
		
		layout.addRow(importHeaderLbl);
		layout.addRow(new float[] {0.6f, 0.4f}, selectedFileLbl, browseBtn);
		layout.addRow(true, new float[1], Box.createGlue());
		layout.addRow(new float[] {0.2f, 0.4f, 0.2f}, cancelBtn, Box.createGlue(), nextBtn);
		
		// Show the import window
		pack();
		setVisible(true);
		setAlwaysOnTop(true);
		
	}
	
}
