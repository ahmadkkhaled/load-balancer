import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Node {
    public static void main(String[] args) {
        try{
            Socket socket = new Socket("127.0.0.1", 3881);

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String nodeID = "" + System.currentTimeMillis();

            // Register as ready
            writer.println("Connection from node with ID: " + nodeID);

            // wait for job request
            while(true){
                String request = reader.readLine(); // e.g. do 13
                int jobDuration = Integer.parseInt(request.split(" ")[1]);
                System.out.println("Job received from load balancer with duration: " + jobDuration + " second(s).");

                // execute job
                executeJob(jobDuration);

                // Register as ready again
                writer.println("Node with ID " + nodeID + " finished executing job.");
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void executeJob(int seconds) throws InterruptedException {
        Thread.sleep(1000 * seconds);
        System.out.println("Job has been executed.\n");
    }
}
