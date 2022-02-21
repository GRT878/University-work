package com.viewingForms;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import net.miginfocom.swing.MigLayout;

import com.basic.DBManager;
import com.basic.JDialogResult;
import com.data.Dolj;
import com.editForms.EdSDoljDialog;
import com.gui.GuiHelper;

public class FrmSDolj extends JDialog{
	private DBManager manager;
	private JTable tblDoljs;
	private JButton btnClose;
	private JButton btnEdit;
	private JButton btnNew;
	private JButton btnDelete;
	private DoljTableModel tblModel;
	private ArrayList<Dolj> doljs;
	
	public FrmSDolj(DBManager manager){
		super();
		this.manager = manager;
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadData();
		createGUI();
		bindListeners();
		pack();
		setTitle("Справочник должностей");
		setLocationRelativeTo(this);
	}
	
	private void loadData() {
		doljs = manager.loadDoljs();
	}
	
	private void createGUI() {
		JPanel pnl = new JPanel(new MigLayout("insets 3, gapy 4", "[grow, fill]", "[]5[grow, fill]10[]"));	
		tblDoljs = new JTable();	
		tblDoljs.setModel(tblModel = new DoljTableModel(doljs));
		RowSorter<DoljTableModel> sorter = new TableRowSorter<DoljTableModel>(tblModel);
		tblDoljs.setRowSorter(sorter);
		tblDoljs.setRowSelectionAllowed(true);
		tblDoljs.setIntercellSpacing(new Dimension(0, 1));
		tblDoljs.setGridColor(new Color(170, 170, 255).darker());
		tblDoljs.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		tblDoljs.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GuiHelper.initColumnSizes(tblDoljs, tblModel);
		JScrollPane scrlPane = new JScrollPane(tblDoljs);
		scrlPane.getViewport().setBackground(Color.white);
		scrlPane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(3,0,3,0),scrlPane.getBorder()));
		btnClose = new JButton("Закрыть");
		pnl.add(scrlPane, "grow, span");
		pnl.add(btnClose, "growx 0, right");
		pnl.add(getToolBar(),"growx,wrap");
		pnl.add(new JLabel("Справочник должностей:"), "growx,span");
		pnl.add(scrlPane, "grow, span");
		pnl.add(btnClose, "growx 0, right");
		getContentPane().setLayout(new MigLayout("insets 0 2 0 2, gapy 0", "[grow, fill]", "[grow, fill]"));
		getContentPane().add(pnl, "grow");
	}
	
	private void bindListeners() {
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addDolj();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editDolj();
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteDolj();
			}
		});
	}
	
	private JToolBar getToolBar() {
		JToolBar res = new JToolBar();
		res.setFloatable(false);

		URL url = FrmSDolj.class.getResource("/images/addNew.png");
		btnNew = new JButton(new ImageIcon(url));
		btnNew.setFocusable(false);
		btnNew.setToolTipText("Добавить новую должность");

		url = FrmSDolj.class.getResource("/images/delRow.png");
		btnDelete = new JButton(new ImageIcon(url));
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("Удалить должность");
		
		url = FrmSDolj.class.getResource("/images/editRow.png");
		btnEdit = new JButton(new ImageIcon(url));
		btnEdit.setFocusable(false);
		btnEdit.setToolTipText("Изменить данные о должности");

		res.add(btnNew);
		res.add(btnEdit);
		res.add(btnDelete);

		return res;
	}
	
	private void editDolj() {
		int index = tblDoljs.getSelectedRow();
		if (index == -1)
			return;
		
		int modelRow = tblDoljs.convertRowIndexToModel(index);
		Dolj dolj = doljs.get(modelRow);
		EdSDoljDialog dlg = new EdSDoljDialog(this, dolj, manager);
		if (dlg.showDialog() == JDialogResult.OK) {
			tblModel.updateRow(modelRow);
			System.out.println("Update dolj OK");
		}
	}
	
	private void addDolj() {
		EdSDoljDialog dlg = new EdSDoljDialog(this, null, manager);
		if (dlg.showDialog() == JDialogResult.OK) {
			Dolj dolj = dlg.getDolj();
			tblModel.addRow(dolj);
		}
	}
	
	private void deleteDolj() {
		int index = tblDoljs.getSelectedRow();
		if (index == -1)
			return;
		
		if (JOptionPane.showConfirmDialog(this,
				"Удалить должность?", "Подтверждение", 
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) != 
				JOptionPane.YES_OPTION)
			return;
	
		int modelRow = tblDoljs.convertRowIndexToModel(index);
		Dolj dolj = doljs.get(modelRow);
		try {
			BigDecimal kod = dolj.getKodDol();
			if (manager.deleteDolj(kod)) {
				tblModel.deleteRow(modelRow);
				System.out.println("Delete dolj OK");
			} else
				JOptionPane.showMessageDialog(this, "Ошибка удаления строки", "Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка удаления", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class DoljTableModel extends AbstractTableModel {
		private ArrayList<Dolj> doljs;
		
		public DoljTableModel(ArrayList<Dolj> doljs) {
			this.doljs = doljs;
		}
	  
		@Override
		public int getColumnCount() {
			return 3;
		}
		
		@Override
		public int getRowCount() {
			return (doljs == null ? 0 : doljs.size());
		}
		
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Dolj d = doljs.get(rowIndex);
			switch (columnIndex) {
				case 0:
					return rowIndex + 1;
				case 1:
					return d.getKodDol();
				case 2:
					return d.getNaimDol();
				default:
					return null;
			}
		}
		
		@Override
		public String getColumnName(int column) {
			switch (column) {
				case 0:
					return "N";
				case 1:
					return "Код";
				case 2:
					return "Наименование";
				default:
					return null;
			}
		}
		
		public Class getColumnClass(int c) {	
			return getValueAt(0, c).getClass();
		}
		
		public void addRow(Dolj dolj) {
			int len = doljs.size();
			doljs.add(dolj);
			fireTableRowsInserted(len, len);
		}
		
		public void updateRow(int index) {
			fireTableRowsUpdated(index, index);
		}

		public void deleteRow(int index) {
			if (index != doljs.size() - 1)
				fireTableRowsUpdated(index + 1, doljs.size() - 1);
			doljs.remove(index);
			fireTableRowsDeleted(index, index);
		}
	}
}
