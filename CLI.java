import java.time.LocalDateTime;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class CLI {
    public static void main(String args[]) {
        System.out.println("~~~~~ Starting ~~~~~");

        // # Start timer
        LocalDateTime startTime = LocalDateTime.now();

        // # Parse command line

        // # Open the files

        // # Convert to BufferedImage

        // # Invoke ColourAdjacencies

        // # Write results to file

        // # End timer and report duration
        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        int seconds = duration.toSecondsPart();
        long minutes = duration.truncatedTo(ChronoUnit.MINUTES).toMinutes();
        System.out.printf("%d:%02d\n", minutes, seconds);

        return;
    }
}