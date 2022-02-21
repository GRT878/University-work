package com.basic;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

import javax.swing.*;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import com.data.Dolj;
import com.data.IspMater;
import com.data.Klient;
import com.data.Mater;
import com.data.Rabota;
import com.data.Sotrud;
import com.data.VRabota;
import com.data.VidRab;
import com.data.Zakaz;
import com.data.ZakazN;
import com.helper.ErrorHandler;
import com.helper.HelperConverter;
import com.rpt.RptData;
import com.rpt.RptParams;

public class DBManager {
	private Connection con = null; 
    public DBManager() throws SQLException {}
    
    public Connection getConnection(){
       return con;
    }
    
    public void setConnection(Connection con) {
    	this.con=con;
    }
    
	public String getVersion() {
		String ver=null;
		Statement stmt;
		ResultSet rset;	
		try {
			stmt = con.createStatement();
			rset = stmt.executeQuery("SELECT @@VERSION");		
			while (rset.next()) {
				ver = rset.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ver;
	}

	public void setPath() {
		Statement stmt;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("USE carService");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	private void RollBack() {
		try {
			con.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		setPath();
	}
	
	//Материалы
	public ArrayList<Mater> loadMaters() {
		ArrayList<Mater> maters = null;
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT kod_m, naim_m, cena_ed FROM mater");
			maters = new ArrayList<Mater>();
			
			while (res.next()) {
				Mater m = new Mater();
				m.setKodM(res.getBigDecimal(1));
				m.setNaimM(res.getString(2));
				m.setCenaEd(res.getBigDecimal(3));
				maters.add(m);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		return maters;
	}

	public boolean updateMater(Mater mater, BigDecimal key) {
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "UPDATE mater set kod_m=?, "
				+ "naim_m=?, cena_ed=? WHERE kod_m=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, mater.getKodM());
			pst.setString(2, mater.getNaimM());
			pst.setBigDecimal(3, mater.getCenaEd());
			pst.setBigDecimal(4, key);
			pst.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
		return true;
	}
	
	public boolean addMater(Mater mater) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="INSERT INTO mater(kod_m, "+
				"naim_m, cena_ed) VALUES(?,?,?)";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, mater.getKodM());
			pst.setString(2, mater.getNaimM());
			pst.setBigDecimal(3, mater.getCenaEd());
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}
	
	public boolean deleteMater(BigDecimal kod) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="DELETE FROM mater WHERE kod_m=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, kod);
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}

	//Должности
	public ArrayList<Dolj> loadDoljs() {
		ArrayList<Dolj> doljs = null;
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT kod_dol, naim_dol FROM dolj");
			doljs = new ArrayList<Dolj>();
			
			while (res.next()) {
				Dolj d = new Dolj();
				d.setKodDol(res.getBigDecimal(1));
				d.setNaimDol(res.getString(2));
				doljs.add(d);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		return doljs;
	}

	public boolean updateDolj(Dolj dolj, BigDecimal key) {
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "UPDATE dolj set kod_dol=?, "
				+ "naim_dol=? WHERE kod_dol=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, dolj.getKodDol());
			pst.setString(2, dolj.getNaimDol());
			pst.setBigDecimal(3, key);
			pst.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
		return true;
	}
	
	public boolean addDolj(Dolj dolj) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="INSERT INTO dolj(kod_dol, "+
				"naim_dol) VALUES(?,?)";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, dolj.getKodDol());
			pst.setString(2, dolj.getNaimDol());
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}
	
	public boolean deleteDolj(BigDecimal kod) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="DELETE FROM dolj WHERE kod_dol=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, kod);
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}

	//Клиенты
	public ArrayList<Klient> loadKlients() {
		ArrayList<Klient> klients = null;
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT kod_kl, naim_kl, "
					+ "doc, tel FROM klient");
			klients = new ArrayList<Klient>();
			
			while (res.next()) {
				Klient k = new Klient();
				k.setKodKl(res.getBigDecimal(1));
				k.setNaimKl(res.getString(2));
				k.setDoc(res.getString(3));
				k.setTel(res.getString(4));
				klients.add(k);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		return klients;
	}

	public boolean updateKlient(Klient klient, BigDecimal key) {
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "UPDATE klient set kod_kl=?, naim_kl=?, "
				+ "doc=?, tel=? WHERE kod_kl=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, klient.getKodKl());
			pst.setString(2, klient.getNaimKl());
			pst.setString(3, klient.getDoc());
			pst.setString(4, klient.getTel());
			pst.setBigDecimal(5, key);
			pst.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
		return true;
	}
	
	public boolean addKlient(Klient klient) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="INSERT INTO klient(kod_kl, naim_kl, "
				+ "doc, tel) VALUES(?,?,?,?)";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, klient.getKodKl());
			pst.setString(2, klient.getNaimKl());
			pst.setString(3, klient.getDoc());
			pst.setString(4, klient.getTel());
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}
	
	public boolean deleteKlient(BigDecimal kod) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="DELETE FROM klient WHERE kod_kl=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, kod);
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}

	//Виды работ
	public ArrayList<VidRab> loadVidRabs() {
		ArrayList<VidRab> vidRabs = null;
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT vid_rab FROM vid_rab");
			vidRabs = new ArrayList<VidRab>();
			
			while (res.next()) {
				VidRab vr = new VidRab();
				vr.setVidRab(res.getString(1));
				vidRabs.add(vr);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		return vidRabs;
	}

	public boolean updateVidRab(VidRab vidRab, String key) {
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "UPDATE vid_rab set vid_rab=? WHERE vid_rab=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setString(1, vidRab.getVidRab());
			pst.setString(2, key);
			pst.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
		return true;
	}
	
	public boolean addVidRab(VidRab vidRab) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="INSERT INTO vid_rab(vid_rab) VALUES(?)";
		try {  
			pst = con.prepareStatement(stm);
			pst.setString(1, vidRab.getVidRab());
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}
	
	public boolean deleteVidRab(String kod) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="DELETE FROM vid_rab WHERE vid_rab=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setString(1, kod);
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}

	//Сотрудники
	public ArrayList<Sotrud> loadSotruds() {
		ArrayList<Sotrud> sotruds = null;
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT kod_sotr, fio, "
					+ "kod_dol, naim_dol FROM sotrud_v");
			sotruds = new ArrayList<Sotrud>();
			
			while (res.next()) {
				Sotrud s = new Sotrud();
				s.setKodSotr(res.getBigDecimal(1));
				s.setFio(res.getString(2));
				
				Dolj d = new Dolj();
				d.setKodDol(res.getBigDecimal(3));
				d.setNaimDol(res.getString(4));
				s.setDolj(d);
				
				sotruds.add(s);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		return sotruds;
	}
	
	public ArrayList<Dolj> loadDoljForCmb() {
		ArrayList<Dolj> doljs = new ArrayList<Dolj>();
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT kod_dol, naim_dol FROM dolj order by kod_dol");
			doljs = new ArrayList<Dolj>();
			while (res.next()) {
				Dolj s = new Dolj();
				s.setKodDol(res.getBigDecimal(1));
				s.setNaimDol(res.getString(2));
				doljs.add(s);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
	          "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
			RollBack();
		}
		return doljs;
	}

	public boolean updateSotrud(Sotrud sotrud, BigDecimal key) {
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "UPDATE sotrud set kod_sotr=?, fio=?, "
				+ "kod_dol=? WHERE kod_sotr=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, sotrud.getKodSotr());
			pst.setString(2, sotrud.getFio());
			if (sotrud.getDolj() == null)
				pst.setBigDecimal(3, null);
			else
				pst.setBigDecimal(3, sotrud.getDolj().getKodDol());
			pst.setBigDecimal(4, key);
			pst.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), 
					"Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
		return true;
	}
	
	public boolean addSotrud(Sotrud sotrud) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="INSERT INTO sotrud(kod_sotr, fio, kod_dol) VALUES(?,?,?)";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, sotrud.getKodSotr());
			pst.setString(2, sotrud.getFio());
			if (sotrud.getDolj() == null)
				pst.setBigDecimal(3, null);
			else
				pst.setBigDecimal(3, sotrud.getDolj().getKodDol());
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), 
					"Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}
	
	public boolean deleteSotrud(BigDecimal kod) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="DELETE FROM sotrud WHERE kod_sotr=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, kod);
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}

	//Работы
	public ArrayList<Rabota> loadRabots() {
		ArrayList<Rabota> rabots = null;
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT kod_rab, vid_rab, naim_rab FROM rabota_v");
			rabots = new ArrayList<Rabota>();
			
			while (res.next()) {
				Rabota r = new Rabota();
				r.setKodRab(res.getBigDecimal(1));
				
				VidRab vr = new VidRab();
				vr.setVidRab(res.getString(2));
				r.setVidR(vr);
				
				r.setNaimRab(res.getString(3));
				
				rabots.add(r);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		return rabots;
	}
	
	public ArrayList<VidRab> loadVidRabForCmb() {
		ArrayList<VidRab> vidRabs = new ArrayList<VidRab>();
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT vid_rab FROM vid_rab order by vid_rab");
			vidRabs = new ArrayList<VidRab>();
			while (res.next()) {
				VidRab vr = new VidRab();
				vr.setVidRab(res.getString(1));
				vidRabs.add(vr);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
	          "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
			RollBack();
		}
		return vidRabs;
	}

	public boolean updateRabota(Rabota rabota, BigDecimal key) {
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "UPDATE rabota set kod_rab=?, vid_rab=?, naim_rab=? WHERE kod_rab=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, rabota.getKodRab());
			if (rabota.getVidR() == null)
				pst.setString(2, null);
			else
				pst.setString(2, rabota.getVidR().getVidRab());
			pst.setString(3, rabota.getNaimRab());
			pst.setBigDecimal(4, key);
			pst.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
		return true;
	}
	
	public boolean addRabota(Rabota rabota) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="INSERT INTO rabota(kod_rab, vid_rab, naim_rab) VALUES(?,?,?)";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, rabota.getKodRab());
			if (rabota.getVidR() == null)
				pst.setString(2, null);
			else
				pst.setString(2, rabota.getVidR().getVidRab());
			pst.setString(3, rabota.getNaimRab());
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}
	
	public boolean deleteRabota(BigDecimal kod) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="DELETE FROM rabota WHERE kod_rab=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, kod);
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}
	
	//Заказ-наряды
	public void refreshZakazNBookRow(ZakazN book) {
		if (book.getIdZn()==null)
			return;
		Connection conn = this.getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery("SELECT st_rab, st_m FROM zakazn_v"+
					" where id_zn=" + book.getIdZn().toString());
			while (res.next()) {
				book.setStRab(res.getBigDecimal(1));
				book.setStM(res.getBigDecimal(2));
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
	}

	public ArrayList<ZakazN> loadZakazNs() {
		ArrayList<ZakazN> zakazNs = null;
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT id_zn, nom_zn, "
					+ "id_zak, nom_zak, data_oform, srok_isp, model, reg_nom, "
					+ "kod_sotr, fio, st_rab, st_m FROM zakazn_v");
			zakazNs = new ArrayList<ZakazN>();
			while (res.next()) {
				ZakazN zn = new ZakazN();
				zn.setIdZn(res.getBigDecimal(1));
				zn.setNomZn(res.getBigDecimal(2));
				
				Zakaz z = new Zakaz();
				z.setIdZak(res.getBigDecimal(3));
				z.setNomZak(res.getBigDecimal(4));
				zn.setZakaz(z);
				
				zn.setDataOform(res.getDate(5));
				zn.setSrokIsp(res.getDate(6));
				zn.setModel(res.getString(7));
				zn.setRegNom(res.getString(8));
				
				Sotrud s = new Sotrud();
				s.setKodSotr(res.getBigDecimal(9));
				s.setFio(res.getString(10));
				zn.setSotrud(s);
				
				zn.setStRab(res.getBigDecimal(11));
				zn.setStM(res.getBigDecimal(12));
				
				zakazNs.add(zn);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		return zakazNs;
	}
	
	public ArrayList<Zakaz> loadZakazForCmb() {
		ArrayList<Zakaz> zakazs = new ArrayList<Zakaz>();
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT id_zak, nom_zak, naim_kl FROM zakaz_v order by nom_zak");
			zakazs = new ArrayList<Zakaz>();
			while (res.next()) {
				Zakaz z = new Zakaz();
				z.setIdZak(res.getBigDecimal(1));
				z.setNomZak(res.getBigDecimal(2));
				Klient k = new Klient();
				k.setNaimKl(res.getString(3));
				z.setKlient(k);
				zakazs.add(z);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
	          "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
			RollBack();
		}
		return zakazs;
	}
	
	public ArrayList<Sotrud> loadSotrudForCmb() {
		ArrayList<Sotrud> sotruds = new ArrayList<Sotrud>();
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT kod_sotr, fio FROM sotrud_v order by kod_sotr");
			sotruds = new ArrayList<Sotrud>();
			while (res.next()) {
				Sotrud s = new Sotrud();
				s.setKodSotr(res.getBigDecimal(1));
				s.setFio(res.getString(2));
				sotruds.add(s);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
	          "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
			RollBack();
		}
		return sotruds;
	}

	public boolean updateZakazN(ZakazN zakazN, BigDecimal key) {
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "UPDATE zakaz_n set nom_zn=?, id_zak=?, data_oform=?,"
				+ " srok_isp=?, model=?, reg_nom=?, kod_sotr=? "
				+ "WHERE id_zn=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, zakazN.getNomZn());
			if (zakazN.getZakaz() == null)
				pst.setBigDecimal(2, null);
			else
				pst.setBigDecimal(2, zakazN.getZakaz().getIdZak());
			pst.setDate(3, HelperConverter.convertFromJavaDateToSQLDate(zakazN.getDataOform()));
			pst.setDate(4, HelperConverter.convertFromJavaDateToSQLDate(zakazN.getSrokIsp()));
			pst.setString(5, zakazN.getModel());
			pst.setString(6, zakazN.getRegNom());
			if(zakazN.getSotrud() == null)
				pst.setBigDecimal(7, null);
			else
				pst.setBigDecimal(7, zakazN.getSotrud().getKodSotr());		
			pst.setBigDecimal(8, key);
			pst.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
		return true;
	}
	
	public boolean addZakazN(ZakazN zakazN) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="INSERT INTO zakaz_n(id_zn, nom_zn, id_zak, data_oform, srok_isp,"
				+ " model, reg_nom, kod_sotr) VALUES(?,?,?,?,?,?,?,?)";
		try {  
			pst = con.prepareStatement(stm);
			zakazN.setIdZn(getId("zn_seq"));
			pst.setBigDecimal(1, zakazN.getIdZn());
			pst.setBigDecimal(2, zakazN.getNomZn());
			if (zakazN.getZakaz() == null)
				pst.setBigDecimal(3, null);
			else
				pst.setBigDecimal(3, zakazN.getZakaz().getIdZak());
			pst.setDate(4, HelperConverter.convertFromJavaDateToSQLDate(zakazN.getDataOform()));
			pst.setDate(5, HelperConverter.convertFromJavaDateToSQLDate(zakazN.getSrokIsp()));
			pst.setString(6, zakazN.getModel());
			pst.setString(7, zakazN.getRegNom());
			if(zakazN.getSotrud() == null)
				pst.setBigDecimal(8, null);
			else
				pst.setBigDecimal(8, zakazN.getSotrud().getKodSotr());
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}
	
	public boolean deleteZakazN(BigDecimal kod) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="DELETE FROM zakaz_n WHERE id_zn=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, kod);
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}

	public BigDecimal getId(String seqName) {
		BigDecimal id = null;
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "SELECT admin.nextval(?)";
		try {
			pst = con.prepareStatement(stm);
			pst.setString(1, seqName);
			ResultSet res = pst.executeQuery();
			while (res.next()) {
				id = res.getBigDecimal(1);
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage(), 
					"Ошибка получения идентификатора", 
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
		return id;
	}
	
	//Выполненные работы
	public ArrayList<VRabota> loadVRabots(BigDecimal kod) {
		ArrayList<VRabota> vRabots = null;
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res;
			if (kod != null) {
				res = stmt.executeQuery("SELECT id_rab, kod_rab, naim_rab, kod_sotr, fio,"
						+ " vrem_ch, stoim_1ch FROM vrabota_v where id_zn=" + kod.toString());
			} else {
				res = stmt.executeQuery("SELECT id_rab, kod_rab, naim_rab, kod_sotr, fio,"
						+ " vrem_ch, stoim_1ch, id_zn FROM vrabota_v where id_zn=null");
			}
			vRabots = new ArrayList<VRabota>();
			
			while (res.next()) {
				VRabota vr = new VRabota();
				vr.setIdRab(res.getBigDecimal(1));
				Rabota r = new Rabota();
				r.setKodRab(res.getBigDecimal(2));
				r.setNaimRab(res.getString(3));
				vr.setRabota(r);
				Sotrud s = new Sotrud();
				s.setKodSotr(res.getBigDecimal(4));
				s.setFio(res.getString(5));
				vr.setSotrud(s);
				vr.setVremCh(res.getBigDecimal(6));
				vr.setStoimCh(res.getBigDecimal(7));
				vRabots.add(vr);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		return vRabots;
	}
	
	public ArrayList<Rabota> loadRabotaForCmb() {
		ArrayList<Rabota> rabots = new ArrayList<Rabota>();
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT kod_rab, vid_rab, naim_rab FROM rabota_v order by kod_rab");
			rabots = new ArrayList<Rabota>();
			while (res.next()) {
				Rabota r = new Rabota();
				r.setKodRab(res.getBigDecimal(1));
				VidRab vr = new VidRab();
				vr.setVidRab(res.getString(2));
				r.setVidR(vr);
				r.setNaimRab(res.getString(3));
				rabots.add(r);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
	          "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
			RollBack();
		}
		return rabots;
	}

	public boolean updateVRabota(VRabota vRabota, BigDecimal key) {
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "UPDATE v_rabota set kod_rab=?, id_zn=?, kod_sotr=?, vrem_ch=?, stoim_1ch=? WHERE id_rab=?";
		try {  
			pst = con.prepareStatement(stm);
			if (vRabota.getRabota() == null)
				pst.setBigDecimal(1, null);
			else
				pst.setBigDecimal(1, vRabota.getRabota().getKodRab());
			if (vRabota.getZakazN() == null)
				pst.setBigDecimal(2, null);
			else
				pst.setBigDecimal(2, vRabota.getZakazN().getIdZn());
			if (vRabota.getSotrud() == null)
				pst.setBigDecimal(3, null);
			else
				pst.setBigDecimal(3, vRabota.getSotrud().getKodSotr());
			pst.setBigDecimal(4, vRabota.getVremCh());
			pst.setBigDecimal(5, vRabota.getStoimCh());
			pst.setBigDecimal(6, key);
			pst.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
		return true;
	}
	
	public boolean addVRabota(VRabota vRabota) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="INSERT INTO v_rabota(id_rab, kod_rab, id_zn, kod_sotr, vrem_ch, stoim_1ch) VALUES(?,?,?,?,?,?)";
		try {  
			pst = con.prepareStatement(stm);
			vRabota.setIdRab(getId("vr_seq"));
			pst.setBigDecimal(1, vRabota.getIdRab());
			if (vRabota.getRabota() == null)
				pst.setBigDecimal(2, null);
			else
				pst.setBigDecimal(2, vRabota.getRabota().getKodRab());
			if (vRabota.getZakazN() == null)
				pst.setBigDecimal(3, null);
			else
				pst.setBigDecimal(3, vRabota.getZakazN().getIdZn());
			if (vRabota.getSotrud() == null)
				pst.setBigDecimal(4, null);
			else
				pst.setBigDecimal(4, vRabota.getSotrud().getKodSotr());
			pst.setBigDecimal(5, vRabota.getVremCh());
			pst.setBigDecimal(6, vRabota.getStoimCh());
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}
	
	public boolean deleteVRabota(BigDecimal kod) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="DELETE FROM v_rabota WHERE id_rab=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, kod);
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}
	
	//Использованные материалы
	public ArrayList<IspMater> loadIspMaters(BigDecimal kod) {
		ArrayList<IspMater> ispMaters = null;
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res;
			if (kod != null) {
				res = stmt.executeQuery("SELECT id_isp_m, kod_m, naim_m, cena, kol_vo FROM isp_mater_v where id_zn=" + kod.toString());
			} else {
				res = stmt.executeQuery("SELECT id_isp_m, kod_m, naim_m, cena, kol_vo, id_zn FROM isp_mater_v where id_zn=null");
			}
			ispMaters = new ArrayList<IspMater>();
			
			while (res.next()) {
				IspMater im = new IspMater();
				im.setIdIspM(res.getBigDecimal(1));
				Mater m = new Mater();
				m.setKodM(res.getBigDecimal(2));
				m.setNaimM(res.getString(3));
				im.setMater(m);
				im.setCena(res.getBigDecimal(4));
				im.setKolvo(res.getBigDecimal(5));
				ispMaters.add(im);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		return ispMaters;
	}
	
	public ArrayList<Mater> loadMaterForCmb() {
		ArrayList<Mater> maters = new ArrayList<Mater>();
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT kod_m, naim_m FROM mater order by kod_m");
			maters = new ArrayList<Mater>();
			while (res.next()) {
				Mater m = new Mater();
				m.setKodM(res.getBigDecimal(1));
				m.setNaimM(res.getString(2));
				maters.add(m);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
	          "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
			RollBack();
		}
		return maters;
	}

	public boolean updateIspMater(IspMater ispMater, BigDecimal key) {
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "UPDATE isp_mater set kod_m=?, id_zn=?, cena=?, kol_vo=? WHERE id_isp_m=?";
		try {  
			pst = con.prepareStatement(stm);
			if (ispMater.getMater() == null)
				pst.setBigDecimal(1, null);
			else
				pst.setBigDecimal(1, ispMater.getMater().getKodM());
			if (ispMater.getZakazN() == null)
				pst.setBigDecimal(2, null);
			else
				pst.setBigDecimal(2, ispMater.getZakazN().getIdZn());
			pst.setBigDecimal(3, ispMater.getCena());
			pst.setBigDecimal(4, ispMater.getKolvo());
			pst.setBigDecimal(5, key);
			pst.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
		return true;
	}
	
	public boolean addIspMater(IspMater ispMater) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm = "INSERT INTO isp_mater(id_isp_m, kod_m, id_zn, cena, kol_vo) VALUES(?,?,?,?,?)";
		try {  
			pst = con.prepareStatement(stm);
			ispMater.setIdIspM(getId("im_seq"));
			pst.setBigDecimal(1, ispMater.getIdIspM());
			if (ispMater.getMater() == null)
				pst.setBigDecimal(2, null);
			else
				pst.setBigDecimal(2, ispMater.getMater().getKodM());
			if (ispMater.getZakazN() == null)
				pst.setBigDecimal(3, null);
			else
				pst.setBigDecimal(3, ispMater.getZakazN().getIdZn());
			pst.setBigDecimal(4, ispMater.getCena());
			pst.setBigDecimal(5, ispMater.getKolvo());
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}
	
	public boolean deleteIspMater(BigDecimal kod) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="DELETE FROM isp_mater WHERE id_isp_m=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, kod);
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}

	//Заказы
	public ArrayList<Zakaz> loadZakazs() {
		ArrayList<Zakaz> zakazs = null;
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT id_zak, nom_zak, kod_kl, "
					+ "naim_kl, pr_obr, pr_otk, data_obr, status, "
					+ "kod_sotr, fio FROM zakaz_v");
			zakazs = new ArrayList<Zakaz>();
			
			while (res.next()) {
				Zakaz z = new Zakaz();
				z.setIdZak(res.getBigDecimal(1));
				z.setNomZak(res.getBigDecimal(2));
				Klient k = new Klient();
				k.setKodKl(res.getBigDecimal(3));
				k.setNaimKl(res.getString(4));
				z.setKlient(k);
				z.setPrObr(res.getString(5));
				z.setPrOtk(res.getString(6));
				z.setDataObr(res.getDate(7));
				z.setStatus(res.getString(8));
				Sotrud s = new Sotrud();
				s.setKodSotr(res.getBigDecimal(9));
				s.setFio(res.getString(10));
				z.setSotrud(s);
				zakazs.add(z);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
		}
		return zakazs;
	}
	
	public ArrayList<Klient> loadKlientForCmb() {
		ArrayList<Klient> klients = new ArrayList<Klient>();
		Connection con = this.getConnection();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery("SELECT kod_kl, naim_kl FROM klient order by kod_kl");
			klients = new ArrayList<Klient>();
			while (res.next()) {
				Klient k = new Klient();
				k.setKodKl(res.getBigDecimal(1));
				k.setNaimKl(res.getString(2));
				klients.add(k);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
	          "Ошибка получения данных", JOptionPane.ERROR_MESSAGE);
			RollBack();
		}
		return klients;
	}

	public boolean updateZakaz(Zakaz zakaz, BigDecimal key) {
		PreparedStatement pst = null;
		Connection con = this.getConnection();
		String stm = "UPDATE zakaz set nom_zak=?, kod_kl=?,"
					+ "pr_obr=?, pr_otk=?, data_obr=?, status=?,"
					+ "kod_sotr=? WHERE id_zak=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, zakaz.getNomZak());
			if (zakaz.getKlient() == null)
				pst.setBigDecimal(2, null);
			else
				pst.setBigDecimal(2, zakaz.getKlient().getKodKl());
			pst.setString(3, zakaz.getPrObr());
			pst.setString(4, zakaz.getPrOtk());
			pst.setDate(5, HelperConverter.convertFromJavaDateToSQLDate(zakaz.getDataObr()));
			pst.setString(6, zakaz.getStatus());
			if (zakaz.getSotrud() == null)
				pst.setBigDecimal(7, null);
			else
				pst.setBigDecimal(7, zakaz.getSotrud().getKodSotr());
			pst.setBigDecimal(8, key);
			pst.executeUpdate();
			con.commit();
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка изменения данных",JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
		return true;
	}
	
	public boolean addZakaz(Zakaz zakaz) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm = "INSERT INTO zakaz(id_zak, nom_zak, kod_kl,"
					+ " pr_obr, pr_otk, data_obr, status,"
					+ "kod_sotr) VALUES(?,?,?,?,?,?,?,?)";
		try {  
			pst = con.prepareStatement(stm);
			zakaz.setIdZak(getId("z_seq"));
			pst.setBigDecimal(1, zakaz.getIdZak());
			pst.setBigDecimal(2, zakaz.getNomZak());
			if (zakaz.getKlient() == null)
				pst.setBigDecimal(3, null);
			else
				pst.setBigDecimal(3, zakaz.getKlient().getKodKl());
			pst.setString(4, zakaz.getPrObr());
			pst.setString(5, zakaz.getPrOtk());
			pst.setDate(6, HelperConverter.convertFromJavaDateToSQLDate(zakaz.getDataObr()));
			pst.setString(7, zakaz.getStatus());
			if (zakaz.getSotrud() == null)
				pst.setBigDecimal(8, null);
			else
				pst.setBigDecimal(8, zakaz.getSotrud().getKodSotr());
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			RollBack();
			JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка добавления данных", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}
	
	public boolean deleteZakaz(BigDecimal kod) {
		PreparedStatement pst = null;
		Connection con=this.getConnection();
		String stm ="DELETE FROM zakaz WHERE id_zak=?";
		try {  
			pst = con.prepareStatement(stm);
			pst.setBigDecimal(1, kod);
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}	
		}
	}
	
	//Вывод отчета
	public ArrayList<RptData> getDataReport(RptParams params) {
		Connection con = this.getConnection();
		PreparedStatement pst = null;
		String stm = "SELECT * FROM report_proc(?,?)";
		try {
			pst = con.prepareStatement(stm);
			if (params.begDate == null)
				pst.setDate(1, null);
			else
				pst.setDate(1, HelperConverter.convertFromJavaDateToSQLDate(
						params.begDate));
			if (params.endDate == null)
				pst.setDate(2, null);
			else
				pst.setDate(2, HelperConverter.convertFromJavaDateToSQLDate(
						params.endDate));
			ResultSet res = pst.executeQuery();
			ArrayList<RptData> rptData = new ArrayList<>();
			while (res.next()) {
				RptData rowData = new RptData();
				rowData.setModel(res.getString(1));
				rowData.setMonth(res.getString(2));
				rowData.setKolvo(res.getInt(3));
				rowData.setStoimrab(res.getBigDecimal(4));
				rowData.setStoimm(res.getBigDecimal(5));
				rowData.setAvgtime(res.getBigDecimal(6));
				rptData.add(rowData);
			}
			res.close();
			return rptData;
		} catch(Exception e) {
			e.printStackTrace();
			return null; 
		}
	}
	
	public void ShowRpt(RptParams params) {
		JasperPrint jp=null;
		ArrayList<RptData> reportData = getDataReport(params);
		JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(reportData);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begDate", params.begDate);
		map.put("endDate", params.endDate);
		String reportName = "reports/report2.jasper";
		try {
			jp = JasperFillManager.fillReport(reportName, map, ds);	
		} catch (JRException e) {
			e.printStackTrace();
		}
		JasperViewer viewer = new JasperViewer(jp, false);
		viewer.setTitle("Отчет по ремонтам");
		viewer.setVisible(true);
	}
}
