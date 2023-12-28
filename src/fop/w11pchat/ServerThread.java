package fop.w11pchat;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ServerThread implements Runnable {
	
	Socket socket;
	private Scanner input;
	private PrintWriter output;
	String text = "";
	String user = "";
	
	
	public ServerThread(Socket socket)
	{
		this.socket = socket;
	}

	public void connection() throws Exception
	{
		int index = 0;
		if(!socket.isConnected())
		{
			for(int i = 0; i < ChatServer.connections.size();i++)
			{
				if(ChatServer.connections.get(i) == socket)
				{
					ChatServer.connections.remove(i);
					ChatServer.users.remove(i);
					ChatServer.times.remove(i);
					index = i;
				}
			}
			
			for(int i = 0; i < ChatServer.connections.size(); i++)
			{
				Socket temp = ChatServer.connections.get(i);
				PrintWriter out = new PrintWriter(temp.getOutputStream());
				String users = ChatServer.users.toString();
				out.println("<system>" + ChatServer.users.get(index) + " disconnected;");
				out.println("<system> Currently Connected: " + users);
				out.flush();
				System.out.println(socket.getLocalAddress().getHostName() + " disconnected");
			}
		}
	}
	
	public void run() {
		try {
			try {
				System.out.println("Server Reacher");
				
				connection();
				
				input = new Scanner(socket.getInputStream());
				output = new PrintWriter(socket.getOutputStream());
				
				output.println("Server reached");
				for(int i = 0; i < ChatServer.connections.size();i++ )
				{
					if(ChatServer.connections.get(i) == socket)
					{
						user = ChatServer.users.get(i);
					}
				}
				
				while(true)
				{
					
					text = input.nextLine();
					System.out.println(user + " wrote: " + text);
					
					if(text.substring(0,1) == "@")
					{
						String recip = text.substring(1, text.indexOf(' '));
						String message = text.substring(text.indexOf(' ')+1);
						for(int i = 0; i < ChatServer.users.size(); i ++)
						{
							if(recip == ChatServer.users.get(i))
							{
								Socket temps = ChatServer.connections.get(i);
								PrintWriter tempo = new PrintWriter(temps.getOutputStream());
								tempo.println("> "+ user + ": " + message);
								tempo.flush();
								System.out.println(user + " sent to " + recip);
							}
						}
					}
					else if(text.substring(0, 6) == "WHOIS")
					{
						for(int i = 0; i < ChatServer.connections.size(); i++)
						{
							Socket temp = ChatServer.connections.get(i);
							PrintWriter tempo = new PrintWriter(temp.getOutputStream());
							for(int j = 0; j < ChatServer.users.size(); j++)
							{
								tempo.println("User: " + ChatServer.users.get(j) + "; Connection time: " + ChatServer.times.get(j));
								tempo.flush();
							}
							System.out.println(user + " asked for a penguin fact");
						}
					}
					else if(text.substring(0, 7) == "LOGOUT")
					{
						output.println("<s> Leaving Chat");
						socket.close();
						System.exit(0);
					}
					else if(text.substring(0, 6) == "PINGU")
					{
						for(int i = 0; i < ChatServer.connections.size(); i++)
						{
							Socket temp = ChatServer.connections.get(i);
							PrintWriter tempo = new PrintWriter(temp.getOutputStream());
							tempo.println("All 17 species of penguin are only found in the souther hemisphere");
							tempo.flush();
							System.out.println(user + " asked for a penguin fact");
						}
					}
					else
					{
						for(int i = 0; i < ChatServer.connections.size();i++ )
						{
							Socket temps = ChatServer.connections.get(i);
							PrintWriter tempo = new PrintWriter (temps.getOutputStream());
							tempo.println(user + ": " + text);
							tempo.flush();
							
						}
					}
					
				}
			}
			finally
			{
				socket.close();
			}
		}
		catch(Exception x){
			System.out.print(x);
		}
	}

}
