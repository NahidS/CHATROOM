import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	public static void main(String[] args) throws IOException {
		int clientNumber = 0;
		checkArgs(args);
		int port = Integer.parseInt(args[0]);
		
		ServerSocket serverSocket = new ServerSocket(port);
		
		try {
			while(true) {
				new Capitalizer(serverSocket.accept(), clientNumber++).start();
			}
		} finally {
			serverSocket.close();
		}
	}

	public static boolean checkArgs(String[] args) {
		if (args.length == 0) {
			System.out.println("Forgot the arguments?");
			return false;
		}
		return true;
	}
	
	private static class Capitalizer extends Thread {
		private Socket socket;
		private int clientNumber;
		
		public Capitalizer(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;
			System.out.println("Connecting to client number " + clientNumber + " at " + socket);
		}
		
		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintStream out = new PrintStream(socket.getOutputStream());
				
				while(true) {
					String input = in.readLine();
					System.out.println("Received: " + input);
					
					if(input == null || input.equals("."))
						break;
					out.println(input.toUpperCase());
				}
			} catch(IOException e) {
				System.out.println("Client nubmer " + clientNumber + ": " + e);
			} finally {
				try {
					socket.close();
				} catch(IOException e) {
					System.out.println("Error with closing a socket");
				}
				System.out.println("Connection with client number " + clientNumber + " closed.");
			}
			
		}
	}
}
