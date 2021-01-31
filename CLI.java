import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;

public class CLI {
    public static final String DIAGONALS_OPTION = "--dont-relate-diagonals";

    public static final String ERRMSG_DIAGONALS =
        "Error: With $d arguments, the 1st must be `%s`\n";
    public static final String ERRMSG_NBARGS =
        "Error: The program must be called with either 2 or 3 arguments.\n";
    public static final String ERRMSG_IMAGE_FILE_CANREAD =
        "Error: The file `%s` cannot be read.\n";
    public static final String ERRMSG_RESULTS_FILE_CANWRITE =
        "Error: The file `%s` cannot be modified.\n";
    public static final String ERRMSG_IMAGE_NULL =
        "Error: The file `%s` couldn't be parsed as an image.\n";

    public static void main(String args[]) {
        // # Start timer
        System.out.println("~~~~~ Starting ~~~~~");
        LocalDateTime startTime = LocalDateTime.now();

        // # Parse command line
        boolean dontRelateDiagonals = ColourAdjacencies.DIAGONALS_DEFAULT;
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

        try {
            // # Open the files
            File imageFile = new File(imageFilepath);
            File resultsFile = new File(resultsFilepath);

            resultsFile.createNewFile();
            if (!imageFile.canRead()) {
                System.err.printf(ERRMSG_IMAGE_FILE_CANREAD,
                                  imageFile.getAbsolutePath());
                System.exit(-1);
            }
            if (!resultsFile.canWrite()) {
                System.err.printf(ERRMSG_RESULTS_FILE_CANWRITE,
                                  resultsFile.getAbsolutePath());
                System.exit(-1);
            }

            // # Convert to BufferedImage
            BufferedImage image = ImageIO.read(imageFile);
            System.out.println(image);

            if (image == null) {
                System.err.printf(ERRMSG_IMAGE_NULL, imageFile);
                System.exit(-1);
            }

            // # Invoke ColourAdjacencies
            ColourAdjacencies adjacencies =
                new ColourAdjacencies(image, dontRelateDiagonals);
            adjacencies.computeAdjacencies();
            adjacencies.computeLines();

            // # Write results to file
            List<String> lines = adjacencies.toLines();

            Files.write(resultsFile.toPath(), lines);

        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(-1);
        }

        // # End timer and report duration
        LocalDateTime endTime = LocalDateTime.now();
        Duration duration = Duration.between(startTime, endTime);
        int seconds = duration.toSecondsPart();
        long minutes = duration.truncatedTo(ChronoUnit.MINUTES).toMinutes();
        System.out.printf("%d:%02d\n", minutes, seconds);

        return;
    }
}