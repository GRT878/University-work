package com.viewingForms;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import net.miginfocom.swing.MigLayout;

import com.basic.DBManager;
import com.basic.JDialogResult;
import com.data.Mater;
import com.editForms.EdSMaterDialog;
import com.gui.GuiHelper;

public class FrmSMater extends JDialog {
	private DBManager manager;
	private JTable tblMaters;
	private JButton btnClose;
	private JButton btnEdit;
	private JButton btnNew;
	private JButton btnDelete;
	private MaterTableModel tblModel;
	private ArrayList<Mater> maters;
	
	public FrmSMater(DBManager manager){
		super();
		this.manager = manager;
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadData();
		createGUI();
		bindListeners();
		pack();
		setTitle("Справочник материалов");
		setLocationRelativeTo(this);
	}
	
	private void loadData() {
		maters = manager.loadMaters();
	}
	
	private void createGUI() {
		JPanel pnl = new JPanel(new MigLayout("insets 3, gapy 4", "[grow, fill]", "[]5[grow, fill]10[]"));	
		tblMaters = new JTable();	
		tblMaters.setModel(tblModel = new MaterTableModel(maters));
		RowSorter<MaterTableModel> sorter = new TableRowSorter<MaterTableModel>(tblModel);
		tblMaters.setRowSorter(sorter);
		tblMaters.setRowSelectionAllowed(true);
		tblMaters.setIntercellSpacing(new Dimension(0, 1));
		tblMaters.setGridColor(new Color(170, 170, 255).darker());
		tblMaters.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		tblMaters.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GuiHelper.initColumnSizes(tblMaters, tblModel);
		JScrollPane scrlPane = new JScrollPane(tblMaters);
		scrlPane.getViewport().setBackground(Color.white);
		scrlPane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(3,0,3,0),scrlPane.getBorder()));
		btnClose = new JButton("Закрыть");
		pnl.add(scrlPane, "grow, span");
		pnl.add(btnClose, "growx 0, right");
		pnl.add(getToolBar(),"growx, wrap");
		pnl.add(new JLabel("Справочник материалов:"), "growx, span");
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
				addMater();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editMater();
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteMater();
			}
		});
	}
	
	private JToolBar getToolBar() {
		JToolBar res = new JToolBar();
		res.setFloatable(false);
		
		URL url = FrmSMater.class.getResource("/images/addNew.png");
		btnNew = new JButton(new ImageIcon(url));
		btnNew.setFocusable(false);
		btnNew.setToolTipText("Добавить новый материал");

		url = FrmSMater.class.getResource("/images/delRow.png");
		btnDelete = new JButton(new ImageIcon(url));
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("Удалить материал");
		
		url = FrmSMater.class.getResource("/images/editRow.png");
		btnEdit = new JButton(new ImageIcon(url));
		btnEdit.setFocusable(false);
		btnEdit.setToolTipText("Изменить данные о материале");
		
		res.add(btnNew);
		res.add(btnEdit);
		res.add(btnDelete);

		return res;
	}
	
	private void editMater() {
		int index = tblMaters.getSelectedRow();
		if (index == -1)
			return;
		
		int modelRow = tblMaters.convertRowIndexToModel(index);
		Mater mater = maters.get(modelRow);
		
		EdSMaterDialog dlg = new EdSMaterDialog(this, mater, manager);
		
		if (dlg.showDialog() == JDialogResult.OK) {
			tblModel.updateRow(modelRow);
			System.out.println("Update mater OK");
		}
	}
	
	private void addMater() {
		EdSMaterDialog dlg = new EdSMaterDialog(this, null, manager);
		if (dlg.showDialog() == JDialogResult.OK) {
			Mater mater = dlg.getMater();
			tblModel.addRow(mater);
		}
	}
	
	private void deleteMater() {
		int index = tblMaters.getSelectedRow();
		if (index == -1)
			return;
		
		if (JOptionPane.showConfirmDialog(this,
				"Удалить материал?", "Подтверждение", 
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) != 
				JOptionPane.YES_OPTION)
			return;
	
		int modelRow = tblMaters.convertRowIndexToModel(index);
		Mater mater = maters.get(modelRow);
		try {
			BigDecimal kod = mater.getKodM();
			if (manager.deleteMater(kod)) {
				tblModel.deleteRow(modelRow);
				System.out.println("Delete mater OK");
			} else
				JOptionPane.showMessageDialog(this, "Ошибка удаления строки", "Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка удаления", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class MaterTableModel extends AbstractTableModel {
		private ArrayList<Mater> maters;
		
		public MaterTableModel(ArrayList<Mater> maters) {
			this.maters = maters;
		}
	  
		@Override
		public int getColumnCount() {
			return 4;
		}
		
		@Override
		public int getRowCount() {
			return (maters == null ? 0 : maters.size());
		}
		
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Mater m = maters.get(rowIndex);
			switch (columnIndex) {
				case 0:
					return rowIndex + 1;
				case 1:
					return m.getKodM();
				case 2:
					return m.getNaimM();
				case 3:
					return m.getCenaEd();
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
				case 3:
					return "Цена за ед.";
				default:
					return null;
			}
		}
		
		public Class getColumnClass(int c) {
			if (c == 3)
				return java.math.BigDecimal.class;
			else
				return getValueAt(0, c).getClass();
		}
		
		public void addRow(Mater mater) {
			int len = maters.size();
			maters.add(mater);
			fireTableRowsInserted(len, len);
		}
		
		public void updateRow(int index) {
			fireTableRowsUpdated(index, index);
		}

		public void deleteRow(int index) {
			if (index != maters.size() - 1)
				fireTableRowsUpdated(index + 1, maters.size() - 1);
			maters.remove(index);
			fireTableRowsDeleted(index, index);
		}
	}
}

