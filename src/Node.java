import java.io.PrintWriter;
import java.net.Socket;


public class Node {
    public static void main(String[] args) {
        try{
            Integer port = 3881;
            String address = "127.0.0.1";

            if(args.length == 2){
                address = args[0];
                port = Integer.parseInt(args[1]);
            }

            String nodeID = "" + System.currentTimeMillis();

            System.out.println("================================================================");
            System.out.println("Worker node started.");
            System.out.println("Registered with worker node ID: " + nodeID);
            System.out.println("Target LoadBalancer address: " + address + ":" + port);
            System.out.println("================================================================\n");

            Socket socket = new Socket(address, port);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);


            // Register as ready
            writer.println("Connection from node with ID: " + nodeID);

            JobExecutor executor = new JobExecutor(socket, nodeID);
            executor.handleRequest(); // wait for job requests and execute

        }
        catch (Exception e){ e.printStackTrace(); }
    }
}
