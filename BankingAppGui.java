package guis;
import DataBase_Objects.User;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;

/*
    Executes Banking tasks like depositing, withdrawing, and transferring
 */
public class BankingAppGui extends BaseFrame implements ActionListener {
    public BankingAppGui(User user){
        super("Banking App", user);
    }


    private JTextField currentBalanceField;
    public JTextField getCurrentBalanceField(){
        return currentBalanceField;
    }


    @Override
    protected void addGuiComponents() {

        setSize(1000,1200);

        //Add welcome message
        String welcomeMessage = "<html>" + "<body style='text-align:center'>" + "<b>Welcome " + user.getUsername() + "</b><br>" + "What would you like to do today?</body></html>";

        JLabel welcomeMessageLabel = new JLabel(welcomeMessage);
        welcomeMessageLabel.setBounds(0,20,getWidth() - 10,40);
        welcomeMessageLabel.setFont(new Font("Monospace",Font.PLAIN,16));
        welcomeMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeMessageLabel);

        //create current balance label
        JLabel currentBalanceLabel = new JLabel("Current Balance");
        currentBalanceLabel.setBounds(0,80,getWidth() - 10,30);
        currentBalanceLabel.setFont(new Font("Monospace",Font.BOLD,22));
        currentBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(currentBalanceLabel);

        //create current balance text field
        currentBalanceField = new JTextField("$" + user.getCurrentBalance());
        currentBalanceField.setBackground(Color.BLACK);
        currentBalanceField.setBounds(15,120,getWidth() - 50, 40);
        currentBalanceField.setFont(new Font("Monospace",Font.PLAIN,28));
        currentBalanceField.setHorizontalAlignment(SwingConstants.RIGHT);
        currentBalanceField.setEditable(false);
        add(currentBalanceField);

        //add deposit button
        JButton depositButton = new JButton("Deposit");
        depositButton.setBackground(Color.gray);
        depositButton.setBounds(15,180,getWidth() - 50, 50);
        depositButton.setFont(new Font("Monospace",Font.BOLD,22));
        depositButton.addActionListener(this);
        add(depositButton);

        //add in withdraw button
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBackground(Color.gray);
        withdrawButton.setBounds(15,250,getWidth() - 50, 50);
        withdrawButton.setFont(new Font("Monospace",Font.BOLD,22));
        withdrawButton.addActionListener(this);
        add(withdrawButton);

        //Add in view Past Transactions button
        JButton pastTransactionButton = new JButton("View Past Transactions");
        pastTransactionButton.setBackground(Color.gray);
        pastTransactionButton.setBounds(15,390,getWidth() - 50, 50);
        pastTransactionButton.setFont(new Font("Monospace",Font.BOLD,22));
        pastTransactionButton.addActionListener(this);
        add(pastTransactionButton);

        // add transfer button
        JButton transferButton = new JButton("Transfer");
        transferButton.setBackground(Color.gray);
        transferButton.setBounds(15,320,getWidth() - 50, 50);
        transferButton.setFont(new Font("Monospace",Font.BOLD,22));
        transferButton.addActionListener(this);
        add(transferButton);

        // add Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.gray);
        logoutButton.setBounds(15,460,getWidth() - 50, 50);
        logoutButton.setFont(new Font("Monospace",Font.BOLD,22));
        logoutButton.addActionListener(this);
        add(logoutButton);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        //user pressed logout
        if(buttonPressed.equalsIgnoreCase("Logout")){
            //return user to login screen
            new LoginGui().setVisible(true);

            //dispose of current gui
            this.dispose();

            //don't bother running the rest of the code
            return;

        }
        //other functions
        BankingAppDialog bankingAppDialog = new BankingAppDialog(this,user);

        //set the title of thr dialog header to the action
        bankingAppDialog.setTitle(buttonPressed);

        // if the button is pressed it deposits, withdraws, or transfers
        if(buttonPressed.equalsIgnoreCase("Deposit") || buttonPressed.equalsIgnoreCase("Withdraw") || buttonPressed.equalsIgnoreCase("Transfer")){
            //add in the current bal and amount gui components to the dialog
            bankingAppDialog.addCurrentBalanceAndAmount();

            //add action button
            bankingAppDialog.addActionButton(buttonPressed);

            // for the transfer action it will require more components
            if(buttonPressed.equalsIgnoreCase("Transfer")){
                bankingAppDialog.addUserField();
            }

        }else if(buttonPressed.equalsIgnoreCase("View Past Transactions")){
            bankingAppDialog.addPastTransactionComponents();
        }

        //make the app dialog visible
        bankingAppDialog.setVisible(true);
    }
}
