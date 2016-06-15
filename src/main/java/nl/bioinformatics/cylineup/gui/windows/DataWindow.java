package nl.bioinformatics.cylineup.gui.windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.cytoscape.work.FinishStatus;
import org.cytoscape.work.ObservableTask;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskObserver;

import nl.bioinformatics.cylineup.CyLineUpReferences;
import nl.bioinformatics.cylineup.gui.ColumnItem;
import nl.bioinformatics.cylineup.gui.JTableButtonMouseListener;
import nl.bioinformatics.cylineup.gui.JTableButtonRenderer;
import nl.bioinformatics.cylineup.gui.LayoutHelper;
import nl.bioinformatics.cylineup.gui.SwingHelper;
import nl.bioinformatics.cylineup.models.NodeTableColumnsComboModel;
import nl.bioinformatics.cylineup.models.SMTableModel;
import nl.bioinformatics.cylineup.models.SmallMultiple;
import nl.bioinformatics.cylineup.models.TableRowTransferHandler;
import nl.bioinformatics.cylineup.tasks.CreateTask;

public class DataWindow extends JFrame {

	/**
	 * Generated serial UID
	 */
	private static final long serialVersionUID = 7487782149279840928L;
	
	private int defaultIdColumn = 0;
	
	public DataWindow(final CyLineUpReferences refs) {
		
		/** Create GUI elements **/
		
		// Column names check box
		final JCheckBox headersChk = new JCheckBox("<html><p>First row contains column headers.</p></html>");
		
		// Create table identifier drop down box
		final JComboBox<String> idCmb = new JComboBox<String>(new NodeTableColumnsComboModel(refs));
		
		// Small multiple table
		final JTable table = new JTable();
		
		// Create buttons for adding Small Multiples
		final JButton autoAddButton = new JButton("<html><p>Auto-detect from data</p></html>");
		final JButton addButton = new JButton("<html><p>Add Small Multiple View</p></html>");
		
		// Create control drop down
		final JCheckBox controlChk = new JCheckBox("<html><p>Values are already relative to a control.</p></html>");
		final JLabel controlLbl = new JLabel("N/A");
		
		// Create submit button
		final JButton nextBtn = new JButton("<html><p>Create small multiples</p></html>");
		
		/** Create layout **/
		
		// Get helper
		LayoutHelper layout = new LayoutHelper(this);
		
		layout.addRow(new JLabel("<html><b>DATA BINDING</b></html>"));
		
		layout.addRow(new JLabel("<html><b>Identifier column in network table</b></html>"));
		layout.addRow(idCmb);
		
		layout.addRow(new JLabel("<html><b>Column Headers</b></html>"));
		layout.addRow(headersChk);
		
		layout.addRow(new JLabel("<html><b>Small Multiple Views</b></html>"));
		layout.addRow(true, new float[] {1f}, new JScrollPane(table));
		layout.addRow(new float[] {2f, 1f, 1f}, Box.createGlue(), autoAddButton, addButton);
		
		layout.addRow(new JLabel("<html><b>Control values</b></html>"));
		layout.addRow(controlLbl);
		layout.addRow(controlChk);
		
		layout.addRow(nextBtn);
		
		/** Set actions and (default) values **/
		
		// Network table ID column chooser action
		idCmb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refs.idColName = idCmb.getSelectedItem().toString();
				refs.idColPos = idCmb.getSelectedIndex();
			}
		});
		
		// Default value of 'first column contains headers' is true
		headersChk.setSelected(true);
		
		// Set table model
		final SMTableModel tModel = new SMTableModel();
		table.setModel(tModel);
		
		// Only allow single rows to be selected in the table
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Add a mouse listener that routes button clicks to the the appropriate button 
		table.addMouseListener(new JTableButtonMouseListener(table));
		
		// Set table button columns to the right cell renderer
		table.getColumn("Actions").setCellRenderer(new JTableButtonRenderer());
		
		// Enable drag-n-drop
		table.setDragEnabled(true);
		table.setDropMode(DropMode.INSERT_ROWS);
		table.setTransferHandler(new TableRowTransferHandler(table));
		
		// Table row height
		table.setRowHeight(25);
		
		// Table switch row selection handler
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(!controlChk.isSelected()) {
					if(table.getSelectedRow() == -1) {
						controlLbl.setText("<html><p>-- Please select a row from the table. --</p></html>");
					} else {
						SmallMultiple row = tModel.smallMultiples.get(table.getSelectedRow());
						controlLbl.setText(row.getName());
					}
				}
			}
		});
		
		// Set Add button handler
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AddSMWindow(refs.data.getColumnItems(headersChk.isSelected()), defaultIdColumn, tModel);
			}
		});
		
		// Set auto-detect button action
		autoAddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(tModel.getRowCount() > 0) {
					JOptionPane.showMessageDialog(DataWindow.this, "Please delete the other rows first before using this function.");
				} else {
					// Get headers
					String[] headers = refs.data.getColumnNames(headersChk.isSelected());
					
					// Loop over columns
					for(int i = 0; i < headers.length; i++) {
						
						// Only process even rows
						if((i & 1) != 0) {
							// Check if we can assemble a row
							if(i + 1 < headers.length) {
								
								// Add table row
								tModel.smallMultiples.add(new SmallMultiple(
										headers[i],
										i,
										new ColumnItem(headers[0], 0),
										new ColumnItem(headers[i], i),
										new ColumnItem(headers[i+1], i+1),
										false
									)
								);
								
								// Update table
								tModel.fireTableDataChanged();
							}
						}
					}
				}
			}
		});
		
		// Disable control values drop down and check the corresponding check box
		controlChk.setSelected(true);
		
		// Set action for 'already relative to control' check box
		controlChk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(controlChk.isSelected()) {
					controlLbl.setText("<html><p>N/A</p></html>");
				} else {
					int selectedRow = table.getSelectedRow();
					if(selectedRow == -1) {
						controlLbl.setText("<html><p>-- Please select a row from the table. --</p></html>");
					} else {
						SmallMultiple row = tModel.smallMultiples.get(selectedRow);
						controlLbl.setText(row.getName());
					}
				}
			}
		});
		
		// 'Create SMs' button action
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				// Run creation task
				CreateTask task = new CreateTask(tModel.smallMultiples, refs);
				refs.taskManager.execute(new TaskIterator(task), new TaskObserver() {
					@Override
					public void taskFinished(ObservableTask task) {
					}
					@Override
					public void allFinished(FinishStatus status) {
						if(status.getType() == FinishStatus.Type.SUCCEEDED) {
							SwingHelper.arrangeWindows(refs);
						}
					}
				});
				
				// Close current window
				dispose();
				
			}
		});
		
		/** Show the window **/
		
		SwingHelper.centerWindow(this, 640);
		pack();
		setVisible(true);
		setAlwaysOnTop(true);
		
	}
	
}
