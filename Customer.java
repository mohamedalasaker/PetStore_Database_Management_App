package pet_store;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Customer {
	
	int CustomerId;
	String firstName;
	String lastName;
	String phoneNumber;
	
	

	Connection conn;
	Statement stmt;
	
	public JPanel mainCustomer;
	private JTextField CustomerIdIdFel;
	private JTextField CustomerFnameFel;
	private JTextField CustomerLnameFel;
	private JTable TableCustomer;
	private JTextField CustomerPhoneFel;


	

	/**
	 * Create the application.
	 */
	public Customer() {
		conn = Branch.conn;
		stmt = Branch.stmt;
		initialize();
	}

	
	void displayCustomer() {
		String query = "SELECT * FROM customer";
		DefaultTableModel model = (DefaultTableModel) TableCustomer.getModel();
		model.setRowCount(0);
		try {
			ResultSet res = stmt.executeQuery(query);
			while(res.next()) {
				CustomerId = res.getInt(1);
				firstName = res.getString(2);
				lastName = res.getString(3);
				phoneNumber = res.getString(4);
				model.addRow(new Object[]{CustomerId,firstName,lastName,phoneNumber});
				CustomerId = 0;
				firstName = null;
				lastName = null;
				phoneNumber = null;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Error in displaying Customers\n" + e.getMessage() ,"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	void insertCustomer() {
		
		try {
			if(CustomerIdIdFel.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Please fill Customer id because it is required","Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			CustomerId = Integer.parseInt(CustomerIdIdFel.getText());
			firstName = CustomerFnameFel.getText();
			lastName = CustomerLnameFel.getText();
			phoneNumber = CustomerPhoneFel.getText();
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "one of the types is not compatible\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return;

		}
		String query = "INSERT INTO customer VALUES('" + CustomerId + "','"+ firstName + "','" + lastName + "','" + phoneNumber + "')"; 
		try {
			stmt.execute(query);
			JOptionPane.showMessageDialog(null, "Customer inserted successfully","FEEDBACK", JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Could not insert the Customer\n"+ e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
		}
		CustomerIdIdFel.setText("");
		CustomerFnameFel.setText("");
		CustomerLnameFel.setText("");
		CustomerPhoneFel.setText("");
	}
	
	
	void deleteCustomer() {
		
		try {
			if(CustomerIdIdFel.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Please specify the Customer who you want to delete","Warning", JOptionPane.WARNING_MESSAGE);
				return ;
			}
			CustomerId = Integer.parseInt(CustomerIdIdFel.getText());
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "one of the types is not compatible\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return;

		}
		String query = "DELETE FROM customer WHERE CustID =" + CustomerId;
		try {
			int a = stmt.executeUpdate(query);
			
			if(a >= 1) {
				JOptionPane.showMessageDialog(null, "Customer Deleted successfully","FEEDBACK", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, "Customer does not Exists","Warning", JOptionPane.WARNING_MESSAGE);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Could not delete Customer:\n" + e.getMessage() ,"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		
		mainCustomer = new JPanel();
		mainCustomer.setBounds(179, 0, 803, 598);
		mainCustomer.setLayout(null);
		
		JLabel CustomerIdLab = new JLabel("Customer id:");
		CustomerIdLab.setBounds(35, 67, 115, 35);
		CustomerIdLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainCustomer.add(CustomerIdLab);
		
		CustomerIdIdFel = new JTextField();
		CustomerIdIdFel.setBounds(146, 72, 200, 30);
		CustomerIdIdFel.setColumns(10);
		mainCustomer.add(CustomerIdIdFel);
		
		JLabel CustomerFnameLab = new JLabel("First name");
		CustomerFnameLab.setBounds(463, 67, 100, 35);
		CustomerFnameLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainCustomer.add(CustomerFnameLab);
		
		CustomerFnameFel = new JTextField();
		CustomerFnameFel.setBounds(569, 72, 200, 30);
		CustomerFnameFel.setColumns(10);
		mainCustomer.add(CustomerFnameFel);
		
		JLabel CustomerLnameLab = new JLabel("Last name:");
		CustomerLnameLab.setBounds(35, 136, 109, 35);
		CustomerLnameLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		mainCustomer.add(CustomerLnameLab);
		
		CustomerLnameFel = new JTextField();
		CustomerLnameFel.setBounds(146, 141, 200, 30);
		CustomerLnameFel.setColumns(10);
		mainCustomer.add(CustomerLnameFel);
		
		
		JLabel CustomerPhoneNumLab = new JLabel("Customer phone number:");
		CustomerPhoneNumLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		CustomerPhoneNumLab.setBounds(359, 141, 204, 35);
		mainCustomer.add(CustomerPhoneNumLab);
		
		CustomerPhoneFel = new JTextField();
		CustomerPhoneFel.setColumns(10);
		CustomerPhoneFel.setBounds(569, 141, 200, 30);
		mainCustomer.add(CustomerPhoneFel);
		
		
		
		
		
		
		TableCustomer = new JTable();
		TableCustomer.setBackground(new Color(255, 255, 255));
		TableCustomer.setRowHeight(23);
		TableCustomer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		TableCustomer.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Customer id", "First name", "Last name","phone number"
			}
		){
			 @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
		});
		TableCustomer.getColumnModel().getColumn(0).setPreferredWidth(100);
		TableCustomer.getColumnModel().getColumn(1).setPreferredWidth(100);
		TableCustomer.getColumnModel().getColumn(2).setPreferredWidth(100);
		TableCustomer.getColumnModel().getColumn(3).setPreferredWidth(100);
		TableCustomer.setBounds(10, 275, 790, 323);
		TableCustomer.getTableHeader().setBackground(Color.LIGHT_GRAY);
		JScrollPane TableCustomerScroll = new JScrollPane(TableCustomer);
		TableCustomerScroll.setBounds(10, 275, 790, 323);
		mainCustomer.add(TableCustomerScroll);
		
		JButton CustomerInsertBtn = new JButton("insert");
		CustomerInsertBtn.setBounds(421, 197, 89, 42);
		CustomerInsertBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		CustomerInsertBtn.setBackground(Color.GREEN);
		CustomerInsertBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				insertCustomer();
			}
		});
		mainCustomer.add(CustomerInsertBtn);
		
		JButton CustomerDisplayBtn = new JButton("display");
		CustomerDisplayBtn.setBounds(537, 197, 89, 42);
		CustomerDisplayBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		CustomerDisplayBtn.setBackground(new Color(0, 128, 255));
		
		CustomerDisplayBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displayCustomer();
			}
		});
		mainCustomer.add(CustomerDisplayBtn);
		
		JButton CustomerbtnDelete = new JButton("delete");
		CustomerbtnDelete.setBounds(649, 197, 89, 42);
		CustomerbtnDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		CustomerbtnDelete.setBackground(Color.RED);
		
		CustomerbtnDelete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				deleteCustomer();
			}
		});
		
		mainCustomer.add(CustomerbtnDelete);
		
		
		
		
		
		mainCustomer.setVisible(true);
		displayCustomer();

	}

}
