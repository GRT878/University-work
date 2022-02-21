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
import com.data.Sotrud;
import com.editForms.EdSSotrudDialog;
import com.gui.GuiHelper;

import net.miginfocom.swing.MigLayout;

public class FrmSSotrud extends JDialog{
	private DBManager manager;
	private JTable tblSotruds;
	private JButton btnClose;
	private JButton btnEdit;
	private JButton btnNew;
	private JButton btnDelete;
	private SotrudTableModel tblModel;
	private ArrayList<Sotrud> sotruds;
	
	public FrmSSotrud(DBManager manager){
		super();
		this.manager = manager;
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadData();
		createGUI();
		bindListeners();
		pack();
		setTitle("Справочник сотрудников");
		setLocationRelativeTo(this);
	}
	
	private void loadData() {
		sotruds = manager.loadSotruds();
	}
	
	private void createGUI() {
		JPanel pnl = new JPanel(new MigLayout("insets 3, gapy 4", "[grow, fill]", "[]5[grow, fill]10[]"));	
		tblSotruds = new JTable();	
		tblSotruds.setModel(tblModel = new SotrudTableModel(sotruds));
		RowSorter<SotrudTableModel> sorter = new TableRowSorter<SotrudTableModel>(tblModel);
		tblSotruds.setRowSorter(sorter);
		tblSotruds.setRowSelectionAllowed(true);
		tblSotruds.setIntercellSpacing(new Dimension(0, 1));
		tblSotruds.setGridColor(new Color(170, 170, 255).darker());
		tblSotruds.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		tblSotruds.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GuiHelper.initColumnSizes(tblSotruds, tblModel);
		JScrollPane scrlPane = new JScrollPane(tblSotruds);
		scrlPane.getViewport().setBackground(Color.white);
		scrlPane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(3,0,3,0),scrlPane.getBorder()));
		btnClose = new JButton("Закрыть");
		pnl.add(scrlPane, "grow, span");
		pnl.add(btnClose, "growx 0, right");
		pnl.add(getToolBar(),"growx, wrap");
		pnl.add(new JLabel("Справочник сотрудников:"), "growx, span");
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
				addSotrud();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editSotrud();
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteSotrud();
			}
		});
	}
	
	private JToolBar getToolBar() {
		JToolBar res = new JToolBar();
		res.setFloatable(false);
		
		URL url = FrmSSotrud.class.getResource("/images/addNew.png");
		btnNew = new JButton(new ImageIcon(url));
		btnNew.setFocusable(false);
		btnNew.setToolTipText("Добавить нового сотрудника");

		url = FrmSSotrud.class.getResource("/images/delRow.png");
		btnDelete = new JButton(new ImageIcon(url));
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("Удалить сотрудника");
		
		url = FrmSSotrud.class.getResource("/images/editRow.png");
		btnEdit = new JButton(new ImageIcon(url));
		btnEdit.setFocusable(false);
		btnEdit.setToolTipText("Изменить данные о сотруднике");
		
		res.add(btnNew);
		res.add(btnEdit);
		res.add(btnDelete);

		return res;
	}
	
	private void editSotrud() {
		int index = tblSotruds.getSelectedRow();
		if (index == -1)
			return;
		
		int modelRow = tblSotruds.convertRowIndexToModel(index);
		Sotrud sotrud = sotruds.get(modelRow);
		EdSSotrudDialog dlg = new EdSSotrudDialog(this, sotrud, manager);
		if (dlg.showDialog() == JDialogResult.OK) {
			tblModel.updateRow(modelRow);
			System.out.println("Update sotrud OK");
		}
	}
	
	private void addSotrud() {
		EdSSotrudDialog dlg = new EdSSotrudDialog(this, null, manager);
		if (dlg.showDialog() == JDialogResult.OK) {
			Sotrud sotrud = dlg.getSotrud();
			tblModel.addRow(sotrud);
		}
	}
	
	private void deleteSotrud() {
		int index = tblSotruds.getSelectedRow();
		if (index == -1)
			return;
		
		if (JOptionPane.showConfirmDialog(this,
				"Удалить сотрудника?", "Подтверждение", 
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) != 
				JOptionPane.YES_OPTION)
			return;
	
		int modelRow = tblSotruds.convertRowIndexToModel(index);
		Sotrud sotrud = sotruds.get(modelRow);
		try {
			BigDecimal kod = sotrud.getKodSotr();
			if (manager.deleteSotrud(kod)) {
				tblModel.deleteRow(modelRow);
				System.out.println("Delete sotrud OK");
			} else
				JOptionPane.showMessageDialog(this, "Ошибка удаления строки", "Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка удаления", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class SotrudTableModel extends AbstractTableModel {
		private ArrayList<Sotrud> sotruds;
		
		public SotrudTableModel(ArrayList<Sotrud> sotruds) {
			this.sotruds = sotruds;
		}
	  
		@Override
		public int getColumnCount() {
			return 4;
		}
		
		@Override
		public int getRowCount() {
			return (sotruds == null ? 0 : sotruds.size());
		}
		
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Sotrud s = sotruds.get(rowIndex);
			switch (columnIndex) {
				case 0:
					return rowIndex + 1;
				case 1:
					return s.getKodSotr();
				case 2:
					return s.getFio();
				case 3:
					return s.getDolj().getNaimDol();
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
					return "ФИО";
				case 3:
					return "Должность";
				default:
					return null;
			}
		}
		
		public Class getColumnClass(int c) {	
			return getValueAt(0, c).getClass();
		}
		
		public void addRow(Sotrud sotrud) {
			int len = sotruds.size();
			sotruds.add(sotrud);
			fireTableRowsInserted(len, len);
		}
		
		public void updateRow(int index) {
			fireTableRowsUpdated(index, index);
		}

		public void deleteRow(int index) {
			if (index != sotruds.size() - 1)
				fireTableRowsUpdated(index + 1, sotruds.size() - 1);
			sotruds.remove(index);
			fireTableRowsDeleted(index, index);
		}
	}
}
