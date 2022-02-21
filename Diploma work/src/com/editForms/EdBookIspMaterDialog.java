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

import net.miginfocom.swing.MigLayout;

import com.basic.DBManager;
import com.basic.JDialogResult;
import com.basic.JRDialog;
import com.data.IspMater;
import com.data.Mater;
import com.data.ZakazN;

public class EdBookIspMaterDialog extends JRDialog{
	private DBManager manager;
	private BigDecimal old_key;
	private final static String title_add = "Добавление нового материала";
	private final static String title_ed = "Редактирование материала";
	private IspMater ispmater = null;
	private boolean isNewRow = false;
	private JTextField edKodM;
	private JComboBox cmbMater;
	private JTextField edKolvo;
	private JTextField edCena;
	private JButton btnOk;
	private JButton btnCancel;
	private BigDecimal fk_key; 
	
	public EdBookIspMaterDialog(Window parent, IspMater ispmater, DBManager manager, BigDecimal fk_key) {
		this.manager = manager;
		this.fk_key = fk_key;
		isNewRow = ispmater == null ? true : false;
		setTitle(isNewRow ? title_add : title_ed);

		if (!isNewRow) {
			this.ispmater = ispmater;
			old_key = ispmater.getIdIspM();
		} else
			this.ispmater = new IspMater();
		
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
				if (!constructIspMater())
					return;
				if (isNewRow) {
					if (manager.addIspMater(ispmater)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
					if (manager.updateIspMater(ispmater, old_key)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
			}
		});
		
		edKodM.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				DefaultComboBoxModel model = (DefaultComboBoxModel) cmbMater.getModel();
				BigDecimal materKod = new BigDecimal(edKodM.getText());
				setMaterCmbItem(model, materKod);
			}
			@Override
			public void focusGained(FocusEvent e) {}
		});
		
		cmbMater.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (e.getItem() != null) {
						Mater mater = (Mater) e.getItem();
						edKodM.setText(mater.getKodM().toString());
					}
				}
			}
		});
	}
	
	private void createGui() {
		JPanel pnl = new JPanel(new MigLayout("insets 5", "[][]"));
		edKodM = new JTextField(5);
		cmbMater = new JComboBox();
		edKolvo = new JTextField(25);
		edCena = new JTextField(25);
		
		btnOk = new JButton("Сохранить");
		btnCancel = new JButton("Отмена");
			
		pnl.add(new JLabel("Материал"));
		pnl.add(edKodM, "split 2");
		pnl.add(cmbMater, "growx, wrap, h 20!");
		pnl.add(new JLabel("Кол-во"));
		pnl.add(edKolvo, "span, growx");
		pnl.add(new JLabel("Стоимость"));
		pnl.add(edCena, "span, growx");
		
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
		ArrayList<Mater> lst = manager.loadMaterForCmb();
		if (lst != null) {
			DefaultComboBoxModel model = new DefaultComboBoxModel(lst.toArray());
			cmbMater.setModel(model);
			BigDecimal rabotaKod = (isNewRow? null : ispmater.getMater().getKodM());
			setMaterCmbItem(model, rabotaKod);
		}
		
		if (!isNewRow){
			edKodM.setText(ispmater.getMater().getKodM() == null ? "" : ispmater.getMater().getKodM().toString());
			edKolvo.setText(ispmater.getKolvo().toString());
			edCena.setText(ispmater.getCena().toString());
		}
	}
	
	private void setMaterCmbItem(DefaultComboBoxModel model, BigDecimal kod) {
		cmbMater.setSelectedItem(null);
		if (kod != null)
			for (int i = 0, c = model.getSize(); i < c; i++)
				if (((Mater)model.getElementAt(i)).getKodM().equals(kod)){
					cmbMater.setSelectedIndex(i);
					break;
				}
	}
	
	private boolean constructIspMater()	{
		try {
			ZakazN zn = new ZakazN();
			zn.setIdZn(fk_key);
			ispmater.setZakazN(zn);
			Object obj = cmbMater.getSelectedItem();
			Mater m = (Mater)obj;
			ispmater.setMater(m);
			ispmater.setKolvo(edKolvo.getText().equals("") ? null : new BigDecimal(edKolvo.getText()));
			ispmater.setCena(edCena.getText().equals("") ? null : new BigDecimal(edCena.getText()));
			
			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	public IspMater getIspMater() {
		return ispmater;
	}
}
