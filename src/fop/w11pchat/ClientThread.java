package fop.w11pchat;
import java.io.*;
import java.net.*;
import java.util.*;

public class ClientThread implements Runnable{
	
	Socket socket;
	PrintWriter output;
	Scanner input;
	Scanner send = new Scanner(System.in);
	
	String text = "";
	
	
	public ClientThread(Socket socket)
	{
		this.socket = socket;
	}

	public void run() {
		try {
			try {
					input = new Scanner(socket.getInputStream());
					output = new PrintWriter(socket.getOutputStream());
					output.flush();

					while(true)
					{
						if(input.hasNext())
						{
							text = input.next();
							System.out.print(text);
						}
						
						if(send.hasNext())
						{
							text = send.next();
						}
						
						
					}
				
				
				
			}
			finally
			{
				socket.close();
			}
		}
		catch(Exception x)
		{
			System.out.println(x);
		}
		
	}
	
	public void check()
	{
		while(true)
		{
			if(input.hasNext())
			{
				String message = input.next();
				if(message == "<s> Leaving Chat")
				{
					System.out.println("Left Chat");
					System.exit(0);
				}
			}
			
			
			
		}
	}

}
