package assignment3;

import java.io.*;
import java.net.*;

public class EchoServer {

	private ServerSocket server;
	private Socket client;
	private int port = 8998;
	
	private class ClientConnThread extends Thread {
		
		private DataOutputStream out;
		private InputStream in;
		private ByteArrayOutputStream buffer;
		private boolean connection = true;
		private Socket client;
		
		/*
		 * ClientConnThread
		 * 
		 * @param Socket client - the client connection socket
		 */
		
		ClientConnThread (Socket client) {
			
			try {
				// Get in and out streams + setup a buffer
				in = client.getInputStream();
				out = new DataOutputStream(client.getOutputStream());
				buffer = new ByteArrayOutputStream();
				
				this.start();
			}
			
			catch (SocketException e) {
				System.out.print("[Client connection]: Connection Reset for " + client.hashCode());
			} catch (EOFException e) {
				System.out.println("[Client connection]: Client Connection Closed." + client.hashCode());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		/*
		 * run()
		 * 
		 * This is the logic that we will run for each client
		 * connection that the Server starts.
		 * 
		 * @see java.lang.Thread#run()
		 */
		
		public void run () {
			try {
				while (connection) {
					// Read messages from client
					connection = echoMessages(buffer, in, out) ? true : false;
				}
				
			} catch (IOException e) {
				System.out.println("[Server]: Client closed connection removed.");
			}
		}
	}
	
	public boolean echoMessages (ByteArrayOutputStream buffer, InputStream in, DataOutputStream out) throws IOException {
		int nRead;
		byte[] data = new byte[16384];

		if ((nRead = in.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		} else if ((nRead = in.read(data, 0, data.length)) == -1) {
			return false;
		}

		buffer.flush();

		byte[] byteMsg = buffer.toByteArray();

		String msg = new String(byteMsg, "UTF-8");

		System.out.println("[Server]: Message received: " + msg);

		// Output the message to the output stream
		out.write(byteMsg);
		out.flush();
		buffer.reset();
		return true;
	}

	public EchoServer() {
		try {
			// Create socket server
			server = new ServerSocket(port);
			System.out.println("[Server]: Started. Waiting for connections on port " + port);
			
			// Endlessly accept new connections
			while(true) {
				// Start a new thread
				new ClientConnThread(server.accept());
				System.out.println("[Server]: A new client just connected.");
			}
			
			
		} catch (SocketException e) {
			System.out.print("[Server]: Connection Reset");
			System.exit(1);
		} catch (EOFException e) {
			System.out.println("[Server]: Client Connection Closed.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new EchoServer();
	}

}