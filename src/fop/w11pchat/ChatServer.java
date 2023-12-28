package fop.w11pchat;
import java.util.ArrayList;
import java.time.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.*;

public class ChatServer {
	
	// Vici racaa problema, dro ar maqvs, inglisuri maqvs 1 saatshi da mere gasasvleli var axali wlis ragaceebze, problemas vityvi: 
	// clientside while cikli ar ar aris bolomde damtavrebuli ris gamoc mudmivi inputis micema titqmis sheudzlebelia, sawiro aris
	// while ciklshi connection chekis gadatana,  chatservershi winaswar gamzadeba monawileebis stringis rom daidos egreve da ar wirdebodes ramdenime enteri
	// amas imitom vwer rom icodet rom dro ar mqonda testebis , matematikis da inglisuris davalebebisa da axali wlis gamo da 
	//magitom ar mushaobs kodi da ara ucodinrobis gamo, ras vaketeb da ra vqna vici magram ver vaswreb
	
	public static ArrayList<Socket> connections = new ArrayList<Socket>();
	public static ArrayList<String> users = new ArrayList<String>();
	public static ArrayList<LocalTime> times = new ArrayList<LocalTime>();
	
	public static void main(String[] args) throws IOException
	{
		String input = "";
		if(args.length == 0)
		{
			System.out.println("Creating Server:\n" 
					+ "1) To create a custom port enter an integer(positive or 0);\n"
					+ "2) To Create the default Port do not enter anything / enter 3000;");
			InputStream scanner = System.in;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(scanner));
			input = bufferedReader.readLine();
		}
		else
		{
			input = args[0];
		}

		int port = 3000;
		if(input != null)
		{
			try {
				port = Integer.parseInt(input);
				
			}
			catch(Exception x)
			{
				System.out.println("Creating Default Port(3000)");
			}
			
		}
		try {
			final int port1 = port;
			ServerSocket server = new ServerSocket(port1);
			System.out.println("Server Running, waiting for connections");
			
			while(true)
			{
				Socket socket = server.accept();
				connections.add(socket);
				times.add(LocalTime.now());
				
				Connect(socket);
				
				ServerThread clientHandler = new ServerThread(socket);
				Thread thread = new Thread(clientHandler);
				thread.start();
				
				
			}
		}
		catch(Exception x)
		{
			x.printStackTrace();
		}
	}
	
	public static void Connect(Socket x) throws Exception
	{
		Scanner scanner = new Scanner(x.getInputStream());
		String username = scanner.nextLine();
		users.add(username);
		for(int i = 0; i < connections.size(); i++)
		{
			Socket temp = connections.get(i);
			PrintWriter out = new PrintWriter(temp.getOutputStream());
			out.println("<system>" + username + " connected;\n");
			out.println("<system>" + "Currently Connected: " + users + "\n");
			out.flush();
			System.out.println(username + " Connected from: " + x.getLocalAddress().getHostName());
		}
	}

}
    
