package pet_store;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

public class Branch implements MouseListener{
	
	// entity variables
	int branchId;
	String location;
	int buildingNum;
	
	// connections
	
	static Connection conn;
	static Statement stmt;
	
	
	
	
	
	// end entity variables
	
	private JFrame frame;
	private JPanel sideBar;
	private JPanel mainBranch;
	private JPanel currentPanel;
	
	
	private JButton BranchBtn;
	private JButton PetBtn;
	private JButton ClinicBtn;
	private JButton CustomerBtn;
	private JButton PurchasesBtn;
	private JButton AppointmentBtn;
	JTextField BranchIdFel;
	JTextField LocationFel;
	JTextField BuildingNumFel;
	JTable TableBranch;
	
	
	
	

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Branch window = new Branch();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Branch() {
		startConnection();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void startConnection() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3301/pet_store", "root", "1111");
			stmt = conn.createStatement();
			
		}catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "database connection failed\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	void displayBranch() {
		String query = "SELECT * FROM branch";
		DefaultTableModel model = (DefaultTableModel) TableBranch.getModel();
		
		model.setRowCount(0);
		try {
			ResultSet res = stmt.executeQuery(query);
			while(res.next()) {
				branchId = res.getInt(1);
				location = res.getString(2);
				buildingNum = res.getInt(3);
				model.addRow(new Object[]{branchId,location,buildingNum});
				branchId = 0;
				location = null;
				buildingNum = 0;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Error in displaying branches\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	void insertBranch() {
		
		try {
			if(BranchIdFel.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Please fill Branch id because it is required","Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			branchId = Integer.parseInt(BranchIdFel.getText());
			location = LocationFel.getText();
			
			if(BuildingNumFel.getText().equals("")) {
				buildingNum = 0;
			}else {
				buildingNum = Integer.parseInt(BuildingNumFel.getText());	
			}
			
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "one of the types is not compatible\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String query = "INSERT INTO branch VALUES('" + branchId + "','"+ location + "','" + buildingNum+"')"; 
		try {
			stmt.execute(query);
			JOptionPane.showMessageDialog(null, "Branch inserted successfully","FEEDBACK", JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Could not insert the branch\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
		}
		BranchIdFel.setText("");
		LocationFel.setText("");
		BuildingNumFel.setText("");
	}
	
	void deleteBranch() {
		
		try {
			if(BranchIdFel.getText().equals("")) {
				JOptionPane.showMessageDialog(null, "Please specify the branch which you want to delete","Warning", JOptionPane.WARNING_MESSAGE);
				return ;
			}
			branchId = Integer.parseInt( BranchIdFel.getText());
		}catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "one of the types is not compatible\n" + e.getMessage(),"ERROR", JOptionPane.ERROR_MESSAGE);
			return;
		}
		String query = "DELETE FROM branch WHERE Bid =" + branchId;
		try {
			int a = stmt.executeUpdate(query);
			
			if(a >= 1) {
				JOptionPane.showMessageDialog(null, "Branch Deleted successfully","FEEDBACK", JOptionPane.INFORMATION_MESSAGE);
			}else {
				JOptionPane.showMessageDialog(null, "Branch does not Exists","Warning", JOptionPane.WARNING_MESSAGE);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Could not delete branch:\n" + e.getMessage() ,"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	
	
	}
	
	
	
	
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(70, 10, 996, 635);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// create side bar
		createSideBar();
		
		// create main branch
		createMainBranch();			
		displayBranch();

	}
	@SuppressWarnings("serial")
	private void createMainBranch() {
		
		mainBranch = new JPanel();
		mainBranch.setBounds(179, 0, 803, 598);
		frame.getContentPane().add(mainBranch);
		mainBranch.setLayout(null);
		
		currentPanel = mainBranch;
		
		JLabel BranchIdLab = new JLabel("branch id:");
		BranchIdLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		BranchIdLab.setBounds(66, 67, 85, 35);
		mainBranch.add(BranchIdLab);
		
		BranchIdFel = new JTextField();
		BranchIdFel.setBounds(160, 72, 200, 30);
		mainBranch.add(BranchIdFel);
		BranchIdFel.setColumns(10);
		
		JLabel LocationLab = new JLabel("location:");
		LocationLab.setFont(new Font("Tahoma", Font.PLAIN, 18));
		LocationLab.setBounds(389, 67, 85, 35);
		mainBranch.add(LocationLab);
		
		LocationFel = new JTextField();
		LocationFel.setColumns(10);
		LocationFel.setBounds(480, 72, 200, 30);
		mainBranch.add(LocationFel);
		
		JLabel BuildingNumberFel = new JLabel("building number:");
		BuildingNumberFel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		BuildingNumberFel.setBounds(10, 136, 141, 35);
		mainBranch.add(BuildingNumberFel);
		
		BuildingNumFel = new JTextField();
		BuildingNumFel.setColumns(10);
		BuildingNumFel.setBounds(160, 141, 200, 30);
		mainBranch.add(BuildingNumFel);
		
		
		
		TableBranch = new JTable();
		TableBranch.setRowHeight(23);
		TableBranch.setFont(new Font("Tahoma", Font.PLAIN, 15));
		TableBranch.setBackground(new Color(255, 255, 255));
		TableBranch.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Branch id", "Location", "Building number"
			}
		){
			 @Override
			    public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
		});
		TableBranch.getColumnModel().getColumn(0).setPreferredWidth(100);
		TableBranch.getColumnModel().getColumn(1).setPreferredWidth(100);
		TableBranch.getColumnModel().getColumn(2).setPreferredWidth(100);
		TableBranch.setBounds(10, 275, 790, 323);
		TableBranch.getTableHeader().setBackground(Color.LIGHT_GRAY);
		
		JScrollPane TableBranchScroll = new JScrollPane(TableBranch);
		TableBranchScroll.setBounds(10, 275, 790, 323);
		mainBranch.add(TableBranchScroll);
		
		JButton BranchInsertBtn = new JButton("insert");
		BranchInsertBtn.setBackground(new Color(0, 255, 0));
		BranchInsertBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		BranchInsertBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertBranch();
			}
		});
		BranchInsertBtn.setBounds(421, 197, 89, 42);
		mainBranch.add(BranchInsertBtn);
		
		JButton BranchDisplayBtn = new JButton("display");
		BranchDisplayBtn.setBackground(new Color(0, 128, 255));
		BranchDisplayBtn.setFont(new Font("Tahoma", Font.PLAIN, 15));
		BranchDisplayBtn.setBounds(537, 197, 89, 42);
		
		BranchDisplayBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayBranch();
			}
		});
		mainBranch.add(BranchDisplayBtn);
		
		JButton btnDelete = new JButton("delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteBranch();
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDelete.setBackground(new Color(255, 0, 0));
		btnDelete.setBounds(649, 197, 89, 42);
		mainBranch.add(btnDelete);
		
		displayBranch();
		
//		TableBranch.addMouseListener(new MouseListener() {
//			
//			@Override
//			public void mouseReleased(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void mousePressed(MouseEvent e) {
//				// TODO Auto-generated method stub
//				if(TableBranch.equals(e.getSource())) {
//	                int rowIdx = TableBranch.rowAtPoint(e.getPoint());    
//	                BranchIdFel.setText( (String) TableBranch.getValueAt(rowIdx,0));
//	                LocationFel.setText((String) TableBranch.getValueAt(rowIdx,1));
//	                BuildingNumFel.setText((String) TableBranch.getValueAt(rowIdx,2));
//				}
//			}
//			
//			@Override
//			public void mouseExited(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void mouseEntered(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		
		
		
	}
	private void createSideBar() {
		// sidebar start
		sideBar = new JPanel();
		sideBar.setBackground(new Color(0, 128, 255));
		sideBar.setBounds(0, 0, 183, 598);
		frame.getContentPane().add(sideBar);
		sideBar.setLayout(null);
		
		JTextPane txtpnPetStore = new JTextPane();
		txtpnPetStore.setFont(new Font("Tahoma", Font.BOLD, 23));
		txtpnPetStore.setForeground(new Color(255, 255, 255));
		txtpnPetStore.setBackground(new Color(0, 128, 255));
		txtpnPetStore.setEditable(false);
		txtpnPetStore.setBounds(36, 11, 126, 32);
		txtpnPetStore.setText("Pet Store");
		sideBar.add(txtpnPetStore);
		
		JPanel TtileLine = new JPanel();
		TtileLine.setBounds(0, 54, 183, 10);
		TtileLine.setLayout(null);
		sideBar.add(TtileLine);
		

		JPanel InnerSideBar = new JPanel();
		InnerSideBar.setBounds(0, 127, 183, 471);
		InnerSideBar.setBackground(new Color(0, 128, 255));
		GridLayout InnerSideBarlayout = new GridLayout(6,1);
		InnerSideBarlayout.setVgap(15);
		InnerSideBar.setLayout(InnerSideBarlayout);
		
		
		BranchBtn = new JButton("Branch");
		BranchBtn.setBounds(0, 130, 183, 45);
		InnerSideBar.add(BranchBtn);
		BranchBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		BranchBtn.setForeground(new Color(255, 255, 255));
		BranchBtn.setBackground(new Color(0, 128, 255));
		BranchBtn.setBorder(BorderFactory.createLineBorder(Color.white,2));
		
		BranchBtn.addMouseListener(this);
		
		BranchBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				frame.getContentPane().remove(1);
				frame.getContentPane().add(mainBranch);
				frame.validate();
				frame.repaint();
			}
		});
		
		PetBtn = new JButton("Pet");
		PetBtn.setBorder(BorderFactory.createLineBorder(Color.white,2));
		PetBtn.setForeground(Color.WHITE);
		PetBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		PetBtn.setBackground(new Color(0, 128, 255));
		PetBtn.setBounds(0, 199, 183, 45);
		
		PetBtn.addMouseListener(this);
		PetBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pet p = new Pet();
				frame.getContentPane().remove(1);
				frame.getContentPane().add(p.mainPet);
				frame.validate();
				frame.repaint();
			}
		});
		
		InnerSideBar.add(PetBtn);
		
		ClinicBtn = new JButton("Clinic");
		ClinicBtn.setBorder(BorderFactory.createLineBorder(Color.white,2));
		ClinicBtn.setForeground(Color.WHITE);
		ClinicBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		ClinicBtn.setBackground(new Color(0, 128, 255));
		ClinicBtn.setBounds(0, 268, 183, 45);
		ClinicBtn.addMouseListener(this);
		
		ClinicBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clinic p = new Clinic();
				frame.getContentPane().remove(1);
				frame.getContentPane().add(p.mainClinic);
				frame.validate();
				frame.repaint();
			}
		});
		InnerSideBar.add(ClinicBtn);
		
		CustomerBtn = new JButton("Customer");
		CustomerBtn.setBorder(BorderFactory.createLineBorder(Color.white,2));
		CustomerBtn.setForeground(Color.WHITE);
		CustomerBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		CustomerBtn.setBackground(new Color(0, 128, 255));
		CustomerBtn.setBounds(0, 337, 183, 45);
		CustomerBtn.addMouseListener(this);
		
		CustomerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Customer p = new Customer();
				frame.getContentPane().remove(1);
				frame.getContentPane().add(p.mainCustomer);
				frame.validate();
				frame.repaint();
			}
		});
		InnerSideBar.add(CustomerBtn);
		
		PurchasesBtn = new JButton("Purchases");
		PurchasesBtn.setBorder(BorderFactory.createLineBorder(Color.white,2));
		PurchasesBtn.setForeground(Color.WHITE);
		PurchasesBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		PurchasesBtn.setBackground(new Color(0, 128, 255));
		PurchasesBtn.setBounds(0, 409, 183, 45);
		PurchasesBtn.addMouseListener(this);
		
		PurchasesBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Purchase p = new Purchase();
				frame.getContentPane().remove(1);
				frame.getContentPane().add(p.mainPurchase);
				frame.validate();
				frame.repaint();
			}
		});
		InnerSideBar.add(PurchasesBtn);
		
		AppointmentBtn = new JButton("Appoitment");
		AppointmentBtn.setBorder(BorderFactory.createLineBorder(Color.white,2));
		AppointmentBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		AppointmentBtn.setForeground(Color.WHITE);
		AppointmentBtn.setFont(new Font("Tahoma", Font.PLAIN, 20));
		AppointmentBtn.setBackground(new Color(0, 128, 255));
		AppointmentBtn.setBounds(0, 477, 183, 45);
		AppointmentBtn.addMouseListener(this);
		AppointmentBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Appointment p = new Appointment();
				frame.getContentPane().remove(1);
				frame.getContentPane().add(p.mainAppointment);
				frame.validate();
				frame.repaint();
			}
		});
		InnerSideBar.add(AppointmentBtn);
		sideBar.add(InnerSideBar);	
		// sidebar end
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == BranchBtn) {
			BranchBtn.setBackground(Color.black);
		}else if(e.getSource() == PetBtn) {
			PetBtn.setBackground(Color.black);

		}else if(e.getSource() == ClinicBtn) {
			ClinicBtn.setBackground(Color.black);

		}else if(e.getSource() == CustomerBtn) {
			CustomerBtn.setBackground(Color.black);

		}else if(e.getSource() == PurchasesBtn) {
			PurchasesBtn.setBackground(Color.black);

		}else if(e.getSource() == AppointmentBtn) {
			AppointmentBtn.setBackground(Color.black);

		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == BranchBtn) {
			BranchBtn.setBackground(new Color(0, 128, 255));
		}else if(e.getSource() == PetBtn) {
			PetBtn.setBackground(new Color(0, 128, 255));
		}else if(e.getSource() == ClinicBtn) {
			ClinicBtn.setBackground(new Color(0, 128, 255));

		}else if(e.getSource() == CustomerBtn) {
			CustomerBtn.setBackground(new Color(0, 128, 255));

		}else if(e.getSource() == PurchasesBtn) {
			PurchasesBtn.setBackground(new Color(0, 128, 255));

		}else if(e.getSource() == AppointmentBtn) {
			AppointmentBtn.setBackground(new Color(0, 128, 255));
		}
		
	}
}
