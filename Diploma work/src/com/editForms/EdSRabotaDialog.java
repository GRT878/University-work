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
import com.data.VidRab;

import net.miginfocom.swing.MigLayout;

public class EdSRabotaDialog extends JRDialog{
	private DBManager manager;
	private BigDecimal old_key;
	private final static String title_add = "Добавление новой работы";
	private final static String title_ed = "Редактирование работы";
	private Rabota rabota = null;
	private boolean isNewRow = false;
	private JTextField edKodRab;
	private JTextField edNaimRab;
	//private JTextField edVidR;
	private JComboBox cmbVidR;
	private JButton btnOk;
	private JButton btnCancel;

	public EdSRabotaDialog(Window parent, Rabota rabota, DBManager manager) {
		this.manager = manager;
		isNewRow = rabota == null ? true : false;
		setTitle(isNewRow ? title_add : title_ed);

		if (!isNewRow) {
			this.rabota = rabota;
			old_key = rabota.getKodRab();
		} else
			this.rabota = new Rabota();
		
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
				if (!constructRabota())
					return;
				if (isNewRow) {
					if (manager.addRabota(rabota)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
				} else 
					if (manager.updateRabota(rabota, old_key)) {
						setDialogResult(JDialogResult.OK);
						close();
					}
			}
		});
		
		/*cmbVidR.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if (e.getItem() != null) {
						VidRab vr = (VidRab) e.getItem();
						edVidR.setText(vr.getVidRab());
					}
				}
			}
		});
		
		edVidR.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				DefaultComboBoxModel model = (DefaultComboBoxModel) cmbVidR.getModel();
				String vidR = new String(edVidR.getText());
				setCmbItem(model, vidR);
			}
			@Override
			public void focusGained(FocusEvent e) {}
		});*/

	}
	
	private void createGui() {
		JPanel pnl = new JPanel(new MigLayout("insets 5", "[][]"));
		edKodRab = new JTextField(25);
		edNaimRab = new JTextField(25);
		//edVidR = new JTextField();
		cmbVidR = new JComboBox();
		
		btnOk = new JButton("Сохранить");
		btnCancel = new JButton("Отмена");
			
		pnl.add(new JLabel("Код"));
		pnl.add(edKodRab,"span");
		pnl.add(new JLabel("Вид работы"));
		//pnl.add(edVidR, "split 2");
		pnl.add(cmbVidR, "growx, wrap, h 20!");
		pnl.add(new JLabel("Работа"));
		pnl.add(edNaimRab,"span");
		
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
		if (!isNewRow){
			edKodRab.setText(rabota.getKodRab().toString());
			edNaimRab.setText(rabota.getNaimRab());
			//edVidR.setText(rabota.getVidR().getVidRab() == null ? "" : rabota.getVidR().getVidRab());
		}
		ArrayList<VidRab> lst = manager.loadVidRabForCmb();
		if (lst != null) {
			DefaultComboBoxModel model = new DefaultComboBoxModel(lst.toArray());
			cmbVidR.setModel(model);
			String vidR = (isNewRow? null : rabota.getVidR().getVidRab());
			setCmbItem(model, vidR);
		}
	}
	
	private void setCmbItem(DefaultComboBoxModel model, String vidR) {
		cmbVidR.setSelectedItem(null);
		if (vidR != null)
			for (int i = 0, c = model.getSize(); i < c; i++)
				if (((VidRab) model.getElementAt(i)).getVidRab().equals(vidR)) {
					cmbVidR.setSelectedIndex(i);
					break;
				}
	}
	
	private boolean constructRabota()	{
		try {
			rabota.setKodRab(edKodRab.getText().equals("") ?  null : new BigDecimal(edKodRab.getText()));
			rabota.setNaimRab(edNaimRab.getText());
			Object obj = cmbVidR.getSelectedItem();
			VidRab vr = (VidRab) obj;
			rabota.setVidR(vr);

			return true;
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка данных", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	public Rabota getRabota() {
		return rabota;
	}
}
