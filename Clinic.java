package pet_store;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

public class Clinic {

	
	int clinicId;
	String doctorSsn;
	String doctorName;
	String doctorPhoneNumber;
	int branchId;
	
	
	Connection conn;
	Statement stmt;
	
	
	
	public JPanel mainClinic;
	
	private JTextField ClinicIdFel;
	private JTextField DoctorSsnFel;
	private JTextField DoctorNameFel;
	private JTable TableClinic;
	private JTextField DoctorPhoneFel;
	private JComboBox<Integer> BranchIdFel;


	

	/**
	 * Create the application.
	 */
	public Clinic() {
		conn = Branch.conn;
		stmt = Branch.stmt;
		initialize();
	}

	
	void displayClinic() {
		String query = "SELECT * FROM clinic";
		DefaultTableModel model = (DefaultTableModel) TableClinic.getModel();
		model.setRowCount(0);
		try {
			ResultSet res = stmt.executeQuery(query);
			while(res.next()) {
					
				clinicId = res.getInt(1);
				doctorSsn = res.getString(2);
				doctorName = res.getString(3);
				doctorPhoneNumber = res.getString(4);
				branchId = res.getInt(5);
				
				model.addRow(new Object[]{clinicId,doctorSsn,doctorName,doctorPhoneNumber,branchId});
				branchId = 0;
				clinicId = 0;
				doctorSsn = null;
				doctorPhoneNumber = null;
				branchId = 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Error in displaying clinics\n" + e.getMessage() ,"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	void insertClinic() {
		
		try {
			if(ClinicIdFel.getText().equals("") || DoctorSsnFel.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Please fill Clinic id and doctor snn because they are required","Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			branchId = (Integer) BranchIdFel.getSelectedItem();
			clinicId = Integer.parseInt(ClinicIdFel.getText());
			doctorSsn = DoctorSsnFel.getText();
			doctorName = DoctorNameFel.getText();
			doctorPhoneNumber = DoctorPhoneFel.getText();
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "one of the types is not compatible\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return;

		}
		
		
		String query = "INSERT INTO clinic VALUES('" + clinicId + "','"+ doctorSsn + "','" + doctorName + "','" + doctorPhoneNumber + "','"+ branchId + "')"; 
		try {
			stmt.execute(query);
			JOptionPane.showMessageDialog(null, "Clinic inserted successfully","FEEDBACK", JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Could not insert the clinic\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
		BranchIdFel.setSelectedItem( BranchIdFel.getItemAt(0));
		ClinicIdFel.setText("");
		DoctorSsnFel.setText("");
		DoctorNameFel.setText("");
		DoctorPhoneFel.setText("");
	}
	
	void deleteClinic() {
		
		try {
			if(ClinicIdFel.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Please specify the Clinic which you want to delete","Warning", JOptionPane.WARNING_MESSAGE);
				return ;
			}
			clinicId = Integer.parseInt( ClinicIdFel.getText());
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "one of the types is not compatible\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String query = "DELETE FROM clinic WHERE cid =" + clinicId;
		try {
			int a = stmt.executeUpdate(query);
			
			if(a >= 1) {
				JOptionPane.showMessageDialog(null, "Clinic Deleted successfully","FEEDBACK", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, "Clinic does not Exists","Warning", JOptionPane.WARNING_MESSAGE);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Could not delete clinic:\n" + e.getMessage() ,"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	
	
	}
	
	
	
	void addBranchFeilds() {
		String query = "SELECT Bid FROM branch";
		try {
			ResultSet res = stmt.executeQuery(query);
			while(res.next()) {
				branchId = res.getInt(1);
				BranchIdFel.addItem(branchId);
				branchId = 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error in reading branch ids\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		
		
		mainClinic = new JPanel();
		mainClinic.setBounds(179, 0, 803, 598);
		mainClinic.setLayout(null);
		JLabel ClinicIdLab = new JLabel("Clinic id:");
		ClinicIdLab.setBounds(73, 67, 77, 35);
		ClinicIdLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainClinic.add(ClinicIdLab);
		
		ClinicIdFel = new JTextField();
		ClinicIdFel.setBounds(160, 72, 200, 30);
		ClinicIdFel.setColumns(10);
		mainClinic.add(ClinicIdFel);
		
		JLabel DoctorSsnLab = new JLabel("Doctor ssn:");
		DoctorSsnLab.setBounds(463, 67, 100, 35);
		DoctorSsnLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainClinic.add(DoctorSsnLab);
		
		DoctorSsnFel = new JTextField();
		DoctorSsnFel.setBounds(569, 72, 200, 30);
		DoctorSsnFel.setColumns(10);
		mainClinic.add(DoctorSsnFel);
		
		JLabel DoctorName = new JLabel("Doctor name:");
		DoctorName.setBounds(35, 136, 109, 35);
		DoctorName.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainClinic.add(DoctorName);
		
		DoctorNameFel = new JTextField();
		DoctorNameFel.setBounds(160, 141, 200, 30);
		DoctorNameFel.setColumns(10);
		mainClinic.add(DoctorNameFel);
		
		
		JLabel DoctorPhoneNumLab = new JLabel("Doctor phone number:");
		DoctorPhoneNumLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		DoctorPhoneNumLab.setBounds(370, 136, 193, 35);
		mainClinic.add(DoctorPhoneNumLab);
		
		DoctorPhoneFel = new JTextField();
		DoctorPhoneFel.setColumns(10);
		DoctorPhoneFel.setBounds(569, 141, 200, 30);
		mainClinic.add(DoctorPhoneFel);
		
		JLabel BranchIdLab = new JLabel("Branch id:");
		BranchIdLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		BranchIdLab.setBounds(59, 197, 109, 35);
		mainClinic.add(BranchIdLab);
		
		BranchIdFel = new JComboBox<Integer>();
		addBranchFeilds();
		BranchIdFel.setBounds(160, 202, 200, 30);
		mainClinic.add(BranchIdFel);
		
		
		
		
		TableClinic = new JTable();
		TableClinic.setBackground(new Color(255, 255, 255));
		TableClinic.setRowHeight(23);
		TableClinic.setFont(new Font("Tahoma", Font.PLAIN, 15));
		TableClinic.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Clinic id", "Doctor ssn", "Doctor name","Doctor phone number", "branch id"
			}
		){
			 @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
		});
		TableClinic.getColumnModel().getColumn(0).setPreferredWidth(100);
		TableClinic.getColumnModel().getColumn(1).setPreferredWidth(100);
		TableClinic.getColumnModel().getColumn(2).setPreferredWidth(100);
		TableClinic.getColumnModel().getColumn(3).setPreferredWidth(100);
		TableClinic.getColumnModel().getColumn(4).setPreferredWidth(100);
		TableClinic.setBounds(10, 275, 790, 323);
		TableClinic.getTableHeader().setBackground(Color.LIGHT_GRAY);
		JScrollPane TableClinicScroll = new JScrollPane(TableClinic);
		TableClinicScroll.setBounds(10, 275, 790, 323);
		mainClinic.add(TableClinicScroll);
		
		JButton ClinicInsertBtn = new JButton("insert");
		ClinicInsertBtn.setBounds(421, 197, 89, 42);
		ClinicInsertBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		ClinicInsertBtn.setBackground(Color.GREEN);
		
		ClinicInsertBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				insertClinic();
			}
		});
		mainClinic.add(ClinicInsertBtn);
		
		JButton ClinicDisplayBtn = new JButton("display");
		ClinicDisplayBtn.setBounds(537, 197, 89, 42);
		ClinicDisplayBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		ClinicDisplayBtn.setBackground(new Color(0, 128, 255));
		ClinicDisplayBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displayClinic();
			}
		});
		mainClinic.add(ClinicDisplayBtn);
		
		JButton ClinicbtnDelete = new JButton("delete");
		ClinicbtnDelete.setBounds(649, 197, 89, 42);
		ClinicbtnDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		ClinicbtnDelete.setBackground(Color.RED);
		
		ClinicbtnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				deleteClinic();
			}
		});
		
		mainClinic.add(ClinicbtnDelete);
		
		
		
		
		
		mainClinic.setVisible(true);
		displayClinic();

	}
}
