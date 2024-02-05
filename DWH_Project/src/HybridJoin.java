import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.collections4.MultiValuedMap;
import java.util.concurrent.BlockingQueue;
import java.util.*;
import java.util.Date;

public class HybridJoin extends Thread {
	
	private ResultSet Master_rs;
	private ArrayList<ArrayList<String>> RESS = new ArrayList<>();
	private ArrayList<String> Recieve = new ArrayList<>();
	private BlockingQueue<ArrayList<String>> Consumer;
	private MultiValuedMap<String, ArrayList<String>> map = new ArrayListValuedHashMap<>();
	private Queue streamQueue = new Queue();
	private String DEE;
	private String url;
	private String usr;
	private String pass;
	private String url1;
	
	public HybridJoin(BlockingQueue<ArrayList<String>> cr, String url, String usr, String pass, String url1)
	{
		Consumer = cr;
		this.url = url;
		this.usr = usr;
		this.pass = pass;
		this.setUrl1(url1);
	}
	
	@Override
	public void run()
	{
		try {
			int i=0;
			while(true && i < 30000)
			{	
				Recieve = Consumer.take();
				
				streamQueue.enqueue(Recieve.get(0));
				
				map.put(Recieve.remove(0), Recieve);
				
				i ++;
				
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(!streamQueue.isEmpty())
		{
			//streamQueue.print();
			List<ArrayList<String>> values = null;
			ArrayList<String> getVals = null;
			ArrayList<String> value = null;
			connection();
			Iterator<ArrayList<String>> iter = RESS.iterator();
			while(iter.hasNext())
			{
				//values = (List<ArrayList<String>>) map.remove(RESS.get(k).get(0));
				value = iter.next();
				values = (List<ArrayList<String>>) map.remove(value.get(0));
				
				for(int j=0; j<values.size(); j++)
				{
					getVals = values.get(j);
					
					if (getVals.size() != 0)
					{
						int productID = Integer.parseInt(value.get(0));
						
						int OrderID = Integer.parseInt(getVals.get(0));
						
						String date = getVals.get(1);
						
						SimpleDateFormat formatt = new SimpleDateFormat("MM/dd/yy HH:mm");
						
						Date parsed = null;
						try {
							parsed = formatt.parse(date);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						Calendar cal = Calendar.getInstance();
						cal.setTime(parsed);
						
						int weeks = cal.get(Calendar.DAY_OF_WEEK);
						
						if (weeks > 4)
						{
							weeks = 4;
						}
						
						int months = cal.get(Calendar.MONTH) + 1; 
						int days = cal.get(Calendar.DAY_OF_MONTH); 
						int years = cal.get(Calendar.YEAR);
						
						int quarter = (months - 1) / 3 + 1;
						
						int CustomerID = Integer.parseInt(getVals.get(2)); 
						
						String CustomerName = getVals.get(3);
						
						char Gender = getVals.get(4).charAt(0);
						
						int Quantity_O = Integer.parseInt(getVals.get(5));
						
						String ProductName = value.get(1);
						
						float ProductPrice = Float.parseFloat(value.get(2).replace("$", "").replace(" ", ""));
						
						int SupplierID = Integer.parseInt(value.get(3));
						
						String SupplierName = value.get(4);
						
						int StoreID = Integer.parseInt(value.get(5));
						
						String StoreName = value.get(6);
						
						
						//Class.forName("com.mysql.jdbc.Driver");
						Connection connect = null;
						try {
							connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/electronica-dw", usr, pass);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							//System.out.println(e);
						}
						
						String query_conn = "SET GLOBAL max_connections = 2000;";
						
						Statement ss = null;
						try {
							ss = connect.createStatement();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							//System.out.println(e);
						}
						try {
							ss.execute(query_conn);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							//System.out.println(e);
						}
								
						String query_1 = "INSERT INTO Time (Day, Week, Quarter, Month, Year)"
							    + " VALUES (?, ?, ?, ?, ?)";
						String query_2 = "INSERT INTO Customers (Customer_ID, Customer_Name, Gender)"
							    + " VALUES (?, ?, ?)";
						String query_3 = "INSERT INTO Products (Product_ID, Product_Name, Product_Price)"
								+ "VALUE (?, ?, ?)";
						String query_4 = "INSERT INTO Store (Store_ID, Store_Name)"
								+ "VALUE (?, ?)";
						String query_5 = "INSERT INTO Supplier (Supplier_ID, Supplier_Name)"
								+ "VALUE (?, ?)";
						String query_6 = "INSERT INTO Sales (Customer_ID, Product_ID, Time_ID, Store_ID, Supplier_ID, Quantity, Sale) VALUES (?, ?, ?, ?, ?, ?, ?)";
						
						PreparedStatement preparedSTATE_1 = null;
						try {
							preparedSTATE_1 = connect.prepareStatement(query_1, Statement.RETURN_GENERATED_KEYS);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							//System.out.println(e);
						}
						
						try {
							preparedSTATE_1.setInt(1, days);
							preparedSTATE_1.setInt(2, weeks);
							preparedSTATE_1.setInt(3, quarter);
							preparedSTATE_1.setInt(4, months);
							preparedSTATE_1.setInt(5, years);
							
							preparedSTATE_1.executeUpdate();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							//System.out.println(e);
						}
						
						try {
							PreparedStatement preparedSTATE_2 = connect.prepareStatement(query_2);
							
							preparedSTATE_2.setInt(1, CustomerID);
							preparedSTATE_2.setString(2, CustomerName);
							preparedSTATE_2.setString(3, String.valueOf(Gender));
							
							preparedSTATE_2.executeUpdate();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							//System.out.println(e);
						}
						
						try {
							PreparedStatement preparedSTATE_3 = connect.prepareStatement(query_3);
							
							preparedSTATE_3.setInt(1, productID);
							preparedSTATE_3.setString(2, ProductName);
							preparedSTATE_3.setFloat(3, ProductPrice);
							
							preparedSTATE_3.executeUpdate();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							//System.out.println(e);
						}
						
						try {
							PreparedStatement preparedSTATE_4 = connect.prepareStatement(query_4);
							
							preparedSTATE_4.setInt(1, StoreID);
							preparedSTATE_4.setString(2, StoreName);
							
							preparedSTATE_4.executeUpdate();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							//System.out.println(e);
						}
						
						try {
							PreparedStatement preparedSTATE_5 = connect.prepareStatement(query_5);
							
							preparedSTATE_5.setInt(1, SupplierID);
							preparedSTATE_5.setString(2, SupplierName);
							
							preparedSTATE_5.executeUpdate();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							//System.out.println(e);
						}
						
						try {
							ResultSet TimeGen = preparedSTATE_1.getGeneratedKeys();
							
							while (TimeGen.next())
							{
								PreparedStatement preparedSTATE_6 = connect.prepareStatement(query_6);
								
								preparedSTATE_6.setInt(1, CustomerID);
								preparedSTATE_6.setInt(2, productID);
								preparedSTATE_6.setInt(3, TimeGen.getInt(1));
								preparedSTATE_6.setInt(4, StoreID);
								preparedSTATE_6.setInt(5, SupplierID);
								preparedSTATE_6.setInt(6, Quantity_O);
								preparedSTATE_6.setFloat(7, ProductPrice * Quantity_O);
								
								preparedSTATE_6.executeUpdate();
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							//System.out.println(e);
						}
						
						
						
						try {
							connect.close();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							//System.out.println(e);
						}
						
						
						System.out.println("Product ID: " + productID + ", " + "Day: " + days + ", " + "Week: " + weeks + ", " + "Month: " + months + ", " + "Quarter: " + quarter + ", " + "Year: " + years + ", " + "Order ID: " + OrderID + ", "
								+ "Customer ID: " + CustomerID + ", " + "Customer Name: " + CustomerName + ", " + "Gender: " + Gender + ", " + "Quantity Ordered: " + Quantity_O + ", " + "Product Name: " + ProductName + ", "
								+ "Product Price: " + ProductPrice + ", " + "Supplier ID: " + SupplierID + ", " + "Supplier Name: " + SupplierName + ", " + "Store ID: " + StoreID + ", " + "Store Name: " + StoreName);
						System.out.println();
						getVals.removeAll(getVals);
					}
				}
				map.remove(values);
				iter.remove();
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			//System.out.println(map.size());
//			if (map.size() < 1000)
//			{
//				int sleepp = 100 + map.size();
//				Controller Control = new Controller(Consumer, url1, usr, pass, sleepp);
//				Control.start();
//				
//				try {
//					Recieve = Consumer.take();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				streamQueue.enqueue(Recieve.get(0));
//				
//				map.put(Recieve.remove(0), Recieve);
//			}
		}	
	}
	
	public void connection() {
//		String url = "jdbc:mysql://localhost:3306/master_data";
//		String Username = "root";
//		String password = "root";
		
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			Connection connect = DriverManager.getConnection(url, usr, pass);
			Statement statement = connect.createStatement();
			
			DEE = streamQueue.dequeue();
			
			String query = "Select * from ((Select * from Master_data where `Product ID` = " + DEE + ") UNION (Select * from Master_data where `Product ID` > " + DEE + ")) as result1 limit 10";
			
			setMaster_rs(statement.executeQuery(query));
			
			int i=0;
			while(Master_rs.next())
			{
				ArrayList<String> Row = new ArrayList<>();
				Row.add(Master_rs.getString("Product ID"));
				Row.add(Master_rs.getString("Product Name"));
				Row.add(Master_rs.getString("Product Price"));
				Row.add(Master_rs.getString("Supplier ID"));
				Row.add(Master_rs.getString("Supplier Name"));
				Row.add(Master_rs.getString("Store ID"));
				Row.add(Master_rs.getString("Store Name"));
				RESS.add(i, Row);
				i++;
			}
			
			connect.close();
		}
		catch (Exception e)
		{
		}
	}

	public ResultSet getMaster_rs() {
		return Master_rs;
	}

	public void setMaster_rs(ResultSet master_rs) {
		Master_rs = master_rs;
	}

	public String getUrl1() {
		return url1;
	}

	public void setUrl1(String url1) {
		this.url1 = url1;
	}
}
