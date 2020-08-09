import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class JobExecutor {
    String nodeID;
    private Socket _socket;
    private PrintWriter writer;
    private BufferedReader reader;

    JobExecutor(Socket socket, String nodeID) throws Exception {
        this.nodeID = nodeID;
        this._socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
        this.writer = new PrintWriter(_socket.getOutputStream(), true);
    }

    public void handleRequest() throws Exception {
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

    private void executeJob(int seconds) throws InterruptedException {
        Thread.sleep(1000 * seconds);
        System.out.println("Job has been executed.\n");
    }
}
