import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * This part of the program is responsible for requesting jobs from the LoadBalancer program
 * to request a job to be executed by the load balancer, follow the following convention of messages:
 * word <white_space> <integer_duration_in_seconds> (e.g. "do 13", "execute 5")
 * it's important that you follow that convention when sending requests to the LoadBalancer or the program will not work correctly
 */
public class JobSender {
    public static void main(String[] args) {
        try{
            Integer port = 3881;
            String address = "127.0.0.1";

            if(args.length == 2){
                address = args[0];
                port = Integer.parseInt(args[1]);
            }

            String jobSenderID = "" + System.currentTimeMillis();
            System.out.println("================================================================");
            System.out.println("JobSender started.");
            System.out.println("Registered with JobSender ID: " + jobSenderID);
            System.out.println("Target LoadBalancer address: " + address + ":" + port);
            System.out.println("================================================================\n");

            Socket socket = new Socket(address, port);
            PrintWriter sender = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            sender.println("Connection from Job Sender with ID: " + jobSenderID);
            while(true){
                String fromLoadBalancer = reader.readLine();
                System.out.println("Message from LoadBalancer: " + fromLoadBalancer);

                Scanner scanner = new Scanner(System.in); // format: word <white_space> <integer_duration_in_seconds> (e.g. "do 13", "execute 5")
                String message = scanner.nextLine();

                sender.println(message);

                fromLoadBalancer = reader.readLine(); 
                System.out.println("Message from LoadBalancer: " + fromLoadBalancer);
                
                if(fromLoadBalancer.contains("dropped")) // continue if the load balancer couldn't assign the job to a node
                    continue;
                else{ // otherwise, await response from load balancer with detailed information about the job (the node that executed it and the total duration)
                    fromLoadBalancer = reader.readLine(); 
                    System.out.println(fromLoadBalancer + "\n");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
