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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;

public class Purchase {
	
	int purchaseId;
	int customerId;
	int petId;
	String dateOfPurchase;

	Connection conn;
	Statement stmt;
	
	public JPanel mainPurchase;
	private JTable TablePurchase;
	private JDateChooser PurchaseDateFel;
	private JComboBox CustomerIdFel;
	private JComboBox PetIdFel;
	private JTextField PurchaseIdFel;


	

	/**
	 * Create the application.
	 */
	public Purchase() {
		conn = Branch.conn;
		stmt = Branch.stmt;
		initialize();
	}
	
	
	
	void displayPurchases() {
		String query = "SELECT * FROM purchases";
		DefaultTableModel model = (DefaultTableModel) TablePurchase.getModel();
		model.setRowCount(0);
		try {
			ResultSet res = stmt.executeQuery(query);
			while(res.next()) {
				purchaseId = res.getInt(1);
				customerId = res.getInt(2);
				petId = res.getInt(3);
				dateOfPurchase = res.getString(4);
				model.addRow(new Object[]{purchaseId,customerId,petId,dateOfPurchase});
				purchaseId = 0;
				customerId = 0;
				petId = 0;
				dateOfPurchase = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Error in displaying purchases\n" + e.getMessage() ,"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	void insertPurchase() {
		
		try {
			if(String.valueOf( PurchaseDateFel.getDate()).equals("null") || PurchaseIdFel.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Please fill Purchase id and purchase date because they are required","Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			purchaseId = Integer.parseInt(PurchaseIdFel.getText());
			customerId = (Integer) CustomerIdFel.getSelectedItem();
			petId = (Integer) PetIdFel.getSelectedItem();
			dateOfPurchase =  FilterDate(String.valueOf(PurchaseDateFel.getDate()));
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "one of the types is not compatible\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return;

		}
		
		String query = "INSERT INTO purchases VALUES('" + purchaseId + "','"+ customerId + "','" + petId + "','" + dateOfPurchase + "')"; 
		
		try {
			stmt.execute(query);
			JOptionPane.showMessageDialog(null, "Purchase inserted successfully","FEEDBACK", JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Could not insert the Purchase\n"+ e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
		PurchaseIdFel.setText("");
		CustomerIdFel.setSelectedItem(CustomerIdFel.getItemAt(0));
		PetIdFel.setSelectedItem(PetIdFel.getItemAt(0));
		PurchaseDateFel.setDate(null);
	}
	
		void deletePurchase() {
		
		// what if empty
			
		if(PurchaseIdFel.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Please specify Purchase id which you want to delete","Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}
		purchaseId = Integer.parseInt(PurchaseIdFel.getText());
		customerId = (Integer) CustomerIdFel.getSelectedItem();
		petId = (Integer) PetIdFel.getSelectedItem();
		
		// made it delete by Purchase id and customer id only?
		String query = "DELETE FROM purchases WHERE CustomerId = '" + customerId +"' and PurchaseID= '" + purchaseId +"'";

		try {
			int a = stmt.executeUpdate(query);
			if(a >= 1) {
				JOptionPane.showMessageDialog(null, "Purchase has been Deleted successfully","FEEDBACK", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, "Purchase does not Exists","Warning", JOptionPane.WARNING_MESSAGE);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Could not delete Purchase:\n" + e.getMessage() ,"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		
		
		mainPurchase = new JPanel();
		mainPurchase.setBounds(179, 0, 803, 598);
		mainPurchase.setLayout(null);
		
		JLabel PurchaseIdLab = new JLabel("Purchase id:");
		PurchaseIdLab.setBounds(35, 67, 115, 35);
		PurchaseIdLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainPurchase.add(PurchaseIdLab);
		
		PurchaseIdFel = new JTextField();
		PurchaseIdFel.setBounds(141, 70, 175, 35);
		mainPurchase.add(PurchaseIdFel);
		PurchaseIdFel.setColumns(10);
		
		JLabel CustomerIdLab = new JLabel("Customer id:");
		CustomerIdLab.setBounds(448, 67, 122, 35);
		CustomerIdLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainPurchase.add(CustomerIdLab);
		
		JLabel PetIdLab = new JLabel("Pet id:");
		PetIdLab.setBounds(61, 136, 109, 35);
		PetIdLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainPurchase.add(PetIdLab);
		
		
		JLabel DateOfPurchaseLab = new JLabel("Date of purchase:");
		DateOfPurchaseLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		DateOfPurchaseLab.setBounds(421, 136, 141, 35);
		mainPurchase.add(DateOfPurchaseLab);
		
		PurchaseDateFel = new JDateChooser();
		PurchaseDateFel.setDateFormatString("yyyy-MM-dd");
		PurchaseDateFel.setMinSelectableDate(new Date());
		PurchaseDateFel.setBounds(570, 136, 218, 35);
		mainPurchase.add(PurchaseDateFel);
		PurchaseDateFel.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO Auto-generated method stub
				if(evt.getPropertyName().equals("date")) {
					
				}
			}
		});
		
		CustomerIdFel = new JComboBox<Integer>();
		CustomerIdFel.setBounds(569, 76, 128, 22);
		mainPurchase.add(CustomerIdFel);
		addCustomerFeilds();

		
		PetIdFel = new JComboBox<Integer>();
		PetIdFel.setBounds(132, 145, 128, 22);
		mainPurchase.add(PetIdFel);
		addPetFeilds();

		
		
		
		
		
		
		TablePurchase = new JTable();
		TablePurchase.setBackground(new Color(255, 255, 255));
		TablePurchase.setRowHeight(23);
		TablePurchase.setFont(new Font("Tahoma", Font.PLAIN, 15));
		TablePurchase.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Purchase id", "Customer id", "Pet id","Date of Purchase"
			}
		){
			 @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
		});
		TablePurchase.getColumnModel().getColumn(0).setPreferredWidth(100);
		TablePurchase.getColumnModel().getColumn(1).setPreferredWidth(100);
		TablePurchase.getColumnModel().getColumn(2).setPreferredWidth(100);
		TablePurchase.getColumnModel().getColumn(3).setPreferredWidth(100);
		TablePurchase.setBounds(10, 275, 790, 323);
		TablePurchase.getTableHeader().setBackground(Color.LIGHT_GRAY);
		JScrollPane TablePurchaseScroll = new JScrollPane(TablePurchase);
		TablePurchaseScroll.setBounds(10, 275, 790, 323);
		mainPurchase.add(TablePurchaseScroll);
		
		JButton PurchaseInsertBtn = new JButton("insert");
		PurchaseInsertBtn.setBounds(421, 197, 89, 42);
		PurchaseInsertBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		PurchaseInsertBtn.setBackground(Color.GREEN);
		
		PurchaseInsertBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				insertPurchase();
			}
		});
		
		mainPurchase.add(PurchaseInsertBtn);
		
		JButton PurchaseDisplayBtn = new JButton("display");
		PurchaseDisplayBtn.setBounds(537, 197, 89, 42);
		PurchaseDisplayBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		PurchaseDisplayBtn.setBackground(new Color(0, 128, 255));
		
		PurchaseDisplayBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displayPurchases();
			}
		});
		mainPurchase.add(PurchaseDisplayBtn);
		
		JButton PurchasebtnDelete = new JButton("delete");
		PurchasebtnDelete.setBounds(649, 197, 89, 42);
		PurchasebtnDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		PurchasebtnDelete.setBackground(Color.RED);
		
		PurchasebtnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				deletePurchase();
			}
		});
		
		mainPurchase.add(PurchasebtnDelete);
		
		
		
		
		mainPurchase.setVisible(true);
		displayPurchases();

	}
	
	
	void addCustomerFeilds() {
		String query = "SELECT Custid FROM Customer";
		try {
			ResultSet res = stmt.executeQuery(query);
			while(res.next()) {
				customerId = res.getInt(1);
				CustomerIdFel.addItem(customerId);
				customerId = 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Error in reading customer ids\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
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
	String FilterDate(String s) {
		String[] tmp = s.split(" ");
		
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
