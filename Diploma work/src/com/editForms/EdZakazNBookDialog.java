package com.editForms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Window;
import java.awt.event.*;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.*;

import net.miginfocom.swing.MigLayout;

import com.basic.DBManager;
import com.basic.JDialogResult;
import com.basic.JRDialog;
import com.data.IspMater;
import com.data.Sotrud;
import com.data.VRabota;
import com.data.Zakaz;
import com.data.ZakazN;
import com.gui.GuiHelper;
import com.helper.HelperConverter;

public class EdZakazNBookDialog extends JRDialog{
	private DBManager manager;
	private BigDecimal old_key;
	SimpleDateFormat frmt = new SimpleDateFormat("dd-MM-yyyy");
	DecimalFormat dfrmt = new DecimalFormat("#.00");
	private String title_add = "Добавление нового заказ-наряда";
	private String title_ed = "Редактирование заказ-наряда";
	private ZakazN zakazN = null;
	private boolean isNewRow = false;
	
	private JTextField edNzakN;
	private JTextField edNZak;
	private JComboBox cmbZakaz;
	private JTextField edKodSotr;
	private JComboBox cmbSotrud;
	private JFormattedTextField edDatOform;
	private JFormattedTextField edSrokIsp;
	private JTextField edModel;
	private JTextField edRegNom;
	private JFormattedTextField edStRab;
	private JFormattedTextField edStM;
	
	private JButton btnOk;
	private JButton btnCancel;
	private JTable tblVRabota;
	private JButton btnVRabotaEdit;
	private JButton btnVRabotaNew;
	private JButton btnVRabotaDelete;
	
	private JTable tblIspMater;
	private JButton btnIspMaterEdit;
	private JButton btnIspMaterNew;
	private JButton btnIspMaterDelete;

	private VRabotaTableModel tblVRModel;
	private ArrayList<VRabota> vRabots;
	
	private IspMaterTableModel tblIMModel;
	private ArrayList<IspMater> ispMaters;
	
	public EdZakazNBookDialog(Window parent, ZakazN zakazN, DBManager manager) {
		this.manager = manager;
		isNewRow = zakazN == null ? true : false;
		setTitle(isNewRow ? title_add : title_ed);
		if (!isNewRow) {
			this.zakazN = zakazN;
			old_key = zakazN.getIdZn();
		} else
			this.zakazN = new ZakazN();
		LoadVRabota();
		LoadIspMater();
		createGui();
		loadData();
		bindListeners();
		pack();
		setResizable(true);
		setButton();
		setLocationRelativeTo(parent);
	}
	
	private void setButton() {
		if (btnOk.getText().equals("Сохранить")) {
			btnCancel.setText("Отмена");
			btnVRabotaNew.setEnabled(false);
			btnVRabotaEdit.setEnabled(false);
			btnVRabotaDelete.setEnabled(false);
			btnIspMaterNew.setEnabled(false);
			btnIspMaterEdit.setEnabled(false);
			btnIspMaterDelete.setEnabled(false);
		} else {
			btnCancel.setText("Выход");
			btnVRabotaNew.setEnabled(true);
			btnVRabotaEdit.setEnabled(true);
			btnVRabotaDelete.setEnabled(true);
			btnIspMaterNew.setEnabled(true);
			btnIspMaterEdit.setEnabled(true);
			btnIspMaterDelete.setEnabled(true);
		}
	}

	private void LoadVRabota() {
		vRabots = manager.loadVRabots(zakazN.getIdZn());
	}
	
	private void LoadIspMater() {
		ispMaters = manager.loadIspMaters(zakazN.getIdZn());
	}
	
	private void bindListeners() {
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ((isNewRow) || ((AbstractButton)e.getSource()).getText().equals("Выход")) {
					setDialogResult(JDialogResult.Cancel);
					close();
				}
			}
		});

		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (((AbstractButton)e.getSource()).getText().equals("Сохранить")) {
					if (!constructZakazNBook())  return;
					if (isNewRow) {
						if (manager.addZakazN(zakazN)) { 
							setDialogResult(JDialogResult.OK);
							((AbstractButton)e.getSource()).setText("Редактировать");
							setButton();
						}	
					} else
						if (manager.updateZakazN(zakazN, old_key)) {
							setDialogResult(JDialogResult.OK);
							((AbstractButton)e.getSource()).setText("Редактировать");
							setButton();
						}
				} else {
					((AbstractButton)e.getSource()).setText("Сохранить");
					setButton();
				}
			}
		});

		btnVRabotaNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addVRabota();
			}
		});
		
		btnVRabotaEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editVRabota();
			}
		});
		
		btnVRabotaDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteVRabota();
			}
		});
		
		btnIspMaterNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addIspMater();
			}
		});
		
		btnIspMaterEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editIspMater();
			}
		});
		
		btnIspMaterDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteIspMater();
			}
		});
		
		cmbSotrud.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (e.getItem() != null) {
						Sotrud sotrud = (Sotrud) e.getItem();
						edKodSotr.setText(sotrud.getKodSotr().toString());
					}
				}
			}
		});
		
		edKodSotr.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				DefaultComboBoxModel model = (DefaultComboBoxModel) cmbSotrud.getModel();
				BigDecimal sotrKod = new BigDecimal(edKodSotr.getText());
				setSotrudCmbItem(model, sotrKod);
			}
			@Override
			public void focusGained(FocusEvent e) {}
		});
		
		cmbZakaz.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (e.getItem() != null) {
						Zakaz zakaz = (Zakaz) e.getItem();
						edNZak.setText(zakaz.getNomZak().toString());
					}
				}
			}
		});
		
		edNZak.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				DefaultComboBoxModel model = (DefaultComboBoxModel) cmbZakaz.getModel();
				BigDecimal nZak = new BigDecimal(edNZak.getText());
				setZakazCmbItem(model, nZak);
			}
			@Override
			public void focusGained(FocusEvent e) {}
		});
	}
	
	private void createGui() {
		JPanel pnl=new JPanel(new MigLayout("insets 5","[][]"));
		
		edNzakN = new JTextField(25);
		edNZak = new JTextField(5);
		cmbZakaz = new JComboBox();
		edKodSotr = new JTextField(5);
		cmbSotrud = new JComboBox();
		edDatOform = new JFormattedTextField(HelperConverter.createFormatter("##-##-####"));
		edSrokIsp = new JFormattedTextField(HelperConverter.createFormatter("##-##-####"));
		edModel = new JTextField(25);
		edRegNom = new JTextField(25);
		edStRab = new JFormattedTextField(dfrmt);
		edStM = new JFormattedTextField(dfrmt);
		
		edStRab.setEditable(false);
		edStRab.setBorder(BorderFactory.createEmptyBorder());
		edStRab.setBackground(pnl.getBackground());
		edStRab.setForeground(Color.BLACK);
		
		edStM.setEditable(false);
		edStM.setBorder(BorderFactory.createEmptyBorder());
		edStM.setBackground(pnl.getBackground());
		edStRab.setForeground(Color.BLACK);
		
		btnOk = new JButton("Сохранить");
		btnCancel = new JButton("Отмена");

		pnl.add(new JLabel("№ заказ-наряда"));
		pnl.add(edNzakN, "span, growx");
		pnl.add(new Label("Заказ"));
		pnl.add(edNZak, "split 2");
		pnl.add(cmbZakaz, "wrap, growx, h 20!");
		pnl.add(new Label("Сотрудник"));
		pnl.add(edKodSotr, "split 2");
		pnl.add(cmbSotrud, "wrap, growx, h 20!");
		pnl.add(new JLabel("Дата оформ."));
		pnl.add(edDatOform, "span, growx");
		pnl.add(new JLabel("Срок исп."));
		pnl.add(edSrokIsp, "span, growx");
		pnl.add(new Label("Модель авто"));
		pnl.add(edModel, "span, growx");
		pnl.add(new Label("Рег. номер"));
		pnl.add(edRegNom, "span, growx");
		pnl.add(new Label("Ст. работ"));
		pnl.add(edStRab, "span, growx");
		pnl.add(new Label("Ст. материалов"));
		pnl.add(edStM, "span, growx");
		
		pnl.add(btnOk, "growx 0, right, sg 1");
		pnl.add(btnCancel, "sg 1");

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pnl, BorderLayout.NORTH);
		
		JPanel pnlVRabota = new JPanel(new MigLayout("insets 3,gapy 4", "[grow, fill]","[]5[]10[][grow, fill][]"));		
		tblVRabota = new JTable();
		tblVRabota.setModel(tblVRModel = new VRabotaTableModel(vRabots));
		tblVRabota = new JTable(tblVRModel);
		RowSorter<VRabotaTableModel> sorterVR = new TableRowSorter<VRabotaTableModel>(tblVRModel);
		tblVRabota.setRowSorter(sorterVR);
		tblVRabota.setRowSelectionAllowed(true);
		tblVRabota.setIntercellSpacing(new Dimension(0, 1));
		tblVRabota.setGridColor(new Color(170, 170, 255).darker());
		tblVRabota.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		tblVRabota.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GuiHelper.initColumnSizes(tblVRabota, tblVRModel);
		JScrollPane scrlPaneVR = new JScrollPane(tblVRabota);	
		pnlVRabota.add(getToolBarVRabota(), "growx, wrap");
		pnlVRabota.add(scrlPaneVR, "grow, span, w 600");
		
		JPanel pnlIspMater = new JPanel(new MigLayout("insets 3,gapy 4", "[grow, fill]","[]5[]10[][grow, fill][]"));		
		tblIspMater = new JTable();
		tblIspMater.setModel(tblIMModel = new IspMaterTableModel(ispMaters));
		tblIspMater = new JTable(tblIMModel);
		RowSorter<IspMaterTableModel> sorterIM = new TableRowSorter<IspMaterTableModel>(tblIMModel);
		tblIspMater.setRowSorter(sorterIM);
		tblIspMater.setRowSelectionAllowed(true);
		tblIspMater.setIntercellSpacing(new Dimension(0, 1));
		tblIspMater.setGridColor(new Color(170, 170, 255).darker());
		tblIspMater.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		tblIspMater.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		GuiHelper.initColumnSizes(tblIspMater, tblIMModel);
		JScrollPane scrlPaneIM = new JScrollPane(tblIspMater);	
		pnlIspMater.add(getToolBarIspMater(), "growx, wrap");
		pnlIspMater.add(scrlPaneIM, "grow, span, w 600");
		
		JTabbedPane pane = new JTabbedPane();
		pane.addTab("Работы", pnlVRabota);
		pane.addTab("Материалы", pnlIspMater);
		getContentPane().add(pane, BorderLayout.CENTER);	
	}

	private JToolBar getToolBarVRabota() {
		JToolBar res = new JToolBar();
		res.setFloatable(false);

		URL url = EdZakazNBookDialog.class.getResource("/images/addNew.png");
		btnVRabotaNew = new JButton(new ImageIcon(url));
		btnVRabotaNew.setFocusable(false);
		btnVRabotaNew.setToolTipText("Добавить новую работу");

		url = EdZakazNBookDialog.class.getResource("/images/delRow.png");
		btnVRabotaDelete = new JButton(new ImageIcon(url));
		btnVRabotaDelete.setFocusable(false);
		btnVRabotaDelete.setToolTipText("Удалить работу");
		
		url = EdZakazNBookDialog.class.getResource("/images/editRow.png");
		btnVRabotaEdit = new JButton(new ImageIcon(url));
		btnVRabotaEdit.setFocusable(false);
		btnVRabotaEdit.setToolTipText("Изменить данные о работе");

		res.add(btnVRabotaNew);
		res.add(btnVRabotaEdit);
		res.add(btnVRabotaDelete);

		return res;
	}
	
	private JToolBar getToolBarIspMater() {
		JToolBar res = new JToolBar();
		res.setFloatable(false);

		URL url = EdZakazNBookDialog.class.getResource("/images/addNew.png");
		btnIspMaterNew = new JButton(new ImageIcon(url));
		btnIspMaterNew.setFocusable(false);
		btnIspMaterNew.setToolTipText("Добавить новый материал");

		url = EdZakazNBookDialog.class.getResource("/images/delRow.png");
		btnIspMaterDelete = new JButton(new ImageIcon(url));
		btnIspMaterDelete.setFocusable(false);
		btnIspMaterDelete.setToolTipText("Удалить материал");
		
		url = EdZakazNBookDialog.class.getResource("/images/editRow.png");
		btnIspMaterEdit = new JButton(new ImageIcon(url));
		btnIspMaterEdit.setFocusable(false);
		btnIspMaterEdit.setToolTipText("Изменить данные о материале");

		res.add(btnIspMaterNew);
		res.add(btnIspMaterEdit);
		res.add(btnIspMaterDelete);

		return res;
	}

	private void setKeyListener(Component c, KeyListener kl) {
		c.addKeyListener(kl);
		if (c instanceof Container)
			for (Component comp:((Container)c).getComponents())
				setKeyListener(comp, kl);
	}

	private void loadData() {
		ArrayList<Zakaz> lstZ = manager.loadZakazForCmb();
		if (lstZ != null) {
			DefaultComboBoxModel modelZ = new DefaultComboBoxModel(lstZ.toArray());
			cmbZakaz.setModel(modelZ);
			BigDecimal kodZ = (isNewRow ? null : zakazN.getZakaz().getIdZak());
			setZakazCmbItem(modelZ, kodZ);
		}
		
		ArrayList<Sotrud> lstS = manager.loadSotrudForCmb();
		if (lstS != null) {
			DefaultComboBoxModel modelS = new DefaultComboBoxModel(lstS.toArray());
			cmbSotrud.setModel(modelS);
			BigDecimal kodS = (isNewRow ? null : zakazN.getSotrud().getKodSotr());
			setSotrudCmbItem(modelS, kodS);
		}
		
		if (!isNewRow) {
			edNzakN.setText(zakazN.getNomZn().toString());
			edDatOform.setText(zakazN.getDataOform() == null ? "" : frmt.format(zakazN.getDataOform()));
			edSrokIsp.setText(zakazN.getSrokIsp() == null ? "" : frmt.format(zakazN.getSrokIsp()));
			edModel.setText(zakazN.getModel());
			edRegNom.setText(zakazN.getRegNom());
			edStRab.setText(zakazN.getStRab() == null ? "0.00" : zakazN.getStRab().toString());
			edStM.setText(zakazN.getStM() == null ? "0.00" : zakazN.getStM().toString());
			edNZak.setText(zakazN.getZakaz().getNomZak() == null ? "" : zakazN.getZakaz().getNomZak().toString());
			edKodSotr.setText(zakazN.getSotrud().getKodSotr() == null ? "" : zakazN.getSotrud().getKodSotr().toString());
		} else {
			edStRab.setText("0.00");
			edStM.setText("0.00");
		}
	}

	private void setZakazCmbItem(DefaultComboBoxModel model, BigDecimal kod) {
		cmbZakaz.setSelectedItem(null);
		if (kod != null)
			for (int i = 0, c = model.getSize(); i < c; i++)
				if (((Zakaz)model.getElementAt(i)).getIdZak().equals(kod)){
					cmbZakaz.setSelectedIndex(i);
					break;
				}
	}
	
	private void setSotrudCmbItem(DefaultComboBoxModel model, BigDecimal kod) {
		cmbSotrud.setSelectedItem(null);
		if (kod != null)
			for (int i = 0, c = model.getSize(); i < c; i++)
				if (((Sotrud)model.getElementAt(i)).getKodSotr().equals(kod)){
					cmbSotrud.setSelectedIndex(i);
					break;
				}
	}
		
	private boolean constructZakazNBook() {
		try {
			zakazN.setNomZn(edNzakN.getText().equals("") ? null : new BigDecimal(edNzakN.getText()));
			Object obj = cmbZakaz.getSelectedItem();
			Zakaz z = (Zakaz)obj;
			zakazN.setZakaz(z);
			obj = cmbSotrud.getSelectedItem();
			Sotrud s = (Sotrud)obj;
			zakazN.setSotrud(s);		
			zakazN.setDataOform(edDatOform.getText().substring(0, 1).trim().equals("") ? null : frmt.parse(edDatOform.getText()));
			zakazN.setSrokIsp(edSrokIsp.getText().substring(0, 1).trim().equals("") ? null : frmt.parse(edSrokIsp.getText()));
			zakazN.setModel(edModel.getText());
			zakazN.setRegNom(edRegNom.getText());
			
			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	private class VRabotaTableModel extends AbstractTableModel {
		private ArrayList<VRabota> vRabots;
		
		public VRabotaTableModel(ArrayList<VRabota> vRabots) {
			this.vRabots = vRabots;
		}
		
		@Override
		public int getColumnCount() {
			return 5;
		}
		
		@Override
		public int getRowCount() {
			return (vRabots == null ? 0 : vRabots.size());
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			VRabota vr = vRabots.get(rowIndex);
			switch (columnIndex) {
				case 0:
					return rowIndex + 1;
				case 1:
					return vr.getRabota().getNaimRab();
				case 2:
					return vr.getSotrud().getFio();
				case 3:
					return vr.getVremCh();
				case 4:
					return vr.getStoimCh();
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
					return "Работа";
				case 2:
					return "Сотрудник";
				case 3:
					return "Время";
				case 4:
					return "Стоимость";
				default:
					return null;
			}
		}

		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
		
		public void addRow(VRabota vr) {
			int len = vRabots.size();
			vRabots.add(vr);
			fireTableRowsInserted(len, len);
		}
		
		public void updateRow(int index) {
			fireTableRowsUpdated(index, index);
		}
		
		public void deleteRow(int index) {
			if (index != vRabots.size() - 1)
				fireTableRowsUpdated(index + 1, vRabots.size() - 1);
			vRabots.remove(index);
			fireTableRowsDeleted(index, index);
		}
	}
	
	private class IspMaterTableModel extends AbstractTableModel {
		private ArrayList<IspMater> ispMaters;
		
		public IspMaterTableModel(ArrayList<IspMater> ispMaters) {
			this.ispMaters = ispMaters;
		}
		
		@Override
		public int getColumnCount() {
			return 4;
		}
		
		@Override
		public int getRowCount() {
			return (ispMaters == null ? 0 : ispMaters.size());
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			IspMater im = ispMaters.get(rowIndex);
			switch (columnIndex) {
				case 0:
					return rowIndex + 1;
				case 1:
					return im.getMater().getNaimM();
				case 2:
					return im.getKolvo();
				case 3:
					return im.getCena();
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
					return "Материал";
				case 2:
					return "Кол-во";
				case 3:
					return "Цена";
				default:
					return null;
			}
		}

		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
		
		public void addRow(IspMater im) {
			int len = ispMaters.size();
			ispMaters.add(im);
			fireTableRowsInserted(len, len);
		}
		
		public void updateRow(int index) {
			fireTableRowsUpdated(index, index);
		}
		
		public void deleteRow(int index) {
			if (index != ispMaters.size() - 1)
				fireTableRowsUpdated(index + 1, ispMaters.size() - 1);
			ispMaters.remove(index);
			fireTableRowsDeleted(index, index);
		}
	}
	
	private void addVRabota() {
		EdBookVRabotaDialog dlg = new EdBookVRabotaDialog(this, null, manager, zakazN.getIdZn());		
		if (dlg.showDialog() == JDialogResult.OK) {
			VRabota vr = dlg.getVRabota();
			manager.refreshZakazNBookRow(zakazN);
			tblVRModel.addRow(vr);
			edStRab.setText(zakazN.getStRab().toString());
			System.out.println("Insert vrabota OK");
		}
	}

	private void editVRabota() {
		int index = tblVRabota.getSelectedRow();
		if (index == -1)
			return;
		int modelRow = tblVRabota.convertRowIndexToModel(index);
		VRabota vr = vRabots.get(modelRow);		
		EdBookVRabotaDialog dlg = new EdBookVRabotaDialog(this, vr, manager, zakazN.getIdZn());
		if (dlg.showDialog() == JDialogResult.OK) {
			manager.refreshZakazNBookRow(zakazN);
			tblVRModel.updateRow(modelRow);
			edStRab.setText(zakazN.getStRab().toString());
			System.out.println("Update vrabota OK");
		}
	}
	
	private void deleteVRabota() {
		int index = tblVRabota.getSelectedRow();
		if (index == -1)
			return;
		if (JOptionPane.showConfirmDialog(this, "Удалить строку?", 
				"Подтверждение", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION)
			return;
		int modelRow = tblVRabota.convertRowIndexToModel(index);
		VRabota vr = vRabots.get(modelRow);
		try {
			BigDecimal id = vr.getIdRab();
			if (manager.deleteVRabota(id)) {
				manager.refreshZakazNBookRow(zakazN);
				tblVRModel.deleteRow(modelRow);
				edStRab.setText(zakazN.getStRab().toString());
				System.out.println("Delete vrabota OK");
			} else
				JOptionPane.showMessageDialog(this, "Ошибка удаления строки", "Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка удаления", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void addIspMater() {
		EdBookIspMaterDialog dlg = new EdBookIspMaterDialog(this, null, manager, zakazN.getIdZn());
		if (dlg.showDialog() == JDialogResult.OK) {
			IspMater im = dlg.getIspMater();
			manager.refreshZakazNBookRow(zakazN);
			tblIMModel.addRow(im);
			edStM.setText(zakazN.getStM().toString());
			System.out.println("Insert ispmater OK");
		}
	}

	private void editIspMater() {
		int index = tblIspMater.getSelectedRow();
		if (index == -1)
			return;
		int modelRow = tblIspMater.convertRowIndexToModel(index);
		IspMater im = ispMaters.get(modelRow);		
		EdBookIspMaterDialog dlg = new EdBookIspMaterDialog(this, im, manager, zakazN.getIdZn());
		if (dlg.showDialog() == JDialogResult.OK) {
			manager.refreshZakazNBookRow(zakazN);
			tblIMModel.updateRow(modelRow);
			edStM.setText(zakazN.getStM().toString());
			System.out.println("Update ispmater OK");
		}
	}
	
	private void deleteIspMater() {
		int index = tblIspMater.getSelectedRow();
		if (index == -1)
			return;
		if (JOptionPane.showConfirmDialog(this, "Удалить строку?", 
				"Подтверждение", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION)
			return;
		int modelRow = tblIspMater.convertRowIndexToModel(index);
		IspMater im = ispMaters.get(modelRow);
		try {
			BigDecimal id = im.getIdIspM();
			if (manager.deleteIspMater(id)) {
				manager.refreshZakazNBookRow(zakazN);
				tblIMModel.deleteRow(modelRow);
				edStM.setText(zakazN.getStM().toString());
				System.out.println("Delete ispmater OK");
			} else
				JOptionPane.showMessageDialog(this, "Ошибка удаления строки", "Ошибка", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка удаления", JOptionPane.ERROR_MESSAGE);
		}
	}
		
	public ZakazN getZakazN() {
		return zakazN;
	}
}
