import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientDemo {

	public static void main(String[] args) {
		try(Socket socket = new Socket("localhost", 1553);
			PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
			Scanner scanner = new Scanner(socket.getInputStream());){

			new Thread(() -> {
				Scanner userInput = new Scanner(System.in);
				while(socket.isConnected()){
					pw.println(userInput.nextLine());
				}
			}).start();
			while(socket.isConnected()){
					System.out.println(scanner.nextLine());
			}
		}catch(NoSuchElementException | IOException ns){
			if(ns instanceof NoSuchElementException){
				System.out.println("Disconnected!");
			}
		}
	}

}