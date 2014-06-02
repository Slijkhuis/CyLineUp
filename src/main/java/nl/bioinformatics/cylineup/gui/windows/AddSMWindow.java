package nl.bioinformatics.cylineup.gui.windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import nl.bioinformatics.cylineup.gui.ColumnItem;
import nl.bioinformatics.cylineup.gui.LayoutHelper;
import nl.bioinformatics.cylineup.gui.SwingHelper;
import nl.bioinformatics.cylineup.models.SMTableModel;
import nl.bioinformatics.cylineup.models.SmallMultiple;

public class AddSMWindow extends JFrame {
	private static final long serialVersionUID = 2572033732016176520L;

	public AddSMWindow(ColumnItem[] cols, int defaultIdCol, final SMTableModel tModel) {
		
		/** Create GUI elements **/
		
		final JButton addButton = new JButton("Add");
		
		final JTextField viewName = new JTextField();
		
		final JComboBox<ColumnItem> viewId = new JComboBox<ColumnItem>();
		
		final JComboBox<ColumnItem> viewVal = new JComboBox<ColumnItem>();
		
		final JComboBox<ColumnItem> viewPVal = new JComboBox<ColumnItem>();
		
		/** Create layout **/
		// Get helper
		LayoutHelper layout = new LayoutHelper(this);
		
		layout.addRow(new JLabel("<html><b>ADD SMALL MULTIPLE VIEW</b></html>"));
		
		layout.addRow(new JLabel("<html><p>View name</p></html>"));
		layout.addRow(viewName);
		
		layout.addRow(new JLabel("<html><p>Identifier column</p></html>"));
		layout.addRow(viewId);
		
		layout.addRow(new JLabel("<html><p>Value column (e.g., fold change)</p></html>"));
		layout.addRow(viewVal);
		
		layout.addRow(new JLabel("<html><p>Statistical relevance column (e.g. p-value)</p></html>"));
		layout.addRow(viewPVal);
		
		layout.addRow(true, new float[] {1f}, Box.createGlue());
		layout.addRow(addButton);
		
		/** Set actions and default values **/
		viewId.setModel(new DefaultComboBoxModel<ColumnItem>(cols));
		viewVal.setModel(new DefaultComboBoxModel<ColumnItem>(cols));
		viewPVal.setModel(new DefaultComboBoxModel<ColumnItem>(cols));
		
		if(defaultIdCol >= 0) {
			((DefaultComboBoxModel<ColumnItem>) viewId.getModel()).setSelectedItem(cols[defaultIdCol]);
		}
		
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Add item to the table model
				tModel.smallMultiples.add(new SmallMultiple(
						viewName.getText(),
						tModel.smallMultiples.size(),
						(ColumnItem) viewId.getSelectedItem(),
						(ColumnItem) viewVal.getSelectedItem(),
						(ColumnItem) viewPVal.getSelectedItem(),
						false
				));
				
				// Refresh table
				tModel.fireTableDataChanged();
				
				// Dispose window
				dispose();
			}
		});
		
		/** Show window **/
		SwingHelper.centerWindow(this, 300);
		pack();
		setVisible(true);
		setAlwaysOnTop(true);
	}
	
	
}
