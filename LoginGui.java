package guis;

import DataBase_Objects.DataBaseClass;
import DataBase_Objects.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/*
    This gui will allow the user to login or launch the register gui
    this extends from the BaseFrame which means we will need to define our own addGuiComponents()
 */
public class LoginGui extends BaseFrame{
    public LoginGui(){
        super("Banking App Login");
    }
    @Override
    protected void addGuiComponents() {
        // create banking app label
        JLabel bankingAppLabel = new JLabel("Walter Financial");

        //set the location and size of the gui component
        bankingAppLabel.setBounds(0, 20, super.getWidth(), 40);

        //change the fot style
        bankingAppLabel.setFont(new Font("Nabla", Font.BOLD,32));

        //center the text in Jlabel
        bankingAppLabel.setHorizontalAlignment(SwingConstants.CENTER);
        

        //add to gui
        add(bankingAppLabel);

        //username label
        JLabel usernameLabel = new JLabel("Username:");

        //getWidth() returns us the width of our frame which is about 420
        usernameLabel.setBounds(20,120,getWidth() - 30,24);

        usernameLabel.setFont(new Font("Monospace",Font.PLAIN,20));
        add(usernameLabel);

        // create username field
        JTextField usernameField = new JTextField();
        usernameField.setBounds(20,160,getWidth() - 50, 40);
        usernameField.setFont(new Font("Monospace",Font.PLAIN,28));
        add(usernameField);

        //create password Label
        JLabel passwordLabel = new JLabel("Password:");

        //getWidth() returns us the width of our frame which is about 420
        passwordLabel.setBounds(20,280,getWidth() - 50,24);
        passwordLabel.setFont(new Font("Monospace",Font.PLAIN,20));
        add(passwordLabel);

        //create password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20,320,getWidth() - 50, 40);
        passwordField.setFont(new Font("Monospace",Font.PLAIN,28));
        add(passwordField);

        //create login button
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(20,460,getWidth() - 50, 40);
        loginButton.setFont(new Font("Monospace",Font.BOLD,20));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get username
                String username = usernameField.getText();

                //get pass
                String password = String.valueOf(passwordField.getPassword());

                // now validate the login credentials
                User user = DataBaseClass.validateLogin(username,password);

                if(user != null){
                    //this means it was a valid login
                    // dispose the gui
                    LoginGui.this.dispose();

                    //launch bank app gui
                    BankingAppGui bankingAppGui = new BankingAppGui(user);
                    bankingAppGui.setVisible(true);

                    //show a success message
                    JOptionPane.showMessageDialog(bankingAppGui,"Login Successful!!");
                }
                else{
                    //not a valid login
                    JOptionPane.showMessageDialog(LoginGui.this, "Login Failed :(");

                }
            }
        });
        add(loginButton);

        //create register label
        JLabel registerLabel = new JLabel("<html><a href=\"\">Don't have an account? Register Here</a></html>");
        registerLabel.setBounds(0,510,getWidth() - 10, 30);
        registerLabel.setFont(new Font("Monospace",Font.PLAIN,20));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //adds event listener so when the mouse is clicked on the register label the register gui is launched
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //dispose of this gui
                LoginGui.this.dispose();

                //Launch the register gui
                new RegisterGui().setVisible(true);
            }
        });
        add(registerLabel);
    }
}
