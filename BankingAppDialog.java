package guis;

import DataBase_Objects.DataBaseClass;
import DataBase_Objects.Transaction;
import DataBase_Objects.User;

import javax.jws.soap.SOAPBinding;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;

/*
    Displays a custom dialog for the banking app gui
 */
public class BankingAppDialog extends JDialog implements ActionListener {
    private User user;
    private BankingAppGui bankingAppGui;
    private JLabel balanceLabel, enterAmountLabel, enterUserLabel;
    private  JTextField enterAmountField, enterUserField;
    private JButton actionButton;
    private JPanel pastTransactionPanel;
    private ArrayList<Transaction> pastTransactions;
    public BankingAppDialog(BankingAppGui bankingAppGui, User user){
        // set size of the screen
        setSize(400,400);

        // add focus to the dialog so nothing else can be interacted with until dialog is closed
        setModal(true);

        // loads the pop up into the center of the banking app gui
        setLocationRelativeTo(bankingAppGui);

        //when  user closes the dialog it releses the resources that were being used
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        //prevent dialog from being resized
        setResizable(false);

        //allows the position and size of each compartment to be manually set
        setLayout(null);

        // referance our gui so we can update the current balance of the user
        this.bankingAppGui = bankingAppGui;

        //need to access the user info to update the db or retrieve info about the user
        this.user = user;

    }

    public void addCurrentBalanceAndAmount(){
        //balance label
        balanceLabel = new JLabel("Balance: $" + user.getCurrentBalance());
        balanceLabel.setBounds(0,10,getWidth() - 20, 20);
        balanceLabel.setFont(new Font("Dialog", Font.BOLD,16));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);

        // enter amount label
        enterAmountLabel = new JLabel("Enter Amount:");
        enterAmountLabel.setBounds(0,50,getWidth() - 20, 20);
        enterAmountLabel.setFont(new Font("Dialog", Font.BOLD,16));
        enterAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountLabel);

        //enter amount field
        enterAmountField = new JTextField();
        enterAmountField.setBounds(15,80,getWidth() - 50, 40);
        enterAmountField.setFont(new Font("Dialog", Font.BOLD,20));
        enterAmountField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountField);

    }
    public void addActionButton(String actionButtonType){
        actionButton = new JButton(actionButtonType);
        actionButton.setBounds(15,300,getWidth() - 50, 40);
        actionButton.setFont(new Font("Dialog", Font.BOLD,20));
        actionButton.addActionListener(this);
        add(actionButton);

    }

    public void addUserField(){
        // add enter user label
        enterUserLabel = new JLabel("Enter User:");
        enterUserLabel.setBounds(0,160,getWidth() - 20, 20);
        enterUserLabel.setFont(new Font("Dialog", Font.BOLD,16));
        enterUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserLabel);

        //add enter user field
        enterUserField = new JTextField();
        enterUserField.setBounds(15,190,getWidth() - 50, 50);
        enterUserField.setFont(new Font("Dialog", Font.BOLD,20));
        enterUserField.setHorizontalAlignment(SwingConstants.CENTER);

        add(enterUserField);

    }

    public void addPastTransactionComponents(){
        // the container where the stored transactions will be located
        pastTransactionPanel = new JPanel();

        //MAKING THE layout a 1x1
        pastTransactionPanel.setLayout(new BoxLayout(pastTransactionPanel,BoxLayout.Y_AXIS));
        //pastTransactionPanel.setSize(400,400);

        //add the ability to scroll threw the panel
        JScrollPane scrollPane = new JScrollPane(pastTransactionPanel);

        // will display the vertical scroll only when it is needed
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0,20,getWidth() - 15,getHeight() - 15);


        // need to perform a database call to update and retrieve all past transactions and store them into the array list
        pastTransactions = DataBaseClass.getPastTransaction(user);

        //iterate through the list and add to the gui
        for(int i = 0; i < pastTransactions.size(); i++){
            //store the current trans
            Transaction pastTransaction = pastTransactions.get(i);

            // create a container to store these transactions in
            JPanel pastTransactionContainer = new JPanel();
            pastTransactionContainer.setLayout(new BorderLayout());

            //Create trans type label for gui
            JLabel transactionTypeLabel = new JLabel(pastTransaction.getTransactionType());
            transactionTypeLabel.setFont(new Font("Dialog",Font.BOLD,20));

            //add the transaction amount label
            JLabel transactionAmountLabel = new JLabel(String.valueOf(pastTransaction.getTransactionAmount()));
            transactionAmountLabel.setFont(new Font("Dialog",Font.BOLD,20));

            // add past transaction date label
            JLabel transactionDateLabel = new JLabel(String.valueOf(pastTransaction.getTransactionDate()));
            transactionDateLabel.setFont(new Font("Dialog",Font.BOLD,20));

            // add these to the container
            pastTransactionContainer.add(transactionTypeLabel,BorderLayout.WEST); // this will be on the west side
            pastTransactionContainer.add(transactionAmountLabel,BorderLayout.EAST); // this will be on the east side
            pastTransactionContainer.add(transactionDateLabel,BorderLayout.SOUTH); // this will be placed south

            // give a Magenta background to each of these
            pastTransactionContainer.setBackground(Color.gray);

            //add a black border to each trans container
            pastTransactionContainer.setBorder(BorderFactory.createLineBorder(Color.black));

            // finally add the transaction component to the transaction panel
            pastTransactionPanel.add(pastTransactionContainer);

            //add to the dialog
            add(scrollPane);
        }
    }

    private void handleTransaction(String transactionType, float amountVal){
        Transaction transaction;
        if(transactionType.equalsIgnoreCase("Deposit")){
            // deposit trans type
            //add to current balance
            user.setCurrentBalance(user.getCurrentBalance().add(new BigDecimal(amountVal)));

            //create transaction
            // leave the date null because we are going to be using the NOW() function in sql to get the current date
            transaction = new Transaction(user.getId(),transactionType,new BigDecimal(amountVal),null);
        }
        else{
            //withdraw transaction type
            //subtract from current balance
            user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(amountVal)));

            //we want to show a negative sign in the amount val when withdrawing
            transaction = new Transaction(user.getId(),transactionType,new BigDecimal(-amountVal),null);
        }

        //update database
        if(DataBaseClass.addTransactionToDatabase(transaction) && DataBaseClass.updateCurrentBalance(user)){
            //show success dialog
            JOptionPane.showMessageDialog(this,transactionType + " Successfully!!");

            //reset the fields
            resetFieldsAndUpdateCurrentBalance();

        }else{
            // display the failure dialog
            JOptionPane.showMessageDialog(this, transactionType + "Failed... :(");
        }
    }

    private  void resetFieldsAndUpdateCurrentBalance(){
        //reset fields
        enterAmountField.setText("");

        //only appears when transfer is clicked on
        if(enterUserField != null){
            enterUserField.setText("");
        }

        //update current balance on dialog
        balanceLabel.setText("Balance $" + user.getCurrentBalance());

        // update the current balance on the main gui
        bankingAppGui.getCurrentBalanceField().setText("$" + user.getCurrentBalance());
    }


    private void handleTransfer(User user, String transferredUser, float amount){

        //attempt to perform transfer
        if(DataBaseClass.transfer(user,transferredUser,amount)){
            // show success
            JOptionPane.showMessageDialog(this,"Transfer was Successful!!");
            resetFieldsAndUpdateCurrentBalance();
        }else{
            //show failed
            JOptionPane.showMessageDialog(this,"Transfer Failed... :(");
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        //get amount value
        float amountVal = Float.parseFloat(enterAmountField.getText());

        //pressed deposit
        if(buttonPressed.equalsIgnoreCase("Deposit")){
            // we need to handle the deposit trans function
            handleTransaction(buttonPressed,amountVal);
        }
        else{
            // pressed either withdraw or transfer

            // need to verify input by making sure that the withdraw or transfer is less than the current balance
            //if the result is -1 it means that the entered amount is more, 0 means they're equal, and 1 means that the entered amount is less
            int result = user.getCurrentBalance().compareTo(BigDecimal.valueOf(amountVal));
            if(result < 0){
                //display error dialog
                JOptionPane.showMessageDialog(this,"Error: Inputted value is more than your current balance!");
                return;

            }
            //check to see if withdraw or transfer was pressed
            if(buttonPressed.equalsIgnoreCase("Withdraw")){
                handleTransaction(buttonPressed,amountVal);

            }
            else{
                //TRANSFER  has been pressed
                String transferredUser = enterUserField.getText();

                //handle transfer
                handleTransfer(user,transferredUser,amountVal);
            }

        }
    }
}
