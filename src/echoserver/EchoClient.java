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

		InputReader iReader = new InputReader(socket.getInputStream());
                OutputWriter oWriter = new OutputWriter(socket.getOutputStream());

                Thread inputThread = new Thread(iReader);
                Thread outputThread = new Thread(oWriter);

                inputThread.start();
                outputThread.start();

                outputThread.join();
                socket.shutdownOutput();
                inputThread.join();
                socket.close();
	}

        private class InputReader implements Runnable {
          InputStream input;

          public InputReader(InputStream i) {
            input = i;
          }

          @Override
          public void run() {
            try {
              int nextByte;
              //Continually process bytes from input until EOF
              //EOF is represented by -1
              while ((nextByte = input.read()) != -1) {
                System.out.write(nextByte);
              }

              System.out.write(input.readAllBytes());

            } catch (IOException e) {
              System.out.println("Caught an unhandled exception: ");
              System.out.println(e);
            }
          }
        }

        private class OutputWriter implements Runnable {
          OutputStream output;

          public OutputWriter(OutputStream os) {
            output = os;
          }

          @Override
          public void run() {

            try {
              int nextByte;
              while ((nextByte = System.in.read()) != -1) {
                output.write(nextByte);
              }

              output.flush();
            } catch (IOException e) {
              System.out.println("Caught an unhandled exception: ");
              System.out.println(e);
            }
          }
        }
}
