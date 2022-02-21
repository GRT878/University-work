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
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.basic.DBManager;
import com.basic.JDialogResult;
import com.basic.JRDialog;
import com.data.Rabota;
import com.data.Sotrud;
import com.data.VRabota;
import com.data.ZakazN;

import net.miginfocom.swing.MigLayout;

public class EdBookVRabotaDialog extends JRDialog{
	private DBManager manager;
	private BigDecimal old_key;
	private final static String title_add = "Добавление новой работы";
	private final static String title_ed = "Редактирование работы";
	private VRabota vrabota = null;
	private boolean isNewRow = false;
	private JTextField edKodRab;
	private JComboBox cmbRabota;
	private JTextField edKodSotr;
	private JComboBox cmbSotrud;
	private JTextField edVremCh;
	private JTextField edStoimCh;
	private JButton btnOk;
	private JButton btnCancel;
	private BigDecimal fk_key; 
	
	public EdBookVRabotaDialog(Window parent, VRabota vrabota, DBManager manager, BigDecimal fk_key) {
		this.manager = manager;
		this.fk_key = fk_key;
		isNewRow = vrabota == null ? true : false;
		setTitle(isNewRow ? title_add : title_ed);

		if (!isNewRow) {
			this.vrabota = vrabota;
			old_key = vrabota.getIdRab();
		} else
			this.vrabota = new VRabota();
		
		createGui();
		bindListeners();
		loadData();
		pack();
		setResizable(false);
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
				if (!constructVRabota())
					return;
				if (isNewRow) {
					if (manager.addVRabota(vrabota)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
					if (manager.updateVRabota(vrabota, old_key)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
			}
		});
		
		cmbRabota.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (e.getItem() != null) {
						Rabota rabota = (Rabota) e.getItem();
						edKodRab.setText(rabota.getKodRab().toString());
					}
				}
			}
		});
		
		edKodRab.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				DefaultComboBoxModel model = (DefaultComboBoxModel) cmbRabota.getModel();
				BigDecimal rabotaKod = new BigDecimal(edKodRab.getText());
				setRabotaCmbItem(model, rabotaKod);
			}
			@Override
			public void focusGained(FocusEvent e) {}
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
				BigDecimal sotrudKod = new BigDecimal(edKodSotr.getText());
				setSotrudCmbItem(model, sotrudKod);
			}
			@Override
			public void focusGained(FocusEvent e) {}
		});
	}
	
	private void createGui() {
		JPanel pnl = new JPanel(new MigLayout("insets 5", "[][]"));
		edKodRab = new JTextField(5);
		cmbRabota = new JComboBox();
		edKodSotr = new JTextField(5);
		cmbSotrud = new JComboBox();
		edVremCh = new JTextField(25);
		edStoimCh = new JTextField(25);
		
		btnOk = new JButton("Сохранить");
		btnCancel = new JButton("Отмена");
			
		pnl.add(new JLabel("Работа"));
		pnl.add(edKodRab, "split 2");
		pnl.add(cmbRabota, "growx, wrap, h 20!");
		pnl.add(new JLabel("Сотрудник"));
		pnl.add(edKodSotr, "split 2");
		pnl.add(cmbSotrud, "growx, wrap, h 20!");
		pnl.add(new JLabel("Время"));
		pnl.add(edVremCh, "span, growx");
		pnl.add(new JLabel("Стоимость"));
		pnl.add(edStoimCh, "span, growx");
		
		pnl.add(btnOk, "growx 0, right, sg 1");
		pnl.add(btnCancel, "sg 1");
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pnl, BorderLayout.CENTER);
	}
	
	private void setKeyListener(Component c, KeyListener kl) {
		c.addKeyListener(kl);
		if (c instanceof Container)
			for (Component comp:((Container)c).getComponents())
				setKeyListener(comp, kl);
	}
	
	private void loadData() {
		ArrayList<Rabota> lstR = manager.loadRabotaForCmb();
		if (lstR != null) {
			DefaultComboBoxModel model = new DefaultComboBoxModel(lstR.toArray());
			cmbRabota.setModel(model);
			BigDecimal rabotaKod = (isNewRow? null : vrabota.getRabota().getKodRab());
			setRabotaCmbItem(model, rabotaKod);
		}
		
		ArrayList<Sotrud> lstS = manager.loadSotrudForCmb();
		if (lstS != null) {
			DefaultComboBoxModel model = new DefaultComboBoxModel(lstS.toArray());
			cmbSotrud.setModel(model);
			BigDecimal sotrudKod = (isNewRow? null : vrabota.getSotrud().getKodSotr());
			setSotrudCmbItem(model, sotrudKod);
		}
		
		if (!isNewRow){
			edKodRab.setText(vrabota.getRabota().getKodRab() == null ? "" : vrabota.getRabota().getKodRab().toString());
			edKodSotr.setText(vrabota.getSotrud().getKodSotr() == null ? "" : vrabota.getSotrud().getKodSotr().toString());
			edVremCh.setText(vrabota.getVremCh().toString());
			edStoimCh.setText(vrabota.getStoimCh().toString());
		}
	}
	
	private void setRabotaCmbItem(DefaultComboBoxModel model, BigDecimal kod) {
		cmbRabota.setSelectedItem(null);
		if (kod != null)
			for (int i = 0, c = model.getSize(); i < c; i++)
				if (((Rabota)model.getElementAt(i)).getKodRab().equals(kod)){
					cmbRabota.setSelectedIndex(i);
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
	
	private boolean constructVRabota()	{
		try {
			ZakazN zn = new ZakazN();
			zn.setIdZn(fk_key);
			vrabota.setZakazN(zn);
			Object obj = cmbRabota.getSelectedItem();
			Rabota r = (Rabota)obj;
			vrabota.setRabota(r);
			obj = cmbSotrud.getSelectedItem();
			Sotrud s = (Sotrud)obj;
			vrabota.setSotrud(s);
			vrabota.setStoimCh(edStoimCh.getText().equals("") ? null : new BigDecimal(edStoimCh.getText()));
			vrabota.setVremCh(edVremCh.getText().equals("") ? null : new BigDecimal(edVremCh.getText()));
			
			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	public VRabota getVRabota() {
		return vrabota;
	}
}
