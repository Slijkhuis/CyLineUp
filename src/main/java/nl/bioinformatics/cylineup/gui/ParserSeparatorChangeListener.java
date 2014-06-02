package nl.bioinformatics.cylineup.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTable;

import nl.bioinformatics.cylineup.data.CsvData;

public class ParserSeparatorChangeListener implements ActionListener {
	
	private char delimiter;
	private JTable table;
	private CsvData f;
	
	public ParserSeparatorChangeListener(CsvData csvf, char c, JTable jtable) {
		delimiter = c;
		table = jtable;
		f = csvf;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		f.setDelimiter(delimiter);
		table.setModel(f.getTableModel(true));
	}

}
