package DataBase_Objects;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

/*
    This class will enable the app to interact with the SQL database to perform activities such as updating and retrieving things in the DB
 */
public class DataBaseClass {
    //Data base configs

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/bankapp";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "password";

    //if valid then return an obj with the user's info
    public static User validateLogin(String username, String password){
        try{
            //est a connection to the BD
            Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);

            // creation of SQL query
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?"
            );

            // replacing the ? with values by indexing 1 to the first ? and 2 to the second ?
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);

            //execute the query and add(store) into a result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //next() returns true or false
            // true means the query returned data and the result set now points to the first row
            // false means the query returned no data and result set equals to null
            if(resultSet.next()){
                //if successful then get the id
                int userId = resultSet.getInt("id");

                // retrieve current balance
                BigDecimal currentBalance = resultSet.getBigDecimal("current_balance");

                // return user object
                return new User(userId,username,password,currentBalance);
            }


        }catch(SQLException e){
            e.printStackTrace();
        }
        //if not valid user
        return null;
    }

    // this will register a new user in the database
    // true means register success
    // false means register failed
    public static  boolean register(String username, String password){
        try{
            // first check and see if entered username is already in use
            if(!checkUser(username)){
                Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO users(username, password, current_balance) " + "VALUES(?, ?, ?)"
                );

                preparedStatement.setString(1,username);
                preparedStatement.setString(2,password);
                preparedStatement.setBigDecimal(3, new BigDecimal(0));

                preparedStatement.executeUpdate();

                return true;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    //check the db for the entered username
    // true means user exists
    //false means new user
    private static boolean checkUser(String username){
        try{
            Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();

            // this means that the query returned no data so this tells us that there is no user with the entered username
            if(!resultSet.next()){
            return false;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return true;
    }

    //true means the db update was successful
    // false means update to db failed
    public static boolean addTransactionToDatabase(Transaction transaction){
        try{
            Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);

            PreparedStatement insertTransaction = connection.prepareStatement(
                    "INSERT transactions(User_id, transaction_type, transaction_amount, transaction_date) " + "VALUES(?, ?, ?, NOW())"
            );
            //NOW() will insert the current date

            insertTransaction.setInt(1,transaction.getUserId());
            insertTransaction.setString(2, transaction.getTransactionType());
            insertTransaction.setBigDecimal(3,transaction.getTransactionAmount());

            // need to update the db after each transaction
            insertTransaction.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }
    //true - update bal was success
    // false - update bal failed
    public static boolean updateCurrentBalance(User user){
        try{
            Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);

            PreparedStatement updateBalance = connection.prepareStatement(
                    "UPDATE users SET current_balance = ? WHERE id = ?"
            );

            updateBalance.setBigDecimal(1, user.getCurrentBalance());
            updateBalance.setInt(2,user.getId());

            updateBalance.executeUpdate();

            return true;

        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    //true - transfer successful
    //false- transfer failed
    public static boolean transfer(User user, String transferredUsername, float transferAmount){
        try{
            Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);

            PreparedStatement queryUser = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );
            queryUser.setString(1,transferredUsername);
            ResultSet resultSet = queryUser.executeQuery();

            while(resultSet.next()){
                //perform transfer
                User transferredUser = new User(
                        resultSet.getInt("id"),
                        transferredUsername,
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance")
                );

                //create trans
                Transaction transferTransaction = new Transaction(
                        user.getId(),
                        "Transfer",
                        new BigDecimal(-transferAmount),
                        null

                );

                //new transaction for the transferred user
                Transaction recievedTransaction = new Transaction(
                        transferredUser.getId(),
                        "Transfer",
                        new BigDecimal(transferAmount),
                        null
                );

                //update transfer user
                transferredUser.setCurrentBalance(transferredUser.getCurrentBalance().add(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(transferredUser);

                //update user current bal
                user.setCurrentBalance(user.getCurrentBalance().subtract(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(user);

                //add all these transactions to the database
                addTransactionToDatabase(transferTransaction);
                addTransactionToDatabase(recievedTransaction);

                return true;
            }


        }catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    // retrieve all the past transactions (will be called to in the view past transactions)
    public static ArrayList<Transaction> getPastTransaction(User user){
        ArrayList<Transaction> pastTransactions = new ArrayList<>();
        try{
            Connection connection = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);

            PreparedStatement selectAllTransaction = connection.prepareStatement(
                    "SELECT * FROM transactions WHERE user_id = ?"
            );
            selectAllTransaction.setInt(1,user.getId());

            ResultSet resultSet = selectAllTransaction.executeQuery();

            //iterate through the results if there are any
            while(resultSet.next()){

                //crate a trans obj
                Transaction transaction = new Transaction(
                        user.getId(),
                        resultSet.getString("transaction_type"),
                        resultSet.getBigDecimal("transaction_amount"),
                        resultSet.getDate("transaction_date")
                );


                // store this into an array list
                pastTransactions.add(transaction);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

        return pastTransactions;
    }

}
