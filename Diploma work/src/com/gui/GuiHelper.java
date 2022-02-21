package com.gui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class GuiHelper {
	public static void initColumnSizes(JTable tbl, AbstractTableModel tblModel) {
		if (tbl.getTableHeader() == null) return;
		if (tbl.getRowCount()==0)  return;
		TableCellRenderer headerRenderer = tbl.getTableHeader().getDefaultRenderer();
		for (int i = 0; i < tblModel.getColumnCount(); i++) {
			TableColumn column = tbl.getColumnModel().getColumn(i);
			Component comp = headerRenderer.getTableCellRendererComponent(null,column.getHeaderValue(), false, false, 0, 0);
			int headerWidth = comp.getPreferredSize().width;
			int cellWidth = 40;
			Class<?> _class = tblModel.getColumnClass(i);
			for (int j = 0; j < tblModel.getRowCount(); j++){
				comp=tbl.getDefaultRenderer(_class).getTableCellRendererComponent(tbl, tblModel.getValueAt(j, i), false, false, 0, i);
				cellWidth = Math.max(comp.getPreferredSize().width, cellWidth);
			}
			column.setPreferredWidth(Math.max(headerWidth, cellWidth) + 10);
		}
	}
}
