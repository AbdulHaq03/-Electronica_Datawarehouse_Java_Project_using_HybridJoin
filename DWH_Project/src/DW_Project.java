import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.*;
import java.util.*;

public class DW_Project {

	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		
		BlockingQueue<ArrayList<String>> queue = new LinkedBlockingQueue<>();
		
		try (Scanner sc = new Scanner(System.in)) {
			System.out.println("Enter the url for transactions: ");
			String url = sc.nextLine();
			
			System.out.println("Enter the url for master data: ");
			String url1 = sc.nextLine();
			
			System.out.println("Enter the username: ");
			String usr = sc.nextLine();
			
			System.out.println("Enter the password: ");
			String pass = sc.nextLine();
			
			sc.close();
			
			StreamGenerator Thread1 = new StreamGenerator(queue, url, usr, pass);
			HybridJoin Thread2 = new HybridJoin(queue, url1, usr, pass, url);
			
			Thread1.start();
			Thread2.start();		}
		
	}

}
