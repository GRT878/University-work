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
import com.data.Klient;
import com.editForms.EdSKlientDialog;
import com.gui.GuiHelper;

public class FrmSKlient extends JDialog{
	private DBManager manager;
	private JTable tblKlients;
	private JButton btnClose;
	private JButton btnEdit;
	private JButton btnNew;
	private JButton btnDelete;
	private KlientTableModel tblModel;
	private ArrayList<Klient> klients;
	
	public FrmSKlient(DBManager manager){
		super();
		this.manager = manager;
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadData();
		createGUI();
		bindListeners();
		pack();
		setTitle("Справочник клиентов");
		setLocationRelativeTo(this);
	}
	
	private void loadData() {
		klients = manager.loadKlients();
	}
	
	private void createGUI() {
		JPanel pnl = new JPanel(new MigLayout("insets 3, gapy 4", "[grow, fill]", "[]5[grow, fill]10[]"));	
		tblKlients = new JTable();	
		tblKlients.setModel(tblModel = new KlientTableModel(klients));
		RowSorter<KlientTableModel> sorter = new TableRowSorter<KlientTableModel>(tblModel);
		tblKlients.setRowSorter(sorter);
		tblKlients.setRowSelectionAllowed(true);
		tblKlients.setIntercellSpacing(new Dimension(0, 1));
		tblKlients.setGridColor(new Color(170, 170, 255).darker());
		tblKlients.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		tblKlients.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GuiHelper.initColumnSizes(tblKlients, tblModel);
		JScrollPane scrlPane = new JScrollPane(tblKlients);
		scrlPane.getViewport().setBackground(Color.white);
		scrlPane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(3,0,3,0),scrlPane.getBorder()));
		btnClose = new JButton("Закрыть");
		pnl.add(scrlPane, "grow, span");
		pnl.add(btnClose, "growx 0, right");
		pnl.add(getToolBar(),"growx,wrap");
		pnl.add(new JLabel("Справочник клиентов:"), "growx,span");
		pnl.add(scrlPane, "grow, span, w 600");
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
				addKlient();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editKlient();
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteKlient();
			}
		});
	}
	
	private JToolBar getToolBar() {
		JToolBar res = new JToolBar();
		res.setFloatable(false);

		URL url = FrmSKlient.class.getResource("/images/addNew.png");
		btnNew = new JButton(new ImageIcon(url));
		btnNew.setFocusable(false);
		btnNew.setToolTipText("Добавить нового клиента");

		url = FrmSKlient.class.getResource("/images/delRow.png");
		btnDelete = new JButton(new ImageIcon(url));
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("Удалить клиента");
		
		url = FrmSKlient.class.getResource("/images/editRow.png");
		btnEdit = new JButton(new ImageIcon(url));
		btnEdit.setFocusable(false);
		btnEdit.setToolTipText("Изменить данные о клиенте");

		res.add(btnNew);
		res.add(btnEdit);
		res.add(btnDelete);

		return res;
	}
	
	private void editKlient() {
		int index = tblKlients.getSelectedRow();
		if (index == -1)
			return;
		
		int modelRow = tblKlients.convertRowIndexToModel(index);
		Klient klient = klients.get(modelRow);
		EdSKlientDialog dlg = new EdSKlientDialog(this, klient, manager);	
		if (dlg.showDialog() == JDialogResult.OK) {
			tblModel.updateRow(modelRow);
			System.out.println("Update klient OK");
		}
	}
	
	private void addKlient() {
		EdSKlientDialog dlg = new EdSKlientDialog(this, null, manager);
		if (dlg.showDialog() == JDialogResult.OK) {
			Klient klient = dlg.getKlient();
			tblModel.addRow(klient);
		}
	}
	
	private void deleteKlient() {
		int index = tblKlients.getSelectedRow();
		if (index == -1)
			return;
		
		if (JOptionPane.showConfirmDialog(this,
				"Удалить клиента?", "Подтверждение", 
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) != 
				JOptionPane.YES_OPTION)
			return;
	
		int modelRow = tblKlients.convertRowIndexToModel(index);
		Klient klient = klients.get(modelRow);
		try {
			BigDecimal kod = klient.getKodKl();
			if (manager.deleteKlient(kod)) {
				tblModel.deleteRow(modelRow);
				System.out.println("Delete klient OK");
			} else
				JOptionPane.showMessageDialog(this, "Ошибка удаления строки", "Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка удаления", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class KlientTableModel extends AbstractTableModel {
		private ArrayList<Klient> klients;
		
		public KlientTableModel(ArrayList<Klient> klients) {
			this.klients = klients;
		}
	  
		@Override
		public int getColumnCount() {
			return 5;
		}
		
		@Override
		public int getRowCount() {
			return (klients == null ? 0 : klients.size());
		}
		
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Klient k = klients.get(rowIndex);
			switch (columnIndex) {
				case 0:
					return rowIndex + 1;
				case 1:
					return k.getKodKl();
				case 2:
					return k.getNaimKl();
				case 3:
					return k.getDoc();
				case 4:
					return k.getTel();
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
					return "Номер док.";
				case 4:
					return "Телефон";
				default:
					return null;
			}
		}
		
		public Class getColumnClass(int c) {	
			return getValueAt(0, c).getClass();
		}
		
		public void addRow(Klient klient) {
			int len = klients.size();
			klients.add(klient);
			fireTableRowsInserted(len, len);
		}
		
		public void updateRow(int index) {
			fireTableRowsUpdated(index, index);
		}

		public void deleteRow(int index) {
			if (index != klients.size() - 1)
				fireTableRowsUpdated(index + 1, klients.size() - 1);
			klients.remove(index);
			fireTableRowsDeleted(index, index);
		}
	}
}
