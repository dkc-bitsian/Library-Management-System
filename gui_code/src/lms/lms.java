package lms;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class lms {

	private JFrame frame;
	private JTextField Isbn_textField;
	private JTextField author_textField;
	private JTextField title_textField;
	DefaultTableModel model ;
	DefaultTableModel model_1 ;
	DefaultTableModel model_2 ;

	private JTable table;
	private JTextField isbn_checkout_textField;
	private JTextField branch_id_checkout_textField;
	private JTextField card_no_checkout_textField;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	Object val ;
	
	private JTable table_1;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTable table_2;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					lms window = new lms();
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
	public lms() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1008, 707);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 13, 978, 647);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("BOOK SEARCH", null, panel, null);
		panel.setLayout(null);
		
		Isbn_textField = new JTextField();
		Isbn_textField.setBounds(41, 13, 116, 22);
		panel.add(Isbn_textField);
		Isbn_textField.setColumns(10);
		
		author_textField = new JTextField();
		author_textField.setBounds(201, 13, 116, 22);
		panel.add(author_textField);
		author_textField.setColumns(10);
		
		title_textField = new JTextField();
		title_textField.setBounds(374, 13, 116, 22);
		panel.add(title_textField);
		title_textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Submit");
		btnNewButton.setBounds(594, 12, 97, 25);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			
			{
				String title=title_textField.getText();
				String author=author_textField.getText();
				String isbn=Isbn_textField.getText();
				
				
				try {
					Connection myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/library?autoReconnect=true&useSSL=false","root","krishna");
					System.out.println ("Database connection established");
					
										
					String query="select Isbn , Title, Authro,branch_id as bid,branch_name,no_of_copies from ( select book_id,a.branch_id,no_of_copies,branch_name from (SELECT * FROM book_copies where book_id like'%"+isbn+"%') as a left join library_branch as b on b.branch_id=a.branch_id) as bb left join authors as aa on aa.Isbn=bb.book_id where aa.Isbn like '%"+isbn+"%' and aa.title like '%"+title+"%' and aa.authro like '%"+author+"%' order by Isbn;";
					Statement stat_new = myconn.createStatement();

					ResultSet result_new=stat_new.executeQuery(query);
					
					while(model.getRowCount() > 0)
					{
					    model.removeRow(0);
					}
					while ( result_new.next() ) 
					{
						String book_id= result_new.getString("Isbn");
						
						int branch_id= result_new.getInt("bid");
						
						int no_of_copies= result_new.getInt("no_of_copies");
						
						int available_copies=no_of_copies;
						int copies_loaned=0 ;
						
						String query1= "select count(*)as a from book_loans where branch_id="+branch_id+" and Isbn='"+book_id+"';";
						Statement ssst = myconn.createStatement();
						ResultSet result1=ssst.executeQuery(query1);
						while ( result1.next() ) 
						{
							copies_loaned=result1.getInt("a");
						}
						 
						available_copies=available_copies-copies_loaned;
						//System.out.println(available_copies);
						
						
						String[] row ={result_new.getString("Isbn"),result_new.getString("Title"),result_new.getString("Authro"),result_new.getString("bid"),result_new.getString("branch_name"),result_new.getString("no_of_copies"),Integer.toString(available_copies)};
						model.addRow(row);
						
					}
		            myconn.close();
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
		});
		panel.add(btnNewButton);
		
		//model = new DefaultTableModel();
        //model.addColumn("ISBN");
        //model.addColumn("Book Title");
        //model.addColumn("Book Author(s)");
        //model.addColumn("Branch Id");
        //model.addColumn("Branch Name");

        //model.addColumn("Copies owned");
        //model.addColumn("Copies available");
		
		JLabel lblIsbn = new JLabel("Isbn");
		lblIsbn.setBounds(70, 48, 56, 16);
		panel.add(lblIsbn);
		
		JLabel lblAuthor = new JLabel("Author");
		lblAuthor.setBounds(230, 48, 56, 16);
		panel.add(lblAuthor);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setBounds(416, 46, 56, 16);
		panel.add(lblTitle);
		
		JScrollPane scrollPane = new JScrollPane();
		
		scrollPane.setBounds(0, 79, 973, 538);
		panel.add(scrollPane);
		
		model=new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"ISBN", "Book Title", "Book author(s)", "branch_id", "branch_name", "No_of_copies","available_copies"
				}
			);
		table = new JTable();
		table.setModel(model);
		scrollPane.setViewportView(table);
		
		
		
		
		JButton button = new JButton("New button");
		scrollPane.setColumnHeaderView(button);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("New check box");
		scrollPane.setColumnHeaderView(chckbxNewCheckBox);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("CHECK OUT", null, panel_1, null);
		panel_1.setLayout(null);
		
		isbn_checkout_textField = new JTextField();
		isbn_checkout_textField.setBounds(28, 40, 116, 22);
		panel_1.add(isbn_checkout_textField);
		isbn_checkout_textField.setColumns(10);
		
		branch_id_checkout_textField = new JTextField();
		branch_id_checkout_textField.setBounds(424, 40, 116, 22);
		panel_1.add(branch_id_checkout_textField);
		branch_id_checkout_textField.setColumns(10);
		
		card_no_checkout_textField = new JTextField();
		card_no_checkout_textField.setBounds(785, 40, 116, 22);
		panel_1.add(card_no_checkout_textField);
		card_no_checkout_textField.setColumns(10);
		
		JLabel lblIsbn_1 = new JLabel("ISBN");
		lblIsbn_1.setBounds(38, 75, 56, 16);
		panel_1.add(lblIsbn_1);
		
		JLabel lblBranchId = new JLabel("Branch Id");
		lblBranchId.setBounds(434, 75, 56, 16);
		panel_1.add(lblBranchId);
		
		JLabel lblBorrowerCardNo = new JLabel("Borrower Card No");
		lblBorrowerCardNo.setBounds(795, 75, 116, 16);
		panel_1.add(lblBorrowerCardNo);
		
		JButton btnCheckOut = new JButton("Check Out");
		btnCheckOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				String Isbn=isbn_checkout_textField.getText();
				String branch_id=branch_id_checkout_textField.getText();
				String Card_no=card_no_checkout_textField.getText();
				
				try {
					Connection myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/library?autoReconnect=true&useSSL=false","root","krishna");
					System.out.println ("Database connection established");
					
					
					Statement mystat = myconn.createStatement();
					
					String query = "SELECT COUNT(Loan_id) FROM BOOK_LOANS WHERE Card_no='" + Card_no +"';";
					int person_loans=0 ;
					
					ResultSet result=mystat.executeQuery(query);
					while ( result.next()) {
						
						person_loans=Integer.parseInt(result.getString("COUNT(Loan_id)"));	
						//System.out.println(person_loans);
		            }
					String query2= "SELECT No_of_copies FROM BOOK_COPIES WHERE Book_id="
							+ Isbn
							+ " AND Branch_id="
							+ branch_id +";" ; 
					ResultSet result1=mystat.executeQuery(query2);
					int no_of_copies=0;
					while(result1.next())
					{
						no_of_copies= Integer.parseInt(result1.getString("No_of_copies"));
						//System.out.println(no_of_copies);

					}
					
					String query3= "SELECT COUNT(Loan_id) FROM BOOK_LOANS WHERE Isbn="
							+ Isbn
							+ " AND Branch_id="
							+ branch_id ; 
					ResultSet result2=mystat.executeQuery(query3);
					int book_loans=0;
					while ( result2.next() ) {
						
						book_loans=Integer.parseInt(result2.getString("COUNT(Loan_id)"));	
						//System.out.println(book_loans);

		            }
					
					String quuery3= "SELECT MAX(Loan_id) FROM BOOK_LOANS"; 
					ResultSet rresult2=mystat.executeQuery(quuery3);
					int max_id=0;
					while ( rresult2.next() ) {
						
						max_id=Integer.parseInt(rresult2.getString("MAX(Loan_id)"));	
						System.out.println(max_id);
		            }
					
					
					 if(person_loans>=3 || no_of_copies==book_loans)
					 {
						 JOptionPane.showMessageDialog(null,"Cant Check_out since the number of personal loans have exceeded three or there are no more copies available");
						 myconn.close();
					 }
					 else
					 {
						 
						 //loan id unique
						 int Loan_id= max_id+1 ;
						
						 
						 String query4 ="INSERT INTO BOOK_LOANS  VALUES ('"
						 		+ Loan_id
						 		+ "' ,'"
						 		+ Isbn
						 		+ "','"
						 		+ branch_id
						 		+ "','"
						 		+ Card_no
						 		+ "',curdate(),adddate(curdate(),14),NULL);";
						 
						 mystat.executeUpdate(query4);
								 
						 
					 }
						 
					
		            myconn.close();
					
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					
					
					JOptionPane.showMessageDialog(null,"ERROR ! ALL fields are mandatary and borrower card number is a numeric input and not an string");
					 
					
					e.printStackTrace();
				}
				
				
				
			}
		});
		btnCheckOut.setBounds(424, 131, 97, 25);
		panel_1.add(btnCheckOut);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("CHECK IN", null, panel_2, null);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		
		textField_5 = new JTextField();
		textField_5.setColumns(10);
		
		JButton btnSearchBook = new JButton("Search book");
		btnSearchBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String bookID = textField_3.getText();
				String cardNumber=textField_4.getText();
				String b_name=textField_5.getText();
				
				
				try {
					Connection myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/library?autoReconnect=true&useSSL=false","root","krishna");
					System.out.println ("Database connection established");
					
					
					Statement mystat = myconn.createStatement();
					
					String query ="select Loan_id,Isbn,Branch_id,Card_no,fname,lname,Date_out,Due_date,Date_in from book_loans c natural join borrowers b where c.Card_no=b.Card_no and Isbn like '%"+bookID+"%' AND c.Card_no like '%"+cardNumber+"%' and (fname like '%"+b_name+"%' or lname like '%"+b_name+"%');"; 
					
					ResultSet result=mystat.executeQuery(query);
					while(model_1.getRowCount() > 0)
					{
					    model_1.removeRow(0);
					}
					while ( result.next() ) 
					{
						String Loan_id=result.getString("Loan_id");
						String Isbn=result.getString("Isbn");
						String Branch_id=result.getString("Branch_id");
						String Card_no=result.getString("Card_no");
						String fname=result.getString("fname");
						String lname=result.getString("lname");
						String Date_out=result.getString("Date_out");
						String Due_date=result.getString("Due_date");
						String Date_in=result.getString("Date_in");
						
						
						
						String[] row={Loan_id,Isbn,Branch_id,Card_no,fname,lname,Date_out,Due_date,Date_in};
		            	model_1.addRow(row);
		                
		            }
		            myconn.close();
					
					
				} catch (SQLException eee1) {
					// TODO Auto-generated catch block
					eee1.printStackTrace();
				}
				
				
				
				
			}
		});
		

		
		model_1=new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Loan_id", "Book_id", "Branch_id", "Card_id", "fname", "lname","Date_out","Due_date","Date_in"
				}
			);
		
		table_1 = new JTable();
		table_1.setModel(model_1);
		
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int row = table_1.rowAtPoint(e.getPoint());
		        int col = table_1.columnAtPoint(e.getPoint());
		        if (row >= 0 && col >= 0)
		        {
		        	val=table_1.getModel().getValueAt(row,0);
		        }
		   
				
			}
		});
		
		
		JLabel lblBookId = new JLabel("Book Id");
		
		JLabel lblCardNo = new JLabel("Card No");
		
		JLabel lblBorrowerName = new JLabel("Borrower Name");
		
		
		
		JButton btnSelectRowFor = new JButton("Select Row for CheckIn");
		btnSelectRowFor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try
				{
					Connection myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/library?autoReconnect=true&useSSL=false","root","krishna");
					System.out.println ("Database connection established");
					System.out.println(val);
					
					String query ="UPDATE book_loans SET Date_in =curdate() where Loan_id ="+ Integer.parseInt((String) val) + "; "; 
					
	
		             java.sql.PreparedStatement update = myconn.prepareStatement(query);		          		             
		             //Statement stmt = myconn.createStatement();
		             update.executeUpdate();
					
		            myconn.close();
					
					
					
					
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
			}
		});
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(27)
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addComponent(textField_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_2.createSequentialGroup()
									.addGap(10)
									.addComponent(lblBookId, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)))
							.addGap(101)
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addComponent(textField_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_2.createSequentialGroup()
									.addGap(10)
									.addComponent(lblCardNo, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)))
							.addGap(127)
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addComponent(textField_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_2.createSequentialGroup()
									.addGap(10)
									.addComponent(lblBorrowerName, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)))
							.addGap(159)
							.addComponent(btnSearchBook, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(382)
							.addComponent(btnSelectRowFor, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 957, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(26)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(textField_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(13)
							.addComponent(lblBookId))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(textField_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(13)
							.addComponent(lblCardNo))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(textField_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(13)
							.addComponent(lblBorrowerName))
						.addComponent(btnSearchBook, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 464, GroupLayout.PREFERRED_SIZE)
					.addGap(3)
					.addComponent(btnSelectRowFor, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
		);
		
		scrollPane_1.setViewportView(table_1);
		panel_2.setLayout(gl_panel_2);
		
		
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("NEW BORROWER", null, panel_3, null);
		panel_3.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(34, 35, 116, 22);
		panel_3.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(204, 35, 116, 22);
		panel_3.add(textField_1);
		textField_1.setColumns(10);
		
		textField_2 = new JTextField();
		textField_2.setBounds(380, 35, 116, 22);
		panel_3.add(textField_2);
		textField_2.setColumns(10);
		
		textField_6 = new JTextField();
		textField_6.setBounds(563, 35, 116, 22);
		panel_3.add(textField_6);
		textField_6.setColumns(10);
		
		textField_7 = new JTextField();
		textField_7.setBounds(753, 35, 116, 22);
		panel_3.add(textField_7);
		textField_7.setColumns(10);
		
		JLabel lblSsn = new JLabel("Ssn(mandatary)");
		lblSsn.setBounds(44, 70, 106, 16);
		panel_3.add(lblSsn);
		
		JLabel lblFirstName = new JLabel("first name(mandatary)");
		lblFirstName.setBounds(204, 70, 142, 16);
		panel_3.add(lblFirstName);
		
		JLabel lblLastName = new JLabel("last name(mandatary)");
		lblLastName.setBounds(375, 70, 127, 16);
		panel_3.add(lblLastName);
		
		JLabel lblAddress = new JLabel("address(mandatary)");
		lblAddress.setBounds(573, 70, 116, 16);
		panel_3.add(lblAddress);
		
		JLabel lblPhone = new JLabel("phone");
		lblPhone.setBounds(763, 70, 56, 16);
		panel_3.add(lblPhone);
		
		JButton btnNewButton_1 = new JButton("Add Borrower");
		
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String ssn=textField.getText();
				String fname=textField_1.getText();
				String lname=textField_2.getText();
				String address=textField_6.getText();
				String phone=textField_7.getText();
				
				System.out.println(ssn);
				if(ssn.isEmpty() || fname.isEmpty() ||lname.isEmpty()|| address.isEmpty())
				{
					System.out.println("aaa");
					JOptionPane.showMessageDialog(null,"ERROR ! Fields which are mandatary cant be left blank");
					return ;
				}

				
				int tot=0;

				try {
					
					
					
					
					Connection myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/library?autoReconnect=true&useSSL=false","root","krishna");
					System.out.println ("Database connection established");
					Statement mystat = myconn.createStatement();
					
					String query = "SELECT count(card_no) as tot FROM borrowers WHERE fname='"+fname+"' AND lname='"+lname+"' AND address='"+address+"' AND ssn='"+ssn+"';";

					ResultSet result = mystat.executeQuery(query);
					while (result.next()) {
						tot = result.getInt("tot");
						System.out.println(tot);
					}
					if(tot > 0){
						JOptionPane.showMessageDialog(null,"New Borrower cant be added because Borrower already exists");
						myconn.close();
						
					}
					
					else
					{
						
						query=" SELECT MAX(card_no) FROM borrowers;";
						
						result=mystat.executeQuery(query);
						
						int Card_no=0;
						
						while(result.next()){
							
							Card_no=Integer.parseInt(result.getString("Max(card_no)"));
						}

						Card_no=Card_no+1;
						String query1 ="INSERT INTO borrowers VALUES('"
								+ Card_no
								+ "','"
								+ ssn
								+ "','"
								+ fname
								+ "','"
								+ lname
								+ "','"
								+ address
								+ "','"
								+ phone
								+ "');";
								
						mystat.executeUpdate(query1);
						
			             myconn.close();         
			            
						
					}
					
										
				} catch (SQLException ee) {
					// TODO Auto-generated catch block
					
					JOptionPane.showMessageDialog(null,"ERROR ! Fields which are mandatary cant be left blank");
					 
					
					
					ee.printStackTrace();
				}
				
				
				
				
			}
		});
		frame.getContentPane().setLayout(null);
		btnNewButton_1.setBounds(369, 173, 150, 49);
		panel_3.add(btnNewButton_1);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("FINES", null, panel_4, null);
		
		JButton btnRefreshFines = new JButton("Refresh Fines");
		btnRefreshFines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				
				int Loan_id ;
				Float fine ;
				try 
				{	
					Connection myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/library?autoReconnect=true&useSSL=false","root","krishna");
					System.out.println ("Database connection established");
					Statement mystat = myconn.createStatement();
					
					String query = "select Loan_id, datediff(current_date(),Due_date) *0.25 as fine from book_loans where date_in IS null AND datediff(current_date(),Due_date)>0;";
					
					ResultSet result = mystat.executeQuery(query);
					
					while (result.next()) 
					{
						Loan_id= result.getInt("Loan_id");
						fine = result.getFloat("fine");
						
						String fine_query ="select Loan_id from fines where paid=false and loan_id="+Loan_id+";";
						
						mystat = myconn.createStatement();
						ResultSet rst=mystat.executeQuery(fine_query);
						
						//update record if rows exist having paid as false corresponding to the loan_id
						if(rst.isBeforeFirst())
						{
							
							
							String update_query="update fines set fine_amt="+ fine +"where Loan_id="+ Loan_id+" ;";
							
							Statement st = myconn.createStatement();
							
							st.executeUpdate(update_query);
							
							
						}
						
						// update records
						
						fine_query="select Loan_id from fines where loan_id="+Loan_id+";";
						

						ResultSet rrres=mystat.executeQuery(fine_query);
						
						//create new record if no records exist for that loan id
						 if(!rrres.isBeforeFirst())
						{
							
							String insert_query ="insert into fines values("+Loan_id+","+ fine +",'false');";
							
							Statement st = myconn.createStatement();
							
							st.executeUpdate(insert_query);

							
						}
						
						
					}	
					
					
					
					query = "select Loan_id, datediff(Date_in,Due_date)*0.25 as fine from book_loans where date_in IS not null AND datediff(date_in,Due_date)>0;";
					
					result = mystat.executeQuery(query);
					
					while (result.next()) 
					{
						Loan_id= result.getInt("Loan_id");
						fine = result.getFloat("fine");
						//query to find an existing query with false as paid
						String fine_query ="select Loan_id from fines where paid='false' and loan_id="+Loan_id+";";
						
						mystat = myconn.createStatement();
						ResultSet rst=mystat.executeQuery(fine_query);
						
						//update record if rows exist having paid as false corresponding to the loan_id
						if(rst.isBeforeFirst())
						{
							

							//System.out.println("updating");
							String update_query="update fines set fine_amt="+ fine +"where Loan_id="+ Loan_id+" ;";
							Statement st = myconn.createStatement();
							
							st.executeUpdate(update_query);
							
							
						}
						
						//query to find any existing records
						fine_query="select Loan_id from fines where loan_id="+Loan_id+";";
						

						ResultSet rrres=mystat.executeQuery(fine_query);
						
						//create new record if no records exist for that loan id
						 if(!rrres.isBeforeFirst())
						{
							//System.out.println("inserting");
							String insert_query ="insert into fines values("+Loan_id+","+ fine +",'false');";
							
							Statement st = myconn.createStatement();
							
							st.executeUpdate(insert_query);

							
						}
						
					}
					
					
					JOptionPane.showMessageDialog(null,"Fines are calculated and updated");
					
					myconn.close();
						
					       
			            
						
				}
					
										
				 catch (SQLException ee) {
					// TODO Auto-generated catch block
					
					ee.printStackTrace();
				}
				
				
				
				
			}
		});
		

		textField_8 = new JTextField();
		textField_8.setColumns(10);
		
		textField_9 = new JTextField();
		textField_9.setColumns(10);
		
		JLabel lblEn = new JLabel("Enter the Loan_id of fine");
		
		JLabel lblEnterTheFine = new JLabel("enter the fine amount");
		
		
		JButton btnUpdateFine = new JButton("Update Fine");
		btnUpdateFine.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(textField_8.getText().isEmpty() || textField_9.getText().isEmpty())
				{
					JOptionPane.showMessageDialog(null,"All fields are mandatary and must be numeric to update a fine");
					return ;
				}
				int Loan_id=Integer.parseInt(textField_8.getText());
				float fine=Float.parseFloat(textField_9.getText());
				
				
				
				try 
				{	
					Connection myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/library?autoReconnect=true&useSSL=false","root","krishna");
					System.out.println ("Database connection established");
					Statement mystat = myconn.createStatement();
					
					String queryzz= "select count(*) as c from book_loans where Loan_id="+Loan_id + ";";
					
					
					
					
					String book_loans_query="Select datediff(current_date(),Due_date) as diff ,datediff(Due_date,Date_in) as ch  from book_loans where Loan_id="+Loan_id+" AND date_in is NOT NULL";
					

					ResultSet resultzz = mystat.executeQuery(queryzz);
					
					while (resultzz.next()) 
					{
						if(resultzz.getInt("c")==0)
						{
							JOptionPane.showMessageDialog(null,"no such loan_id exists");
							return ;
						}
					}
					
					ResultSet result = mystat.executeQuery(book_loans_query);
					
					while (result.next()) 
					{
						if(result.getInt("ch")==0)
						{
							JOptionPane.showMessageDialog(null,"book is already checked in before the due date");
							return ;
						}
						
						
						if(result.getInt("diff")>0)
						{
							
							String query = "update fines set fine_amt="+ fine +",paid='true' where Loan_id="+ Loan_id+" ;";
							Statement stat = myconn.createStatement();

							stat.executeUpdate(query);
							
							
						}
						
						else
						{
							JOptionPane.showMessageDialog(null,"cant update fine because either the book is not yet returned or the current date is less than due date");
							myconn.close();
						}
						
					}
					
					
										
					myconn.close();
					    
						
				}
					
										
				 catch (SQLException ee) {
					// TODO Auto-generated catch block
					
					ee.printStackTrace();
				}
				
			
			}
		});
		
		JButton btnDisplayFines = new JButton("Display All Fines");
		btnDisplayFines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{	
					Connection myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/library?autoReconnect=true&useSSL=false","root","krishna");
					System.out.println ("Database connection established");
					Statement mystat = myconn.createStatement();
					String book_loans_query="select gg.card_no as card_no,sum(gg.fine_amt) as total_fine from (Select f.Loan_id,f.fine_amt , b.Card_no from fines as f left join book_loans as b on f.loan_id=b.loan_id ) as gg group by card_no order by total_fine";
					

					ResultSet result = mystat.executeQuery(book_loans_query);
					while(model_2.getRowCount() > 0)
					{
					    model_2.removeRow(0);
					}
					while (result.next()) 
					{
						String[] row ={result.getString("card_no"),result.getString("total_fine")};
						model_2.addRow(row);
						
					}
					
					
										
					myconn.close();
					    
						
				}
					
										
				 catch (SQLException ee) {
					// TODO Auto-generated catch block
					
					ee.printStackTrace();
				}
				
				
				
				
				
				
			}
		});
		
		JButton btnNewButton_2 = new JButton("Display current fines only");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{	
					Connection myconn= DriverManager.getConnection("jdbc:mysql://localhost:3306/library?autoReconnect=true&useSSL=false","root","krishna");
					System.out.println ("Database connection established");
					Statement mystat = myconn.createStatement();
					String book_loans_query="Select m.card_no as card_no,sum(fine_amt) as total_fine from ( Select bb.card_no,bb.fine_amt,bb.paid from( Select f.Loan_id,f.fine_amt , b.Card_no, f.paid from fines as f left join book_loans as b on f.loan_id=b.loan_id) as bb where bb.paid='false') as m group by m.card_no order by total_fine;";
					

					ResultSet result = mystat.executeQuery(book_loans_query);
					while(model_2.getRowCount() > 0)
					{
					    model_2.removeRow(0);
					}
					while (result.next()) 
					{
						String[] row ={result.getString("card_no"),result.getString("total_fine")};
						model_2.addRow(row);
						
					}
					
					
										
					myconn.close();
					    
						
				}
					
										
				 catch (SQLException ee) {
					// TODO Auto-generated catch block
					
					ee.printStackTrace();
				}
			}
		});
		
		JScrollPane scrollPane_2 = new JScrollPane();
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(58)
							.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
								.addComponent(textField_8, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblEn, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE))
							.addGap(16)
							.addComponent(btnUpdateFine, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
							.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_4.createSequentialGroup()
									.addGap(295)
									.addComponent(btnRefreshFines, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_4.createSequentialGroup()
									.addGap(284)
									.addComponent(btnDisplayFines, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(58)
							.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
								.addComponent(textField_9, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblEnterTheFine, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE))
							.addGap(446)
							.addComponent(btnNewButton_2))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(12)
							.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 949, GroupLayout.PREFERRED_SIZE)))
					.addGap(12))
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGap(26)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(7)
							.addComponent(textField_8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(3)
							.addComponent(lblEn))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(42)
							.addComponent(btnUpdateFine, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addComponent(btnRefreshFines, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
							.addGap(20)
							.addComponent(btnDisplayFines, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)))
					.addGap(7)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(1)
							.addComponent(textField_9, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(1)
							.addComponent(lblEnterTheFine))
						.addComponent(btnNewButton_2))
					.addGap(16)
					.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 427, GroupLayout.PREFERRED_SIZE))
		);
		
		model_2=new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Card_no", "Total_fine"
				}
			);
		
		
		table_2 = new JTable();
		table_2.setModel(model_2);
		
		scrollPane_2.setViewportView(table_2);
		panel_4.setLayout(gl_panel_4);
	}
}
