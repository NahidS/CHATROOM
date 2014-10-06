import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	public static void main(String[] args) throws IOException {
		int clientNumber = 0;
		if(!checkArgs(args)) {
			return;
		}
		final int port = Integer.parseInt(args[0]);
		ServerSocket serverSocket = new ServerSocket(port);
		try {
			while(true) {
				new Capitalizer(serverSocket.accept(), clientNumber++).start();
			}
		} finally {
			serverSocket.close();
		}
	}

	/**
	 * Validate the provided arguments
	 * @param args - Input arguments 
	 * @return {@code true} if arguments are valid, otherwise {@code false}
	 */
	public static boolean checkArgs(String[] args) {
		if (args.length == 0) {
			System.out.println("Argument is not provided.");
			usage();
			return false;
		}
		try {
			Integer.parseInt(args[0]);
		} catch(NumberFormatException e) {
			System.out.println("Port number is not a valid number.");
			return false;
		}
		if (args.length > 1) {
			System.out.println("Extra arguements are ignored.");
		}
		return true;
	}
	
	/**
	 * Print the program usage on the screen
	 */
	public static void usage() {
		System.out.println("Usage: Server <port number>");
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
