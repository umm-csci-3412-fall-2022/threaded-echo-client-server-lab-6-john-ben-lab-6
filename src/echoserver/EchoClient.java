package echoserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class EchoClient {

        public static final int PORT_NUMBER = 6013;

	public static void main(String[] args) throws IOException, InterruptedException {
		EchoClient client = new EchoClient();
		client.start();
	}

	private void start() throws IOException, InterruptedException {
		Socket socket = new Socket("localhost", PORT_NUMBER);

		InputReader iReader = new InputReader(socket);
                OutputWriter oWriter = new OutputWriter(socket);

                Thread inputThread = new Thread(iReader);
                Thread outputThread = new Thread(oWriter);

                inputThread.start();
                outputThread.start();



	}

        private class InputReader implements Runnable {
          Socket socket;

          public InputReader(Socket s) {
            socket = s;
          }

          @Override
          public void run() {
            try {
              InputStream input = socket.getInputStream();
              while (!socket.isInputShutdown()) {
                System.out.write(input.read());
              }

              System.out.write(input.readAllBytes());

            } catch (IOException e) {
              System.out.println("Caught an unhandled exception: ");
              System.out.println(e);
            } finally {
              try {
                socket.close();
              } catch (IOException e) {
                System.out.println("Caught an unhandled exception while trying to close the socket: ");
                System.out.println(e);
              }
            }
          }
        }

        private class OutputWriter implements Runnable {
          Socket socket;

          public OutputWriter(Socket s) {
            socket = s;
          }

          @Override
          public void run() {

            try {
              OutputStream output = socket.getOutputStream();
              int nextByte;
              //Continually process bytes from System.in until EOF
              //EOF is represented by -1
              while ((nextByte = System.in.read()) != -1) {
                output.write(nextByte);
              }

              output.flush();
            } catch (IOException e) {
              System.out.println("Caught an unhandled exception: ");
              System.out.println(e);
            } finally {
              try {
                socket.shutdownOutput();
              } catch (IOException e) {
                System.out.println("Caught an unhandled exception while trying to shut down the output: ");
                System.out.println(e);
              }
            }
          }
        }
}
