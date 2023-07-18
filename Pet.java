package pet_store;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.Color;
import javax.swing.JFormattedTextField;

import com.toedter.calendar.*;




public class Pet {

	int petId;
	float price;
	String type;
	String dateOfBirth;
	int branchId;
	
	
	Connection conn;
	Statement stmt;
	
	
	JPanel mainPet;
	
	private JTextField PetIdFel;
	private JTextField PriceFel;
	private JTextField TypeFel;
	private JComboBox<Integer> BranchIdFel;
	private JTable TablePet;
	private JDateChooser PetDateOfBirthFel;


	

	/**
	 * Create the application.
	 */
	public Pet() {
		conn = Branch.conn;
		stmt = Branch.stmt;
		initialize();
	}

	void displayPet() {
		String query = "SELECT * FROM pet WHERE Pid NOT IN (select PetId from purchases) ";
		DefaultTableModel model = (DefaultTableModel) TablePet.getModel();
		model.setRowCount(0);
		try {
			ResultSet res = stmt.executeQuery(query);
			while(res.next()) {
				petId = res.getInt(1);
				price = res.getFloat(2);
				type = res.getString(3);
				dateOfBirth = res.getString(4);
				branchId = res.getInt(5);
				model.addRow(new Object[]{petId,price,type,dateOfBirth,branchId});
				branchId = 0;
				price = 0;
				type = null;
				dateOfBirth = null;
				branchId = 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Error in displaying pets\n" + e.getMessage() ,"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	void insertPet() {
		
		try {
			if(PetIdFel.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Please fill pet id  because it is required","Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if(PetDateOfBirthFel.getDate() == null) {
				JOptionPane.showMessageDialog(null, "Please fill pet date of birth  because it is required","Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			petId = Integer.parseInt(PetIdFel.getText());
			if(PriceFel.getText().equals("")) {
				price = 0;
			}else {
				price = Float.parseFloat(PriceFel.getText());			
			}
			type = TypeFel.getText();
			dateOfBirth = FilterDate(String.valueOf(PetDateOfBirthFel.getDate()));
			branchId = (Integer) BranchIdFel.getSelectedItem();
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "one of the types is not compatible\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String query = "INSERT INTO pet VALUES('" + petId + "','"+ price + "','" + type + "','" + dateOfBirth + "','"+ branchId + "')"; 
		try {
			stmt.execute(query);
			JOptionPane.showMessageDialog(null, "Pet inserted successfully","FEEDBACK", JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Could not insert the pet\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
		}
		PetIdFel.setText("");
		PriceFel.setText("");
		TypeFel.setText("");
		BranchIdFel.setSelectedItem(BranchIdFel.getItemAt(0));
		PetDateOfBirthFel.setDate(null);
	}
	
	void deletePet() {
		
		
		try {
			if(PetIdFel.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Please specify the Pet which you want to delete","Warning", JOptionPane.WARNING_MESSAGE);
				return ;
			}
			petId = Integer.parseInt(PetIdFel.getText());
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "one of the types is not compatible\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return;

		}
		String query = "DELETE FROM pet WHERE Pid =" + petId;
		try {
			int a = stmt.executeUpdate(query);
			
			if(a >= 1) {
				JOptionPane.showMessageDialog(null, "Pet Deleted successfully","FEEDBACK", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, "Pet does not Exists","WARNING", JOptionPane.WARNING_MESSAGE);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Could not delete pet:\n" + e.getMessage() ,"ERROR", JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(null, "Error in reading branch ids\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		
		
		mainPet = new JPanel();
		mainPet.setBounds(179, 0, 803, 598);
		mainPet.setLayout(null);
		JLabel PetIdLab = new JLabel("pet id :");
		PetIdLab.setBounds(84, 67, 66, 35);
		PetIdLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainPet.add(PetIdLab);
		
		PetIdFel = new JTextField();
		PetIdFel.setBounds(160, 72, 200, 30);
		PetIdFel.setColumns(10);
		mainPet.add(PetIdFel);
		
		JLabel PriceLab = new JLabel("price:");
		PriceLab.setBounds(425, 67, 45, 35);
		PriceLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainPet.add(PriceLab);
		
		PriceFel = new JTextField();
		PriceFel.setBounds(480, 72, 200, 30);
		PriceFel.setColumns(10);
		mainPet.add(PriceFel);
		
		JLabel typeLab = new JLabel("type:");
		typeLab.setBounds(99, 136, 45, 35);
		typeLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainPet.add(typeLab);
		
		TypeFel = new JTextField();
		TypeFel.setBounds(160, 141, 200, 30);
		TypeFel.setColumns(10);
		mainPet.add(TypeFel);
		
		JLabel BranchIdLab = new JLabel("branch id:");
		BranchIdLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		BranchIdLab.setBounds(388, 136, 89, 35);
		mainPet.add(BranchIdLab);
		
		BranchIdFel = new JComboBox<Integer>();
		addBranchFeilds();
		BranchIdFel.setBounds(480, 141, 200, 30);
		mainPet.add(BranchIdFel);
		
		JLabel DateOfBirthLab = new JLabel("date of birth:");
		DateOfBirthLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		DateOfBirthLab.setBounds(32, 200, 120, 35);
		mainPet.add(DateOfBirthLab);
			
		PetDateOfBirthFel = new JDateChooser();
		PetDateOfBirthFel.setDateFormatString("yyyy-MM-dd");
		PetDateOfBirthFel.setMaxSelectableDate(new Date());

		PetDateOfBirthFel.setBounds(160, 205, 218, 30);
		mainPet.add(PetDateOfBirthFel);
		
		PetDateOfBirthFel.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO Auto-generated method stub
				if(evt.getPropertyName().equals("date")) {
					
				}
			}
		});
		
		
		
		
		
		TablePet = new JTable();
		TablePet.setBackground(new Color(255, 255, 255));
		TablePet.setRowHeight(23);
		TablePet.setFont(new Font("Tahoma", Font.PLAIN, 15));
		TablePet.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Pet id", "Price", "Type","Date of birth", "branch id"
			}
		){
			 @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
		});
		TablePet.getColumnModel().getColumn(0).setPreferredWidth(100);
		TablePet.getColumnModel().getColumn(1).setPreferredWidth(100);
		TablePet.getColumnModel().getColumn(2).setPreferredWidth(100);
		TablePet.getColumnModel().getColumn(3).setPreferredWidth(100);
		TablePet.getColumnModel().getColumn(4).setPreferredWidth(100);
		TablePet.setBounds(10, 275, 790, 323);
		TablePet.getTableHeader().setBackground(Color.LIGHT_GRAY);
		JScrollPane TablePetScroll = new JScrollPane(TablePet);
		TablePetScroll.setBounds(10, 275, 790, 323);
		mainPet.add(TablePetScroll);
		
		JButton PetInsertBtn = new JButton("insert");
		PetInsertBtn.setBounds(421, 197, 89, 42);
		PetInsertBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		PetInsertBtn.setBackground(Color.GREEN);
		PetInsertBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				insertPet();
			}
		});
		mainPet.add(PetInsertBtn);
		
		JButton PetDisplayBtn = new JButton("display");
		PetDisplayBtn.setBounds(537, 197, 89, 42);
		PetDisplayBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		PetDisplayBtn.setBackground(new Color(0, 128, 255));
		
		PetDisplayBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displayPet();
			}
		});
		mainPet.add(PetDisplayBtn);
		
		JButton PetbtnDelete = new JButton("delete");
		PetbtnDelete.setBounds(649, 197, 89, 42);
		PetbtnDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		PetbtnDelete.setBackground(Color.RED);
		
		PetbtnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				deletePet();
			}
		});
		
		mainPet.add(PetbtnDelete);
		
		
		
		
		mainPet.setVisible(true);
		displayPet();

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
		
	
}
