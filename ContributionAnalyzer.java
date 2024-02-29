package version_5;

import java.sql.*;
import java.util.Scanner;

public class ContributionAnalyzer {
    
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/oops";
    static final String USERNAME = "root";
    static final String PASSWORD = "welcome";
    static final int TOTAL_ACTIVE_TIME = 16;
    static final int TOTAL_PASSIVE_TIME = 8;

    public static void main(String[] args) {
        try {         
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);           
            String query = "SELECT PersonID, Person_Name, Amount_Contributed FROM persons ORDER BY Amount_Contributed DESC";           
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);          
            ResultSet resultSet = statement.executeQuery(query);           
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your name to log in: ");
            String loggedInPerson = scanner.nextLine();
            System.out.println("Welcome, " + loggedInPerson + "!");
            int rank = 1;
            boolean shouldLogout = false;
            double totalContribution = 0;          
            while (resultSet.next()) {
                double contribution = resultSet.getDouble("Amount_Contributed");
                totalContribution += contribution;
            }           
            resultSet.beforeFirst();
            while (resultSet.next()) {
                int personID = resultSet.getInt("PersonID");
                String personName = resultSet.getString("Person_Name");
                double contribution = resultSet.getDouble("Amount_Contributed");              
                double activeTime = (contribution / totalContribution) * TOTAL_ACTIVE_TIME;
                double passiveTime = (contribution / totalContribution) * TOTAL_PASSIVE_TIME;         
                System.out.println("Rank " + rank + ": PersonID: " + personID + ", Person Name: " + personName +
                        ", Contribution: " + contribution +
                        ", Available Active Time: " + activeTime + " hours, Available Passive Time: " + passiveTime + " hours\n");               
                if (personName.equals(loggedInPerson) && shouldLogout) {
                    System.out.println("Please log out. Lower-ranked individuals should log out.");
                    break;
                }              
                if (personName.equals(loggedInPerson)) {
                    shouldLogout = true;
                }          
                if (shouldLogout && !personName.equals(loggedInPerson)) {
                    System.out.println(personName + ", please log out. Lower-ranked individuals should log out.\n");
                }
                rank++;
            }         
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
