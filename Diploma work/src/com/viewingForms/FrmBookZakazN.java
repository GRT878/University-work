package com.viewingForms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import com.basic.DBManager;
import com.basic.JDialogResult;
import com.data.ZakazN;
import com.editForms.EdZakazNBookDialog;
import com.gui.GuiHelper;

import net.miginfocom.swing.MigLayout;

public class FrmBookZakazN extends JDialog{
	private DBManager manager;
	private JTable tblZakazNs;
	private JButton btnClose;
	private JButton btnEdit;
	private JButton btnNew;
	private JButton btnDelete;
	private ZakazNTableModel tblModel;
	private ArrayList<ZakazN> zakazNs;
	
	public FrmBookZakazN(DBManager manager){
		super();
		this.manager = manager;
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadData();
		createGUI();
		bindListeners();
		pack();
		setTitle("Книга заказ-нарядов");
		setLocationRelativeTo(this);
	}
	
	private void loadData() {
		zakazNs = manager.loadZakazNs();
	}
	
	private void createGUI() {
		JPanel pnl = new JPanel(new MigLayout("insets 3, gapy 4", "[grow, fill]", "[]5[grow, fill]10[]"));	
		tblZakazNs = new JTable();	
		tblZakazNs.setModel(tblModel = new ZakazNTableModel(zakazNs));
		RowSorter<ZakazNTableModel> sorter = new TableRowSorter<ZakazNTableModel>(tblModel);
		tblZakazNs.setRowSorter(sorter);
		tblZakazNs.setRowSelectionAllowed(true);
		tblZakazNs.setIntercellSpacing(new Dimension(0, 1));
		tblZakazNs.setGridColor(new Color(170, 170, 255).darker());
		tblZakazNs.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		tblZakazNs.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GuiHelper.initColumnSizes(tblZakazNs, tblModel);
		JScrollPane scrlPane = new JScrollPane(tblZakazNs);
		scrlPane.getViewport().setBackground(Color.white);
		scrlPane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(3,0,3,0),scrlPane.getBorder()));
		btnClose = new JButton("Закрыть");
		pnl.add(scrlPane, "grow, span");
		pnl.add(btnClose, "growx 0, right");
		pnl.add(getToolBar(),"growx,wrap");
		pnl.add(new JLabel("Книга заказ-нарядов:"), "growx,span");
		pnl.add(scrlPane, "grow, span, w 1000");
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
				addZakazN();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editZakazN();
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteZakazN();
			}
		});
	}
	
	private JToolBar getToolBar() {
		JToolBar res = new JToolBar();
		res.setFloatable(false);

		URL url = FrmBookZakazN.class.getResource("/images/addNew.png");
		btnNew = new JButton(new ImageIcon(url));
		btnNew.setFocusable(false);
		btnNew.setToolTipText("Добавить новый заказ-наряд");

		url = FrmBookZakazN.class.getResource("/images/delRow.png");
		btnDelete = new JButton(new ImageIcon(url));
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("Удалить заказ-наряд");
		
		url = FrmBookZakazN.class.getResource("/images/editRow.png");
		btnEdit = new JButton(new ImageIcon(url));
		btnEdit.setFocusable(false);
		btnEdit.setToolTipText("Изменить данные о заказе-наряде");

		res.add(btnNew);
		res.add(btnEdit);
		res.add(btnDelete);

		return res;
	}
	
	private void editZakazN() {
		int index = tblZakazNs.getSelectedRow();
		if (index == -1)
			return;
		
		int modelRow = tblZakazNs.convertRowIndexToModel(index);
		ZakazN zakazN = zakazNs.get(modelRow);
		
		EdZakazNBookDialog dlg = new EdZakazNBookDialog(this, zakazN, manager);
		
		if (dlg.showDialog() == JDialogResult.OK) {
			tblModel.updateRow(modelRow);
			System.out.println("Update zakazN OK");
		}
	}
	
	private void addZakazN() {
		EdZakazNBookDialog dlg = new EdZakazNBookDialog(this, null, manager);
		dlg.showDialog();
		ZakazN zakazN = dlg.getZakazN();
		if (zakazN.getNomZn() != null)
			tblModel.addRow(zakazN);	
	}
	
	private void deleteZakazN() {
		int index = tblZakazNs.getSelectedRow();
		if (index == -1)
			return;
		
		if (JOptionPane.showConfirmDialog(this,
				"Удалить заказ-наряд?", "Подтверждение", 
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) != 
				JOptionPane.YES_OPTION)
			return;
	
		int modelRow = tblZakazNs.convertRowIndexToModel(index);
		ZakazN zakazN = zakazNs.get(modelRow);
		try {
			BigDecimal kod = zakazN.getIdZn();
			if (manager.deleteZakazN(kod)) {
				tblModel.deleteRow(modelRow);
				System.out.println("Delete zakazN OK");
			} else
				JOptionPane.showMessageDialog(this, "Ошибка удаления строки", "Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка удаления", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class ZakazNTableModel extends AbstractTableModel {
		private ArrayList<ZakazN> zakazNs;
		
		public ZakazNTableModel(ArrayList<ZakazN> zakazNs) {
			this.zakazNs = zakazNs;
		}
	  
		@Override
		public int getColumnCount() {
			return 10;
		}
		
		@Override
		public int getRowCount() {
			return (zakazNs == null ? 0 : zakazNs.size());
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			ZakazN zn = zakazNs.get(rowIndex);
			switch (columnIndex) {
				case 0:
					return rowIndex + 1;
				case 1:
					return zn.getNomZn();
				case 2:
					return (zn.getZakaz() == null ? "" : zn.getZakaz().getNomZak());
				case 3:
					return (zn.getSotrud() == null ? "" : zn.getSotrud().getFio());
				case 4:
					return zn.getDataOform();
				case 5:
					return zn.getSrokIsp();
				case 6:
					return zn.getModel();
				case 7:
					return zn.getRegNom();
				case 8:
					return zn.getStRab();
				case 9:
					return zn.getStM();
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
					return "№ заказ-наряда";
				case 2:
					return "№ заказа";
				case 3:
					return "Сотрудник";
				case 4:
					return "Дата оформ.";
				case 5:
					return "Срок исп.";
				case 6:
					return "Модель авто";
				case 7:
					return "Рег. номер";
				case 8:
					return "Ст. работ";
				case 9:
					return "Ст. материалов";
				default:
					return null;
			}
		}
		
		public Class getColumnClass(int c) {	
			return getValueAt(0, c).getClass();
		}
		
		public void addRow(ZakazN zakazN) {
			int len = zakazNs.size();
			manager.refreshZakazNBookRow(zakazN);
			zakazNs.add(zakazN);
			fireTableRowsInserted(len, len);
		}
		
		public void updateRow(int index) {
			fireTableRowsUpdated(index, index);
		}

		public void deleteRow(int index) {
			if (index != zakazNs.size() - 1)
				fireTableRowsUpdated(index + 1, zakazNs.size() - 1);
			zakazNs.remove(index);
			fireTableRowsDeleted(index, index);
		}
	}
}
