package fop.w11pchat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.io.*;
import java.util.*;

public class ChatClient {
	
	
	
	public static void main(String[] args) throws Exception
	{
		int port = 3000;
		String hostname = "localhost";
		String input = "";
		
		
		if(args.length == 0)
		{
			System.out.println("CConnecting to server:\n" 
					+ "1) To connect to a custom server enter <Adress>:<port>;\n"
					+ "2) To connect to the default server do not enter anything / enter localhost:3000;");
			InputStream scanner = System.in;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(scanner));
			input = bufferedReader.readLine();
		}
		else
		{
			input = args[0];
			if(input != null || input != "")
			{
				hostname = input.substring(0, input.indexOf(':'));
				port = Integer.parseInt(input.substring(input.indexOf(':')+1));
			}
		}
		

		try{
			Socket socket = new Socket(hostname,port);
			System.out.println("Connected to " + hostname + " port: " + port);
			
			System.out.println("Enter Username: ");
			InputStream scanner = System.in;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(scanner));
			input = bufferedReader.readLine();
			
			PrintWriter output = new PrintWriter(socket.getOutputStream());
			output.println(input);
			output.flush();
			
			ClientThread x = new ClientThread(socket);
			Thread y = new Thread(x);
			y.start();
			
			System.out.println("reached");
			
			
			String text = ""; 
			
			while(true)
			{
				text = bufferedReader.readLine();
				if(text != null)
				{
					output.println(text);
					output.flush();
				}
			}
			
				
			
		}
		catch(Exception x)
		{
			System.out.println("Server not repsonding: "
					+ x);
			System.exit(0);
		} 
	}
    
}
