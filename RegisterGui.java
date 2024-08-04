package guis;

import DataBase_Objects.DataBaseClass;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterGui extends BaseFrame{
    public RegisterGui(){
        super("Bank Account Register");
    }

    @Override
    protected void addGuiComponents() {

        // create banking app label
        JLabel bankingAppLabel = new JLabel("Walter Financial");

        //set the location and size of the gui component
        bankingAppLabel.setBounds(0, 20, super.getWidth(), 40);

        //change the fot style
        bankingAppLabel.setFont(new Font("Monospace", Font.BOLD,32));

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
        passwordLabel.setBounds(20,220,getWidth() - 50,24);
        passwordLabel.setFont(new Font("Monospace",Font.PLAIN,20));
        add(passwordLabel);

        //create password field
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20,260,getWidth() - 50, 40);
        passwordField.setFont(new Font("Monospace",Font.PLAIN,28));
        add(passwordField);

        //create re_password Label
        JLabel re_passwordLabel = new JLabel("Re-enter Password:");

        //getWidth() returns us the width of our frame which is about 420
        re_passwordLabel.setBounds(20,320,getWidth() - 50,24);
        re_passwordLabel.setFont(new Font("Monospace",Font.PLAIN,20));
        add(re_passwordLabel);

        //create re_password field
        JPasswordField re_passwordField = new JPasswordField();
        re_passwordField.setBounds(20,360,getWidth() - 50, 40);
        re_passwordField.setFont(new Font("Monospace",Font.PLAIN,28));
        add(re_passwordField);

        //create Register button
        JButton loginButton = new JButton("Register");
        loginButton.setBounds(20,440,getWidth() - 50, 40);
        loginButton.setFont(new Font("Monospace",Font.BOLD,20));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get username
                String username = usernameField.getText();

                //get pass
                String password = String.valueOf(passwordField.getPassword());

                //get re-entered password
                String rePassword = String.valueOf(re_passwordField.getPassword());

                // we now need to validate the user input

                if(validateUserInput(username,password,rePassword)){
                    //now try and register the user in the database
                    if(DataBaseClass.register(username,password)){
                        // registration successful
                        // dispose of this gui
                        RegisterGui.this.dispose();

                        // now launch the login gui

                        LoginGui loginGui = new LoginGui();
                        loginGui.setVisible(true);

                        //now display a registration complete message
                        JOptionPane.showMessageDialog(loginGui,"Account Registration Successful :)");
                    }else{
                        // registration failed message
                        JOptionPane.showMessageDialog(RegisterGui.this, "Error: Username Already in use");
                    }
                }else{
                    // invalid user input either username or password
                    JOptionPane.showMessageDialog(RegisterGui.this,
                            "Error: Username must be at least 8 characters long\n" +
                                    "and/or Password must match");
                }
            }
        });
        add(loginButton);

        //create Login label
        JLabel LoginLabel = new JLabel("<html><a href=\"\">Already have an account? Login Here</a></html>");
        LoginLabel.setBounds(0,500,getWidth() - 10, 30);
        LoginLabel.setFont(new Font("Monospace",Font.PLAIN,20));
        LoginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //adds event listener so when the mouse is clicked on the Login label the Login gui is launched
        LoginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //dispose of this gui
                RegisterGui.this.dispose();

                //Launch the Login gui
                new LoginGui().setVisible(true);
            }
        });
        add(LoginLabel);
    }

    private boolean validateUserInput(String username, String password, String rePassword){
        //requirements: all fields must have a value
        //              username has to be at least 8 characters long
        //              password and the re-password have to be the same
        if(username.isEmpty() || password.length() == 0 || rePassword.length() ==0) return false;

        if(username.length() < 8) return false;

        if(!password.equals(rePassword)) return false;

        //if passes all these it grants validation
        return true;
    }
}
