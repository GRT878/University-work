package com.viewingForms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import com.data.VidRab;
import com.editForms.EdSVidRabDialog;
import com.gui.GuiHelper;

import net.miginfocom.swing.MigLayout;

public class FrmSVidRab extends JDialog{
	private DBManager manager;
	private JTable tblVidRabs;
	private JButton btnClose;
	private JButton btnEdit;
	private JButton btnNew;
	private JButton btnDelete;
	private MaterTableModel tblModel;
	private ArrayList<VidRab> vidRabs;
	
	public FrmSVidRab(DBManager manager){
		super();
		this.manager = manager;
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadData();
		createGUI();
		bindListeners();
		pack();
		setTitle("Справочник видов работ");
		setLocationRelativeTo(this);
	}
	
	private void loadData() {
		vidRabs = manager.loadVidRabs();
	}
	
	private void createGUI() {
		JPanel pnl = new JPanel(new MigLayout("insets 3, gapy 4", "[grow, fill]", "[]5[grow, fill]10[]"));	
		tblVidRabs = new JTable();	
		tblVidRabs.setModel(tblModel = new MaterTableModel(vidRabs));
		RowSorter<MaterTableModel> sorter = new TableRowSorter<MaterTableModel>(tblModel);
		tblVidRabs.setRowSorter(sorter);
		tblVidRabs.setRowSelectionAllowed(true);
		tblVidRabs.setIntercellSpacing(new Dimension(0, 1));
		tblVidRabs.setGridColor(new Color(170, 170, 255).darker());
		tblVidRabs.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		tblVidRabs.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GuiHelper.initColumnSizes(tblVidRabs, tblModel);
		JScrollPane scrlPane = new JScrollPane(tblVidRabs);
		scrlPane.getViewport().setBackground(Color.white);
		scrlPane.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(3,0,3,0),scrlPane.getBorder()));
		btnClose = new JButton("Закрыть");
		pnl.add(scrlPane, "grow, span");
		pnl.add(btnClose, "growx 0, right");
		pnl.add(getToolBar(),"growx,wrap");
		pnl.add(new JLabel("Справочник видов работ:"), "growx,span");
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
				addVidRab();
			}
		});
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editVidRab();
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteVidRab();
			}
		});
	}
	
	private JToolBar getToolBar() {
		JToolBar res = new JToolBar();
		res.setFloatable(false);

		URL url = FrmSVidRab.class.getResource("/images/addNew.png");
		btnNew = new JButton(new ImageIcon(url));
		btnNew.setFocusable(false);
		btnNew.setToolTipText("Добавить новый вид работ");

		url = FrmSVidRab.class.getResource("/images/delRow.png");
		btnDelete = new JButton(new ImageIcon(url));
		btnDelete.setFocusable(false);
		btnDelete.setToolTipText("Удалить вид работ");
		
		url = FrmSVidRab.class.getResource("/images/editRow.png");
		btnEdit = new JButton(new ImageIcon(url));
		btnEdit.setFocusable(false);
		btnEdit.setToolTipText("Изменить данные о виде работ");

		res.add(btnNew);
		res.add(btnEdit);
		res.add(btnDelete);

		return res;
	}
	
	private void editVidRab() {
		int index = tblVidRabs.getSelectedRow();
		if (index == -1)
			return;
		
		int modelRow = tblVidRabs.convertRowIndexToModel(index);
		VidRab vidRab = vidRabs.get(modelRow);
		EdSVidRabDialog dlg = new EdSVidRabDialog(this, vidRab, manager);		
		if (dlg.showDialog() == JDialogResult.OK) {
			tblModel.updateRow(modelRow);
			System.out.println("Update vid_rab OK");
		}
	}
	
	private void addVidRab() {
		EdSVidRabDialog dlg = new EdSVidRabDialog(this, null, manager);
		if (dlg.showDialog() == JDialogResult.OK) {
			VidRab vidRab = dlg.getVidRab();
			tblModel.addRow(vidRab);
		}
	}
	
	private void deleteVidRab() {
		int index = tblVidRabs.getSelectedRow();
		if (index == -1)
			return;
		
		if (JOptionPane.showConfirmDialog(this,
				"Удалить вид работы?", "Подтверждение", 
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) != 
				JOptionPane.YES_OPTION)
			return;
	
		int modelRow = tblVidRabs.convertRowIndexToModel(index);
		VidRab vidRab = vidRabs.get(modelRow);
		try {
			String kod = vidRab.getVidRab();
			if (manager.deleteVidRab(kod)) {
				tblModel.deleteRow(modelRow);
				System.out.println("Delete vid_rab OK");
			} else
				JOptionPane.showMessageDialog(this, "Ошибка удаления строки", "Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка удаления", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private class MaterTableModel extends AbstractTableModel {
		private ArrayList<VidRab> vidRabs;
		
		public MaterTableModel(ArrayList<VidRab> vidRabs) {
			this.vidRabs = vidRabs;
		}
	  
		@Override
		public int getColumnCount() {
			return 2;
		}
		
		@Override
		public int getRowCount() {
			return (vidRabs == null ? 0 : vidRabs.size());
		}
		
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			VidRab vr = vidRabs.get(rowIndex);
			switch (columnIndex) {
				case 0:
					return rowIndex + 1;
				case 1:
					return vr.getVidRab();
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
					return "Вид работы";
				default:
					return null;
			}
		}
		
		public Class getColumnClass(int c) {	
			return getValueAt(0, c).getClass();
		}
		
		public void addRow(VidRab vidRab) {
			int len = vidRabs.size();
			vidRabs.add(vidRab);
			fireTableRowsInserted(len, len);
		}
		
		public void updateRow(int index) {
			fireTableRowsUpdated(index, index);
		}

		public void deleteRow(int index) {
			if (index != vidRabs.size() - 1)
				fireTableRowsUpdated(index + 1, vidRabs.size() - 1);
			vidRabs.remove(index);
			fireTableRowsDeleted(index, index);
		}
	}
}
