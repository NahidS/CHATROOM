import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;


public class Client {
	public static void main(String[] args) throws IOException {
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);
		String sentence;
		
		try {
			Socket client = new Socket(hostname, port);
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			PrintStream outToServer = new PrintStream(client.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			while(true) {
				sentence = inFromUser.readLine();
				outToServer.println(sentence);
				System.out.println("echo: " + inFromServer.readLine());
				
				if(sentence.equals("bye"))
					break;
			}
			client.close();
		} catch(IOException e) {
			System.out.println(e);
		}
	}
}
