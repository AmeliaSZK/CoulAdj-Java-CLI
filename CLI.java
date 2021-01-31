import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

public class CLI {
    public static final String DIAGONALS_OPTION = "--dont-relate-diagonals";
    public static final boolean DIAGONALS_DEFAULT = false;
    public static final String ERRMSG_DIAGONALS =
        "Error: With $d arguments, the 1st must be `%s`\n";
    public static final String ERRMSG_NBARGS =
        "Error: The program must be called with either 2 or 3 arguments.\n";

    public static void main(String args[]) {

        System.out.println("~~~~~ Starting ~~~~~");

        // # Start timer
        LocalDateTime startTime = LocalDateTime.now();

        // # Parse command line
        boolean dontRelateDiagonals = DIAGONALS_DEFAULT;
        String imageFilepath = ""; // Initialized to shut up the compiler
        String resultsFilepath = "";

        List<String> argv = Arrays.asList(args);
        switch (argv.size()) {
        case 3:
            if (argv.get(0).equals(DIAGONALS_OPTION)) {
                dontRelateDiagonals = true;
            } else {
                System.err.printf(ERRMSG_DIAGONALS, argv.size(),
                                  DIAGONALS_OPTION);
                System.exit(-1);
            }
            /* FALL THROUGH */
        case 2:
            int lastArgIndex = argv.size() - 1;
            int penultimateArgIndex = lastArgIndex - 1;
            imageFilepath = argv.get(penultimateArgIndex);
            resultsFilepath = argv.get(lastArgIndex);
            break;

        default:
            System.err.printf(ERRMSG_NBARGS);
            System.exit(-1);
            break;
        }
        System.out.println("dontRelateDiagonals = " + dontRelateDiagonals);
        System.out.println("imageFilepath = " + imageFilepath);
        System.out.println("resultsFilepath = " + resultsFilepath);

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