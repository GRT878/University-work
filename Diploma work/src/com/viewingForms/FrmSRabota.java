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
import com.data.Rabota;
import com.editForms.EdSRabotaDialog;
import com.gui.GuiHelper;

import net.miginfocom.swing.MigLayout;

public class FrmSRabota extends JDialog{
	private DBManager manager;
	private JTable tblRabots;
	private JButton btnClose;
	private JButton btnEdit;
	private JButton btnNew;
	private JButton btnDelete;
	private RabotaTableModel tblModel;
	private ArrayList<Rabota> rabots;
	
	public FrmSRabota(DBManager manager){
		super();
		this.manager = manager;
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadData();
		createGUI();
		bindListeners();
		pack();
		setTitle("Справочник работ");
		setLocationRelativeTo(this);
	}
	
	private void loadData() {
		rabots = manager.loadRabots();
	}
	
	private void createGUI() {
		JPanel pnl = new JPanel(new MigLayout("insets 3, gapy 4", "[grow, fill]", "[]5[grow, fill]10[]"));	
		tblRabots = new JTable();	
		tblRabots.setModel(tblModel = new RabotaTableModel(rabots));
		RowSorter<RabotaTableModel> sorter = new TableRowSorter<RabotaTableModel>(tblModel);
		tblRabots.getColumnModel().getColumn(0).setPreferredWidth(1);
		tblRabots.getColumnModel().getColumn(1).setPreferredWidth(1);
		tblRabots.getColumnModel().getColumn(2).setPreferredWidth(1);
		tblRabots.setRowSorter(sorter);
		tblRabots.setRowSelectionAllowed(true);
		tblRabots.setIntercellSpacing(new Dimension(0, 1));
		tblRabots.setGridColor(new Color(170, 170, 255).darker());
		tblRabots.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		tblRabots.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GuiHelper.initColumnSizes(tblRabots, tblModel);
		JScrollPane scrlPane = new JScrollPane(tblRabots);
		scrlPane.getViewport().setBackground(Color.white);
		scrlPane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(3,0,3,0),scrlPane.getBorder()));
		btnClose = new JButton("Закрыть");
		pnl.add(scrlPane, "grow, span");
		pnl.add(btnClose, "growx 0, right");
		pnl.add(getToolBar(),"growx, wrap");
		pnl.add(new JLabel("Справочник работ:"), "growx, span");
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
				addRabota();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editRabota();
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteRabota();
			}
		});
	}
	
	private JToolBar getToolBar() {
		JToolBar res = new JToolBar();
		res.setFloatable(false);
		
		URL url = FrmSRabota.class.getResource("/images/addNew.png");
		btnNew = new JButton(new ImageIcon(url));
		btnNew.setFocusable(false);
		btnNew.setToolTipText("Добавить новую работу");

		url = FrmSRabota.class.getResource("/images/delRow.png");
		btnDelete = new JButton(new ImageIcon(url));
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("Удалить работу");
		
		url = FrmSRabota.class.getResource("/images/editRow.png");
		btnEdit = new JButton(new ImageIcon(url));
		btnEdit.setFocusable(false);
		btnEdit.setToolTipText("Изменить данные о работе");
		
		res.add(btnNew);
		res.add(btnEdit);
		res.add(btnDelete);

		return res;
	}
	
	private void editRabota() {
		int index = tblRabots.getSelectedRow();
		if (index == -1)
			return;
		
		int modelRow = tblRabots.convertRowIndexToModel(index);
		Rabota rabota = rabots.get(modelRow);		
		EdSRabotaDialog dlg = new EdSRabotaDialog(this, rabota, manager);
		if (dlg.showDialog() == JDialogResult.OK) {
			tblModel.updateRow(modelRow);
			System.out.println("Update rabota OK");
		}
	}
	
	private void addRabota() {
		EdSRabotaDialog dlg = new EdSRabotaDialog(this, null, manager);
		if (dlg.showDialog() == JDialogResult.OK) {
			Rabota rabota = dlg.getRabota();
			tblModel.addRow(rabota);
		}
	}
	
	private void deleteRabota() {
		int index = tblRabots.getSelectedRow();
		if (index == -1)
			return;
		
		if (JOptionPane.showConfirmDialog(this,
				"Удалить работу?", "Подтверждение", 
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) != 
				JOptionPane.YES_OPTION)
			return;
	
		int modelRow = tblRabots.convertRowIndexToModel(index);
		Rabota rabota = rabots.get(modelRow);
		try {
			BigDecimal kod = rabota.getKodRab();
			if (manager.deleteRabota(kod)) {
				tblModel.deleteRow(modelRow);
				System.out.println("Delete rabota OK");
			} else
				JOptionPane.showMessageDialog(this, "Ошибка удаления строки", "Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка удаления", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class RabotaTableModel extends AbstractTableModel {
		private ArrayList<Rabota> rabots;
		
		public RabotaTableModel(ArrayList<Rabota> rabots) {
			this.rabots = rabots;
		}
	  
		@Override
		public int getColumnCount() {
			return 4;
		}
		
		@Override
		public int getRowCount() {
			return (rabots == null ? 0 : rabots.size());
		}
		
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Rabota r = rabots.get(rowIndex);
			switch (columnIndex) {
				case 0:
					return rowIndex + 1;
				case 1:
					return r.getKodRab();
				case 2:
					return r.getVidR().getVidRab();
				case 3:
					return r.getNaimRab();
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
					return "Вид работы";
				case 3:
					return "Работа";
				default:
					return null;
			}
		}
		
		public Class getColumnClass(int c) {	
			return getValueAt(0, c).getClass();
		}
		
		public void addRow(Rabota rabota) {
			int len = rabots.size();
			rabots.add(rabota);
			fireTableRowsInserted(len, len);
		}
		
		public void updateRow(int index) {
			fireTableRowsUpdated(index, index);
		}

		public void deleteRow(int index) {
			if (index != rabots.size() - 1)
				fireTableRowsUpdated(index + 1, rabots.size() - 1);
			rabots.remove(index);
			fireTableRowsDeleted(index, index);
		}
	}
}
