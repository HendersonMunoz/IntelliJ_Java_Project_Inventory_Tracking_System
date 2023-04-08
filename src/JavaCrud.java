import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

// Code below gets generated automatically as I make changes/additions to the GUI design in the JavaCrud.form file.
public class JavaCrud {
    private JPanel Main;
    private JTextField txtName;
    private JTextField txtPrice;
    private JTextField txtQty;
    private JButton saveButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton searchButton;
    private JTextField txtpid;

    // The main method
    public static void main(String[] args) {
        JFrame frame = new JFrame("JavaCrud");
        frame.setContentPane(new JavaCrud().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    // 'con' and 'pst' variables below were created to help with the DB connection.
    // The DB connection code block is at the end of the JavaCrud.java file
    Connection con;
    PreparedStatement pst;

    // Connect method, housing the functionality for all the buttons in the project.
    public JavaCrud() {
        Connect();

        // Save button action listener
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // declaring string variables.
                String name,price,qty;

                // Getting the user input
                name = txtName.getText();
                price = txtPrice.getText();
                qty = txtQty.getText();

                try {
                    // SQL insert script, using the data gathered from the user then adding it to the DB.
                    pst = con.prepareStatement("insert into products(pname,price,qty)values(?,?,?)");
                    pst.setString(1, name);
                    pst.setString(2, price);
                    pst.setString(3, qty);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null,"Record Added!");

                    // resetting the datafields and positioning the cursor back to the top data field, after the users
                    // clicks on the Save button
                    txtName.setText("");
                    txtPrice.setText("");
                    txtQty.setText("");
                    txtName.requestFocus();
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }

            }
        });

        // Code block for the Search button functionality.
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // declaring the pid (product ID) variable to catch the user input in the Search field.
                    String pid = txtpid.getText();
                    // SQL statement that will fetch for product details from the DB.
                    pst = con.prepareStatement("select pname,price,qty from products where pid = ?");
                    pst.setString(1, pid);
                    ResultSet rs = pst.executeQuery();

                    // If the user enter an existing/valid pid value, the system will fetch the product details from the DB and display them.
                    if(rs.next()==true)
                    {
                        String name = rs.getString(1);
                        String price = rs.getString(2);
                        String qty = rs.getString(3);

                        txtName.setText(name);
                        txtPrice.setText(price);
                        txtQty.setText(qty);
                    }
                    // if the user does not enter a valid pid value, the data fields will reset and the message below will display.
                    else
                    {
                        txtName.setText("");
                        txtPrice.setText("");
                        txtQty.setText("");
                        JOptionPane.showMessageDialog(null,"Product does not exist!");
                    }
                }

                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }
            }
        });
        // Code block for the update button functionality.
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // declaring String variables.
                String pid,name,price,qty;

                name = txtName.getText();
                price = txtPrice.getText();
                qty = txtQty.getText();
                pid = txtpid.getText();

                try {
                    // SQL - update script. It'll update the product details, based on the user input.
                    pst = con.prepareStatement("update products set pname = ?,price = ?,qty = ? where pid = ?");
                    pst.setString(1, name);
                    pst.setString(2, price);
                    pst.setString(3, qty);
                    pst.setString(4, pid);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Updated!");

                    // Once the update has been completed, and the user clicks 'ok' on the post update message, the data fields will reset
                    txtName.setText("");
                    txtPrice.setText("");
                    txtQty.setText("");
                    txtName.requestFocus();
                    txtpid.setText("");
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });

        // Code block for the Delete button functionality.
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // String variable
                String bid;
                bid = txtpid.getText();

                // If the user queries a record/product, then clicks on the 'Delete' button,
                // the SQL delete script below will execute. Deleting the record from the DB.
                try {
                    pst = con.prepareStatement("delete from products  where pid = ?");
                    pst.setString(1, bid);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Deleted!");

                    // Clearing Data fields after the product has been deleted.
                    txtName.setText("");
                    txtPrice.setText("");
                    txtQty.setText("");
                    txtName.requestFocus();
                    txtpid.setText("");
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }
        });
    }

    // Code block below is the Connect method that helps link this project, to the database I created for this project.
    // DB name, user, and password must be given in the URL parameter.
    // mysql-connector.jar file must also be downloaded and added to the project dependencies for the DB connection to work.
    // XAMPP tool also needs to be installed. So that I'm able to run an Apache server and an MySQL server when using the DB.
    public void Connect()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/my_electronics", "root", "root");
            System.out.println("Database connection has been established!");
        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
}
