import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class JobSender {
    public static void main(String[] args) {
        try{
            Socket socket = new Socket("127.0.0.1", 3881);
            PrintWriter sender = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            sender.println("Connection from Job Sender");
            while(true){
                String response = reader.readLine();
                System.out.println("Message from LoadBalancer: " + response);

                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();

                sender.println(message);

                response = reader.readLine();
                System.out.println("Message from LoadBalancer: " + response);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
