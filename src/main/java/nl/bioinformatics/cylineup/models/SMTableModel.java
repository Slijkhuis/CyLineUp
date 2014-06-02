package nl.bioinformatics.cylineup.models;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

public class SMTableModel extends AbstractTableModel implements Reorderable {
	private static final long serialVersionUID = -142778442600139079L;
	
	private String[] columnNames = new String[] {"Name", "Identifier Column", "Value (e.g. fold change)", "p-value", "Actions"};
	public List<SmallMultiple> smallMultiples = new ArrayList<SmallMultiple>();
	
	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public int getRowCount() {
		return smallMultiples.size();
	}

    @Override public Class<?> getColumnClass(int col) {
        if(col < 4) {
        	return String.class;
        } else {
        	return JButton.class;
        }
    }
	
    @Override public String getColumnName(int col) {
    	return columnNames[col];
    }
    
	@Override
	public Object getValueAt(final int row, final int col) {
		
		SmallMultiple sm = smallMultiples.get(row);
		
		switch (col) {
		case 0:
			return sm.getName();
		case 1:
			return sm.getIdColumn().toString();
		case 2:
			return sm.getValueColumn().toString();
		case 3:
			return sm.getPValueColumn().toString();
		case 4:
			final JButton button = new JButton("Delete");
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                	smallMultiples.remove(row);
                	fireTableDataChanged();
                	System.out.println("Button clicked for row " + row);
                }
            });
            return button;
		default:
			return null;
		}
	}
	
	@Override
	public void reorder(int fromIndex, int toIndex) {
		// Check whether we need to move the row up or down
		if(fromIndex >= toIndex) {
			// Move the row up
			for(int i = fromIndex; i > toIndex; i--) {
				Collections.swap(smallMultiples, i, i - 1);
			}
		} else {
			// Move the row down
			toIndex--;
			for(int i = fromIndex; i < toIndex; i++) {
				Collections.swap(smallMultiples, i, i + 1);
			}
		}
		fireTableDataChanged();
	}
}
