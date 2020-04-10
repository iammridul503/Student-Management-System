/*
@author :Mridul Mishra


To execute:
Step:1-javac StudentManagementSystem.java
Step:2-java -cp ".:*" Main
*/
import java.sql.*;
import java.util.*;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.Period;

public class StudentManagementSystem
{
    static Connection con=null;
    Scanner sc = new Scanner(System.in);
    public void addStudent()
    {
        System.out.println("Enter the Student's Details");
        System.out.print("Name: ");
        String str=sc.nextLine();
        int ch =str.indexOf(' ');
        String studentName=Character.toUpperCase(str.charAt(0))+str.substring(1,ch)+" "+Character.toUpperCase(str.charAt(ch+1))+str.substring(ch+2,str.length());
        System.out.print("\nDate of Birth (YYYY-MM-DD): ");
        LocalDate date=LocalDate.parse(sc.nextLine());
        LocalDate now=LocalDate.parse("2020-04-01");
        Period dur = Period.between(date, now);
        String age = Integer.toString(dur.getYears())+" Years "+Integer.toString(dur.getMonths())+" Months "+Integer.toString(dur.getDays())+" Days";
        System.out.print("\nFather's Name: ");
        String fatherName=sc.nextLine();
        System.out.print("\nMother's Name: ");
        String motherName=sc.nextLine();
        System.out.print("\nMobile No: ");
        long mobileNum= sc.nextLong();
        int uniqueId=20200000+(int)(1000.0 * Math.random());
        
        String createTable = "CREATE TABLE IF NOT EXISTS Student(\n"  
        + " id integer PRIMARY KEY,\n" 
        + " uniqueId integer NOT NULL,\n" 
        + " studentName text NOT NULL,\n" 
        + " age integer NOT NULL,\n"
        + " fatherName text NOT NULL,\n"  
        + " motherName text NOT NULL,\n"   
        + " mobileNum long NOT NULL\n"  
        + ");";  
        String insertTable="INSERT INTO Student(uniqueId, studentName, age, fathername, motherName, mobileNum) VALUES(?,?,?,?,?,?)";
        try
        {  
            Statement stmt = con.createStatement();  
            stmt.execute(createTable);  
            PreparedStatement pstmt = con.prepareStatement(insertTable);  
            pstmt.setInt(1, uniqueId);
            pstmt.setString(2, studentName);
            pstmt.setString(3, age);
            pstmt.setString(4, fatherName);
            pstmt.setString(5, motherName);
            pstmt.setLong(6, mobileNum);
            pstmt.executeUpdate();
        } 
        catch (SQLException e) 
        {  
            System.out.println(e.getMessage());  
            System.out.println("Insertion failed....");
        }  


        System.out.print("\033[H\033[2J");  
        System.out.flush();
        System.out.println("New Student Data Added...");
        System.out.println("Registration No: "+uniqueId);        
    }
    public void searchStudent()
    {
        System.out.println("Enter the student's name");
        String str=sc.nextLine();
        int ch =str.indexOf(' ');
        String studentName=Character.toUpperCase(str.charAt(0))+str.substring(1,ch)+" "+Character.toUpperCase(str.charAt(ch+1))+str.substring(ch+2,str.length());
        String searchStudent = "SELECT * FROM Student WHERE studentName = '"+studentName+"'";
        try
        {
            PreparedStatement pstmt  = con.prepareStatement(searchStudent);
            ResultSet rs  = pstmt.executeQuery();
            System.out.println("Id: "+rs.getInt("id"));
            System.out.println("Unique Id: "+rs.getInt("uniqueId"));
            System.out.println("Name: "+rs.getString("studentName"));
            System.out.println("Age: "+rs.getString("age"));
            System.out.println("Father's Name: "+rs.getString("fatherName"));
            System.out.println("Mother's Name: "+rs.getString("motherName"));
            System.out.println("Mobile No.: "+rs.getLong("mobileNum"));
        }
        catch(Exception e)
        {
           System.out.println("Student does not exist..");
        }
    }  
    public void searchAllStudent()
    {
        String searchStudent = "SELECT * FROM Student ";
        Statement stmt=null;
        try
        {
            stmt  = con.createStatement();
            ResultSet rs  = stmt.executeQuery(searchStudent);
            System.out.format("%5s%10s%30s%30s%30s%30s%15s","Id","Unique Id", "Student Name", "Age", "Father Name", "Mother Name","Mobile No");
            System.out.println();

            while(rs.next())
            {
                System.out.format("%5d%10d%30s%30s%30s%30s%15d",rs.getInt("id"),rs.getInt("uniqueId"),rs.getString("studentName"),rs.getString("age"),rs.getString("fatherName"),rs.getString("motherName"),rs.getLong("mobileNum"));
                System.out.println();
            }
        }
        catch(Exception e)
        {
           // System.out.println(e.getMessage());
            System.out.println("Student does not exist..");
        }

    }        
}
class Main extends StudentManagementSystem
{
    
    public static void connect()
    {
        
        try
        {
            Class.forName("org.sqlite.JDBC");
            con=DriverManager.getConnection("jdbc:sqlite:SMS.db");
            System.out.println("\nConnected to the Database.....");
        }
        catch(Exception e)
        {
            System.out.println(e);
            System.out.println("\n\n\n\nConnection to the DATABASE could not be established...");
            System.exit(1);
        }

    }
    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }
    public static void main(String[] args) throws Exception
    {   
        System.out.print("Connecting");
        for(int i=1;i<=4;i++)
        {wait(1000);System.out.print(".");} 

        StudentManagementSystem ob =new StudentManagementSystem();
        System.out.print("\033[H\033[2J");  
        System.out.flush();
        connect();
        
        Scanner sc =new Scanner(System.in);
        System.out.println("Student ManageMent System");
        System.out.println("1.Add new Student");
        System.out.println("2.Search Student");
        System.out.println("3.Print Entire DataBase");
        System.out.print("Enter the choice: ");
        int choice = sc.nextInt();
        
        switch(choice)
        {
            case 1:
            {
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                ob.addStudent();
                break;
            }
            case 2:
            {   
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                ob.searchStudent();
                break;

            }
            case 3:
            {
                System.out.print("\033[H\033[2J");  
                System.out.flush();
                ob.searchAllStudent();
                break;

            }
            default:
            {
                System.out.println("Wrong Choice");
            }
        } 
        con.close();
    } 
}