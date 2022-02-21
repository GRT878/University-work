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
import com.data.Zakaz;
import com.editForms.EdZakazBookDialog;
import com.gui.GuiHelper;

import net.miginfocom.swing.MigLayout;

public class FrmBookZakaz extends JDialog{
	private DBManager manager;
	private JTable tblZakazs;
	private JButton btnClose;
	private JButton btnEdit;
	private JButton btnNew;
	private JButton btnDelete;
	private ZakazTableModel tblModel;
	private ArrayList<Zakaz> zakazs;
	
	public FrmBookZakaz(DBManager manager){
		super();
		this.manager = manager;
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadData();
		createGUI();
		bindListeners();
		pack();
		setTitle("Книга заказов");
		setLocationRelativeTo(this);
	}
	
	private void loadData() {
		zakazs = manager.loadZakazs();
	}
	
	private void createGUI() {
		JPanel pnl = new JPanel(new MigLayout("insets 3, gapy 4", "[grow, fill]", "[]5[grow, fill]10[]"));	
		tblZakazs = new JTable();	
		tblZakazs.setModel(tblModel = new ZakazTableModel(zakazs));
		RowSorter<ZakazTableModel> sorter = new TableRowSorter<ZakazTableModel>(tblModel);
		tblZakazs.setRowSorter(sorter);
		tblZakazs.setRowSelectionAllowed(true);
		tblZakazs.setIntercellSpacing(new Dimension(0, 1));
		tblZakazs.setGridColor(new Color(170, 170, 255).darker());
		tblZakazs.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		tblZakazs.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GuiHelper.initColumnSizes(tblZakazs, tblModel);
		JScrollPane scrlPane = new JScrollPane(tblZakazs);
		scrlPane.getViewport().setBackground(Color.white);
		scrlPane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(3,0,3,0),scrlPane.getBorder()));
		btnClose = new JButton("Закрыть");
		pnl.add(scrlPane, "grow, span");
		pnl.add(btnClose, "growx 0, right");
		pnl.add(getToolBar(),"growx, wrap");
		pnl.add(new JLabel("Книга заказов:"), "growx,span");
		pnl.add(scrlPane, "grow, span, w 1200");
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
				addZakaz();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editZakaz();
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteZakaz();
			}
		});
	}
	
	private JToolBar getToolBar() {
		JToolBar res = new JToolBar();
		res.setFloatable(false);

		URL url = FrmSMater.class.getResource("/images/addNew.png");
		btnNew = new JButton(new ImageIcon(url));
		btnNew.setFocusable(false);
		btnNew.setToolTipText("Добавить новый заказ");

		url = FrmSMater.class.getResource("/images/delRow.png");
		btnDelete = new JButton(new ImageIcon(url));
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("Удалить заказ");
		
		url = FrmSMater.class.getResource("/images/editRow.png");
		btnEdit = new JButton(new ImageIcon(url));
		btnEdit.setFocusable(false);
		btnEdit.setToolTipText("Изменить данные о заказе");

		res.add(btnNew);
		res.add(btnEdit);
		res.add(btnDelete);

		return res;
	}
	
	private void editZakaz() {
		int index = tblZakazs.getSelectedRow();
		if (index == -1)
			return;
		
		int modelRow = tblZakazs.convertRowIndexToModel(index);
		Zakaz zakaz = zakazs.get(modelRow);
		
		EdZakazBookDialog dlg = new EdZakazBookDialog(this, zakaz, manager);
		
		if (dlg.showDialog() == JDialogResult.OK) {
			tblModel.updateRow(modelRow);
			System.out.println("Update zakaz OK");
		}
	}
	
	private void addZakaz() {
		EdZakazBookDialog dlg = new EdZakazBookDialog(this, null, manager);
		if (dlg.showDialog() == JDialogResult.OK) {
			Zakaz zakaz = dlg.getZakaz();
			tblModel.addRow(zakaz);
		}
	}
	
	private void deleteZakaz() {
		int index = tblZakazs.getSelectedRow();
		if (index == -1)
			return;
		
		if (JOptionPane.showConfirmDialog(this,
				"Удалить заказ?", "Подтверждение", 
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) != 
				JOptionPane.YES_OPTION)
			return;
	
		int modelRow = tblZakazs.convertRowIndexToModel(index);
		Zakaz zakaz = zakazs.get(modelRow);
		try {
			BigDecimal kod = zakaz.getIdZak();
			if (manager.deleteZakaz(kod)) {
				tblModel.deleteRow(modelRow);
				System.out.println("Delete zakaz OK");
			} else
				JOptionPane.showMessageDialog(this, "Ошибка удаления строки", "Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка удаления", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class ZakazTableModel extends AbstractTableModel {
		private ArrayList<Zakaz> zakazs;
		
		public ZakazTableModel(ArrayList<Zakaz> zakazs) {
			this.zakazs = zakazs;
		}
	  
		@Override
		public int getColumnCount() {
			return 8;
		}
		
		@Override
		public int getRowCount() {
			return (zakazs == null ? 0 : zakazs.size());
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Zakaz z = zakazs.get(rowIndex);
			switch (columnIndex) {
				case 0:
					return rowIndex + 1;
				case 1:
					return z.getNomZak();
				case 2:
					return (z.getSotrud() == null ? "" : z.getSotrud().getFio());
				case 3:
					return z.getDataObr();
				case 4:
					return (z.getKlient() == null ? "" : z.getKlient().getNaimKl());
				case 5:
					return z.getPrObr();
				case 6:
					return z.getPrOtk();
				case 7:
					return z.getStatus();
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
					return "Номер";
				case 2:
					return "Сотрудник";
				case 3:
					return "Дата";
				case 4:
					return "Клиент";
				case 5:
					return "Причина обр.";
				case 6:
					return "Причина отк.";
				case 7:
					return "Статус";		
				default:
					return null;
			}
		}
		
		public Class getColumnClass(int c) {
			if (c == 3)
				return java.util.Date.class;
			else if (c == 5)
				return java.lang.String.class;
			else if (c == 6)
				return java.lang.String.class;
			else if (c == 7)
				return java.lang.String.class;
			else
				return getValueAt(0, c).getClass();
		}
		
		public void addRow(Zakaz zakaz) {
			int len = zakazs.size();
			zakazs.add(zakaz);
			fireTableRowsInserted(len, len);
		}
		
		public void updateRow(int index) {
			fireTableRowsUpdated(index, index);
		}

		public void deleteRow(int index) {
			if (index != zakazs.size() - 1)
				fireTableRowsUpdated(index + 1, zakazs.size() - 1);
			zakazs.remove(index);
			fireTableRowsDeleted(index, index);
		}
	}
}

