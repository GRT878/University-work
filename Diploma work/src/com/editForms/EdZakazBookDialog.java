package com.editForms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.basic.DBManager;
import com.basic.JDialogResult;
import com.basic.JRDialog;
import com.data.Klient;
import com.data.Sotrud;
import com.data.Zakaz;
import com.helper.HelperConverter;

public class EdZakazBookDialog extends JRDialog{
	private DBManager manager;
	private BigDecimal old_key;
	SimpleDateFormat frmt = new SimpleDateFormat("dd-MM-yyyy");
	private String title_add = "Добавление нового заказа";
	private String title_ed = "Редактирование заказа";
	private Zakaz zakaz = null;
	private boolean isNewRow = false;
	
	private JTextField edNzak;
	private JTextField edKodKl;
	private JComboBox cmbKlient;
	private JTextArea edPrObr;
	private JTextArea edPrOtk;
	private JFormattedTextField edDataObr;
	private JComboBox cmbStatus;
	private JTextField edKodSotr;
	private JComboBox cmbSotrud;
	
	private JButton btnOk;
	private JButton btnCancel;
	
	public EdZakazBookDialog(Window parent, Zakaz zakaz, DBManager manager) {
		this.manager = manager;
		isNewRow = zakaz == null ? true : false;
		setTitle(isNewRow ? title_add : title_ed);
		if (!isNewRow) {
			this.zakaz = zakaz;
			old_key = zakaz.getIdZak();
		} else
			this.zakaz = new Zakaz();
		createGui();
		loadData();
		bindListeners();
		pack();
		setResizable(true);
		setLocationRelativeTo(parent);
	}
	
	private void bindListeners() {
		setKeyListener(this, new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
					setDialogResult(JDialogResult.Cancel);
					close();
					e.consume();
				} else
					super.keyPressed(e);
			}
		});
		
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		btnCancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				setDialogResult(JDialogResult.Cancel);
				close();
			}
		});

		btnOk.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if (!constructZakaz())
					return;
				if (isNewRow) {
					if (manager.addZakaz(zakaz)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
					if (manager.updateZakaz(zakaz, old_key)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
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
		
		cmbKlient.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (e.getItem() != null) {
						Klient klient = (Klient) e.getItem();
						edKodKl.setText(klient.getKodKl().toString());
					}
				}
			}
		});
		
		edKodKl.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				DefaultComboBoxModel model = (DefaultComboBoxModel) cmbKlient.getModel();
				BigDecimal klKod = new BigDecimal(edKodKl.getText());
				setKlientCmbItem(model, klKod);
			}
			@Override
			public void focusGained(FocusEvent e) {}
		});
	}
	
	private void createGui() {
		JPanel pnl=new JPanel(new MigLayout("insets 5","[][]"));
		
		edNzak = new JTextField(25);
		edKodSotr = new JTextField(5);
		cmbSotrud = new JComboBox();
		edDataObr = new JFormattedTextField(HelperConverter.createFormatter("##-##-####"));
		edKodKl = new JTextField(5);
		cmbKlient = new JComboBox();
		edPrObr = new JTextArea(5, 25);
		edPrObr.setLineWrap(true);
		edPrOtk = new JTextArea(5, 25);
		edPrOtk.setLineWrap(true);
		cmbStatus = new JComboBox();
		
		btnOk = new JButton("Сохранить");
		btnCancel = new JButton("Отмена");
		
		pnl.add(new JLabel("Ном. заказа"));
		pnl.add(edNzak, "span, growx");
		pnl.add(new JLabel("Сотрудник"));
		pnl.add(edKodSotr, "split 2");
		pnl.add(cmbSotrud, "wrap, growx, h 20!");
		pnl.add(new JLabel("Дата обр."));
		pnl.add(edDataObr, "span, growx");
		pnl.add(new JLabel("Клиент"));
		pnl.add(edKodKl, "split 2");
		pnl.add(cmbKlient, "wrap, growx, h 20!");
		pnl.add(new JLabel("Причина обр."));
		pnl.add(edPrObr, "span, growx");
		pnl.add(new JLabel("Причина отк."));
		pnl.add(edPrOtk, "span, growx");
		pnl.add(new JLabel("Статус"));
		pnl.add(cmbStatus, "span, growx, h 20!");
		
		pnl.add(btnOk, "growx 0, right, sg 1");
		pnl.add(btnCancel, "sg 1");
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pnl, BorderLayout.NORTH);
	}

	private void setKeyListener(Component c, KeyListener kl) {
		c.addKeyListener(kl);
		if (c instanceof Container)
			for (Component comp:((Container)c).getComponents())
				setKeyListener(comp, kl);
	}

	private void loadData() {
		ArrayList<Sotrud> lstSotr = manager.loadSotrudForCmb();
		if (lstSotr != null) {
			DefaultComboBoxModel model = new DefaultComboBoxModel(lstSotr.toArray());
			cmbSotrud.setModel(model);
			BigDecimal kod = (isNewRow ? null : zakaz.getSotrud().getKodSotr());
			setSotrudCmbItem(model, kod);
		}
		
		ArrayList<Klient> lstKl = manager.loadKlientForCmb();
		if (lstKl != null) {
			DefaultComboBoxModel model = new DefaultComboBoxModel(lstKl.toArray());
			cmbKlient.setModel(model);
			BigDecimal kod = (isNewRow ? null : zakaz.getKlient().getKodKl());
			setKlientCmbItem(model, kod);
		}
		
		ArrayList<String> lstStatus = new ArrayList<String>();
		lstStatus.add("Активен");
		lstStatus.add("Выполнен");
		lstStatus.add("В процессе выполнения");
		lstStatus.add("Отказ в обслуживании");
		DefaultComboBoxModel model = new DefaultComboBoxModel(lstStatus.toArray());
		cmbStatus.setModel(model);
		String status = (isNewRow ? null : zakaz.getStatus());
		setStatusCmbItem(model, status);
		
		if (!isNewRow) {
			edNzak.setText(zakaz.getNomZak().toString());
			edKodSotr.setText(zakaz.getSotrud().getKodSotr() == null ? "" : zakaz.getSotrud().getKodSotr().toString());
			edDataObr.setText(zakaz.getDataObr() == null ? "" : frmt.format(zakaz.getDataObr()));
			edKodKl.setText(zakaz.getKlient().getKodKl() == null ? "" : zakaz.getKlient().getKodKl().toString());
			edPrObr.setText(zakaz.getPrObr());
			edPrOtk.setText(zakaz.getPrOtk());
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
	
	private void setKlientCmbItem(DefaultComboBoxModel model, BigDecimal kod) {
		cmbKlient.setSelectedItem(null);
		if (kod != null)
			for (int i = 0, c = model.getSize(); i < c; i++)
				if (((Klient)model.getElementAt(i)).getKodKl().equals(kod)){
					cmbKlient.setSelectedIndex(i);
					break;
				}
	}
	
	private void setStatusCmbItem(DefaultComboBoxModel model, String status) {
		cmbStatus.setSelectedItem(null);
		if (status != null)
			for (int i = 0, c = model.getSize(); i < c; i++)
				if (model.getElementAt(i).equals(status)){
					cmbStatus.setSelectedIndex(i);
					break;
				}
	}
		
	private boolean constructZakaz() {
		try {
			zakaz.setNomZak(edNzak.getText().equals("") ? null : new BigDecimal(edNzak.getText()));
			Object obj = cmbSotrud.getSelectedItem();
			Sotrud s = (Sotrud)obj;
			zakaz.setSotrud(s);
			zakaz.setDataObr(edDataObr.getText().substring(0, 1).trim().equals("") ? null : frmt.parse(edDataObr.getText()));
			obj = cmbKlient.getSelectedItem();
			Klient k = (Klient)obj;
			zakaz.setKlient(k);
			zakaz.setPrObr(edPrObr.getText());
			zakaz.setPrOtk(edPrOtk.getText());
			if(cmbStatus.getSelectedIndex() != -1)
				zakaz.setStatus(cmbStatus.getSelectedItem().toString());
			else
				zakaz.setStatus(null);
			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
		
	public Zakaz getZakaz() {
		return zakaz;
	}
}
