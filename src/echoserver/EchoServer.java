package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class EchoServer {

	public static final int PORT_NUMBER = 6013;
	public static void main(String[] args) throws IOException, InterruptedException {
		EchoServer server = new EchoServer();
		server.start();
	}

	private void start() throws IOException, InterruptedException {
		ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
                ExecutorService pool = Executors.newCachedThreadPool();
		while (true) {
			Socket socket = serverSocket.accept();

                        pool.submit(new ConnectionHandler(socket));
		}
	}

        private class ConnectionHandler implements Runnable {
          Socket socket;
          InputStream input;
          OutputStream output;

          public ConnectionHandler(Socket s) {
            socket = s;
            try {
              input = socket.getInputStream();
              output = socket.getOutputStream();
            } catch (IOException ioe) {
              System.out.println("Caught an unexpected exception: ");
              System.out.println(ioe);
            }
          }

          @Override
          public void run() {
            try {
              input.transferTo(output);

              //Finish with cleanup.
              input.close();
              output.flush();
              output.close();
              socket.close();
            } catch (IOException ioe) {
              System.out.println("Caught an unexpected exception: ");
              System.out.println(ioe);
            }
          }
        }
}
