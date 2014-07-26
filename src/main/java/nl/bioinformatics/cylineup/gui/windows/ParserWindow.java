package nl.bioinformatics.cylineup.gui.windows;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import org.cytoscape.work.TaskIterator;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.data.CsvData;
import nl.bioinformatics.cylineup.gui.LayoutHelper;
import nl.bioinformatics.cylineup.gui.ParserSeparatorChangeListener;
import nl.bioinformatics.cylineup.gui.SwingHelper;
import nl.bioinformatics.cylineup.tasks.ImportTask;

public class ParserWindow {
	
	public ParserWindow(File f, final CyLineUpReferences refs) {
		
		// Create CSVFile object
		refs.data = new CsvData(f, refs);
		
		// Create the window
		final JFrame frame = new JFrame("Import data");
		SwingHelper.centerWindow(frame);
		
		// Create header label
		JLabel headerLbl = new JLabel("<html><b>Import data</b></html>");
		
		// Create radio buttons
		JPanel btnPanel = new JPanel();
		ButtonGroup group = new ButtonGroup();
		JRadioButton commaRBtn = new JRadioButton(",");
		JRadioButton semicolonRBtn = new JRadioButton(";");
		JRadioButton tabRBtn = new JRadioButton("tab");
		JRadioButton spaceRBtn = new JRadioButton("space");
		JRadioButton whiteSpaceRBtn = new JRadioButton("whitespace");
		
		// Create radio button group
		btnPanel.setBorder(new TitledBorder("Separator character"));
		group.add(commaRBtn);
		commaRBtn.setSelected(true);
		group.add(semicolonRBtn);
		group.add(tabRBtn);
		group.add(spaceRBtn);
		group.add(whiteSpaceRBtn);
		addMultiple(btnPanel, commaRBtn, semicolonRBtn, tabRBtn, spaceRBtn, whiteSpaceRBtn);
		
		// Create table
		JTable table = new JTable();
		table.setModel(refs.data.getTableModel(true));
		
		// Radio button action listeners
		commaRBtn.addActionListener(new ParserSeparatorChangeListener((CsvData) refs.data, ',', table));
		semicolonRBtn.addActionListener(new ParserSeparatorChangeListener((CsvData) refs.data, ';', table));
		tabRBtn.addActionListener(new ParserSeparatorChangeListener((CsvData) refs.data, '\t', table));
		spaceRBtn.addActionListener(new ParserSeparatorChangeListener((CsvData) refs.data, ' ', table));
		whiteSpaceRBtn.addActionListener(new ParserSeparatorChangeListener((CsvData) refs.data, '\u0000', table));
		
		// Create table's scroll panel
		JPanel scrollPanel = new JPanel();
		scrollPanel.setLayout(new BorderLayout());
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.add(table);
		scrollPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPanel.setBorder(new TitledBorder("Preview (might be truncated)"));
		
		// Create back and import buttons
		JButton nextBtn = new JButton("Import");
		JButton prevBtn = new JButton("Back");
		
		// Back button closes window
		prevBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				refs.taskManager.execute(new TaskIterator(new ImportTask(refs)));
			}
		});
		
		// Import button goes to column selection
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
				new DataWindow(refs);
			}
		});
		
		// Create layout
		LayoutHelper layout = new LayoutHelper(frame);
		layout.addRow(headerLbl);
		layout.addRow(btnPanel);
		layout.addRow(true, new float[1], scrollPanel);
		layout.addRow(new float[] {1, 4, 1}, prevBtn, Box.createGlue(), nextBtn);
		
		// Show the window
		frame.pack();
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		
	}
	
	private void addMultiple(Container cont, Component... comps) {
		for(Component comp : comps) {
			cont.add(comp);
		}
	}
	
}
