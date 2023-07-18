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
import java.util.Date;

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

public class Appointment {

	int clinicId;
	int petId;
	String date;
	String time;
	float cost;
	
	Connection conn;
	Statement stmt;
	
	public JPanel mainAppointment;
	private JTable TableAppointment;
	private JComboBox<Integer> ClinicIdFel;
	private JComboBox<String> MinutsFel;
	private  JComboBox<String> HoursFel;
	private JDateChooser AppointmentDateFel;
	private JComboBox<Integer> PetIdFel;
	private JTextField CostFel;

	

	
	public Appointment() {
		conn = Branch.conn;
		stmt = Branch.stmt;
		initialize();
	}

	void displayAppointment() {
		String query = "SELECT * FROM appointment";
		DefaultTableModel model = (DefaultTableModel) TableAppointment.getModel();
		model.setRowCount(0);
		try {
			ResultSet res = stmt.executeQuery(query);
			while(res.next()) {
				clinicId = res.getInt(1);
				petId = res.getInt(2);
				date = res.getString(3);
				time = res.getString(4);
				cost = res.getFloat(5);
				model.addRow(new Object[]{clinicId,petId,date,time,cost});
				clinicId = 0;
				petId = 0;
				date = null;
				time = null;
				cost = 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Error in displaying appointments\n" + e.getMessage() ,"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	void insertAppointment() {
		
		try {
			if(AppointmentDateFel.getDate() == null) {
				JOptionPane.showMessageDialog(null, "Please fill appointment date because it is required","Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			clinicId = (Integer) ClinicIdFel.getSelectedItem();
			petId = (Integer) PetIdFel.getSelectedItem();
			date = FilterDate( String.valueOf( AppointmentDateFel.getDate()) );
			time = HoursFel.getSelectedItem() + ":"+ MinutsFel.getSelectedItem() +":00"; 
			if(CostFel.getText().equals("")) {
				cost = 0;
			}else {
				cost = Float.parseFloat(CostFel.getText());
			}
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "one of the types is not compatible\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);

		}
		
		String query = "INSERT INTO appointment VALUES('" + clinicId + "','"+ petId + "','" + date + "','" + time + "','" + cost + "')"; 
		try {
			stmt.execute(query);
			JOptionPane.showMessageDialog(null, "Appointment inserted successfully","FEEDBACK", JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Could not insert the Appointment\n"+ e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
		ClinicIdFel.setSelectedItem(ClinicIdFel.getItemAt(0));
		PetIdFel.setSelectedItem(PetIdFel.getItemAt(0));
		HoursFel.setSelectedItem(HoursFel.getItemAt(0));
		MinutsFel.setSelectedItem(MinutsFel.getItemAt(0));
		CostFel.setText("");
		AppointmentDateFel.setDate(null);

	}

	void deleteAppointment() {
		
		// what if empty
		
		try {
			if(AppointmentDateFel.getDate()== null) {
				JOptionPane.showMessageDialog(null, "Please fill appointment date because it is required","Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			clinicId = (Integer) ClinicIdFel.getSelectedItem();
			petId = (Integer) PetIdFel.getSelectedItem();
			date =  FilterDate( String.valueOf( AppointmentDateFel.getDate() ) );
			time = HoursFel.getSelectedItem() + ":"+ MinutsFel.getSelectedItem() +":00"; 
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "one of the types is not compatible\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String query = "DELETE FROM appointment WHERE clinic_id = '" + clinicId +"' and Date= '" + date +"' and PetId= '" + petId + "'" + "and Time='" + time + "'";
		try {
			int a = stmt.executeUpdate(query);
			if(a >= 1) {
				JOptionPane.showMessageDialog(null, "Appointment has been Deleted successfully","FEEDBACK", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, "Appointment does not Exists","Warning", JOptionPane.WARNING_MESSAGE);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Could not delete Appointment:\n" + e.getMessage() ,"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		
		
		mainAppointment = new JPanel();
		mainAppointment.setBounds(179, 0, 803, 598);
		mainAppointment.setLayout(null);
		
	
		
		JLabel PetIdLab = new JLabel("Pet id:");
		PetIdLab.setBounds(307, 67, 100, 35);
		PetIdLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainAppointment.add(PetIdLab);
		
		PetIdFel = new JComboBox();
		addPetFeilds();
		PetIdFel.setBounds(369, 76, 70, 22);
		mainAppointment.add(PetIdFel);

		
		JLabel DateLab = new JLabel("Appoitnment date:");
		DateLab.setBounds(10, 136, 158, 35);
		DateLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainAppointment.add(DateLab);
		
		AppointmentDateFel = new JDateChooser();
		AppointmentDateFel.setDateFormatString("yyyy-MM-dd");
		AppointmentDateFel.setBounds(160, 136, 188, 35);
		AppointmentDateFel.setMinSelectableDate(new Date());
		mainAppointment.add(AppointmentDateFel);
		AppointmentDateFel.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO Auto-generated method stub
				if(evt.getPropertyName().equals("date")) {
					
				}
			}
		});
		
		
		
		
		
		
		JLabel TimeLab = new JLabel("Appointment time (HH-MM):");
		TimeLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		TimeLab.setBounds(358, 136, 229, 35);
		mainAppointment.add(TimeLab);
		
		JLabel ClinicIdLab = new JLabel("Clinic id:");
		ClinicIdLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		ClinicIdLab.setBounds(88, 67, 109, 35);
		mainAppointment.add(ClinicIdLab);
		
		ClinicIdFel = new JComboBox<Integer>();
		addClinicFeilds();
		ClinicIdFel.setBounds(160, 76, 77, 22);
		mainAppointment.add(ClinicIdFel);
		
		HoursFel = new JComboBox<String>();
		for(int i = 0 ; i <= 24 ; i++) {
			if( i < 10) {
				HoursFel.addItem("0"+i);

			}else {
				HoursFel.addItem(i+"");
			}
		}
		HoursFel.setBounds(597, 145, 62, 22);
		mainAppointment.add(HoursFel);
		
		MinutsFel = new JComboBox<String>();
		for(int i = 0 ; i <= 59 ; i++){
			if(i <10) {
				MinutsFel.addItem("0"+i);
			}else {
				MinutsFel.addItem(i+"");
				
			}
		}
		MinutsFel.setBounds(669, 145, 62, 22);
		mainAppointment.add(MinutsFel);
		
		
		TableAppointment = new JTable();
		TableAppointment.setBackground(new Color(255, 255, 255));
		TableAppointment.setRowHeight(23);
		TableAppointment.setFont(new Font("Tahoma", Font.PLAIN, 15));
		TableAppointment.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Clinic id", "Pet id", "Date","Time", "cost"
			}
		){
			 @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
		});
		TableAppointment.getColumnModel().getColumn(0).setPreferredWidth(100);
		TableAppointment.getColumnModel().getColumn(1).setPreferredWidth(100);
		TableAppointment.getColumnModel().getColumn(2).setPreferredWidth(100);
		TableAppointment.getColumnModel().getColumn(3).setPreferredWidth(100);
		TableAppointment.getColumnModel().getColumn(4).setPreferredWidth(100);
		TableAppointment.setBounds(10, 275, 790, 323);
		TableAppointment.getTableHeader().setBackground(Color.LIGHT_GRAY);
		JScrollPane TableAppointmentScroll = new JScrollPane(TableAppointment);
		TableAppointmentScroll.setBounds(10, 275, 790, 323);
		mainAppointment.add(TableAppointmentScroll);
		
		JButton AppointmentInsertBtn = new JButton("insert");
		AppointmentInsertBtn.setBounds(421, 197, 89, 42);
		AppointmentInsertBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		AppointmentInsertBtn.setBackground(Color.GREEN);
		
		AppointmentInsertBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				insertAppointment();
			}
		});
		mainAppointment.add(AppointmentInsertBtn);
		
		JButton AppointmentDisplayBtn = new JButton("display");
		AppointmentDisplayBtn.setBounds(537, 197, 89, 42);
		AppointmentDisplayBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		AppointmentDisplayBtn.setBackground(new Color(0, 128, 255));
		
		AppointmentDisplayBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displayAppointment();
			}
		});
		mainAppointment.add(AppointmentDisplayBtn);
		
		JButton AppointmentbtnDelete = new JButton("delete");
		AppointmentbtnDelete.setBounds(649, 197, 89, 42);
		AppointmentbtnDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		AppointmentbtnDelete.setBackground(Color.RED);
		
		AppointmentbtnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				deleteAppointment();
			}
		});
		
		mainAppointment.add(AppointmentbtnDelete);
		
		JLabel CostLab = new JLabel("Cost:");
		CostLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		CostLab.setBounds(476, 67, 100, 35);
		mainAppointment.add(CostLab);
		
		CostFel = new JTextField();
		CostFel.setBounds(530, 67, 126, 35);
		mainAppointment.add(CostFel);
		CostFel.setColumns(10);
		
		
		
		
		
		
		
		
		mainAppointment.setVisible(true);
		displayAppointment();

	}
	
	String FilterDate(String s) {
		String [] tmp = s.split(" ");
		
		switch(tmp[1]) {
			
			case "Jan":{
				tmp[1] = "01";
				break;
			}
			case "Feb":{
				tmp[1] = "02";
				break;
			}
			case "Mar":{
				tmp[1] = "03";
				break;
			}
			case "Apr":{
				tmp[1] = "04";
				break;
			}
			case "May":{
				tmp[1] = "05";
				break;
			}
			case "Jun":{
				tmp[1] = "06";
				break;
			}
			case "Jul":{
				tmp[1] = "07";
				break;
			}
			case "Aug":{
				tmp[1] = "08";
				break;
			}
			case "Sep":{
				tmp[1] = "09";
				break;
			}
			case "Oct":{
				tmp[1] = "10";
				break;
			}
			case "Nov":{
				tmp[1] = "11";
				break;
			}
			case "Dec":{
				tmp[1] = "12";
				break;
			}
		}
		
		return tmp[5] + "-" + tmp[1] + "-" + tmp[2];
	}
	
	void addClinicFeilds() {
		String query = "SELECT Cid FROM clinic";
		try {
			ResultSet res = stmt.executeQuery(query);
			while(res.next()) {
				clinicId = res.getInt(1);
				ClinicIdFel.addItem(clinicId);
				clinicId = 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Error in reading clinic ids\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	void addPetFeilds() {
		String query = "SELECT Pid FROM pet";
		try {
			ResultSet res = stmt.executeQuery(query);
			while(res.next()) {
				petId = res.getInt(1);
				PetIdFel.addItem(petId);
				petId = 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Error in reading pet ids\n"+ e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
}
