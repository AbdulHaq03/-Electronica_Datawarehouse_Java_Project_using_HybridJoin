import java.sql.*;
import java.util.concurrent.BlockingQueue;
import java.util.*;

public class StreamGenerator extends Thread{
	
	private ResultSet rs;
	private ArrayList<String> result = new ArrayList<>();
	private BlockingQueue<ArrayList<String>> Producer;
	private String url;
	private String usr;
	private String pass;
	
	public StreamGenerator(BlockingQueue<ArrayList<String>> pr, String url, String usr, String pass)
	{
		Producer = pr;
		this.url = url;
		this.usr = usr;
		this.pass = pass;
	}
	
	@Override
	public void run()
	{
//		url = "jdbc:mysql://localhost:3306/transactions_data";
//		Username = "root";
//		String password = "root";
		
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Connection connect = DriverManager.getConnection(url, usr, pass);
			Statement statement = connect.createStatement();
			
			String query = "Select * from transactions";
			
			rs = statement.executeQuery(query);
			
			System.out.println("Query Successful");

			while(rs.next())
			{
				result.add(rs.getString("Product ID"));
				result.add(rs.getString("Order ID"));
				result.add(rs.getString("Order Date"));
				result.add(rs.getString("Customer ID"));
				result.add(rs.getString("Customer Name"));
				result.add(rs.getString("Gender"));
				result.add(rs.getString("Quantity Ordered"));
				
				Producer.put(result);
				result = new ArrayList<>();
				//Thread.sleep(100);
			}
			connect.close();
			
		}
		catch (Exception e)
		{
		}
	}
}
