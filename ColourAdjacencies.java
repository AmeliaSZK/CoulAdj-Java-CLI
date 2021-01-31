import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ColourAdjacencies {
    public static final boolean DIAGONALS_DEFAULT = false;
    private static final String DELIMITER = "\t";

    /* DO NOT CHANGE THESE HEADERS
     *
     * These headers are part of the API defined in the Readme.
     * They MUST NOT be changed unless the major version number is incremented.
     *
     * The outputted files are meant to be parsed by programs that rely on
     *    hardcoded column names.
     *
     * THE NAMES OF THE COLUMNS, AND THE ORDER IN WHICH THEY ARE WRITTEN,
     *    ARE THE MOST CRITICAL PART OF THE API.
     *
     * DO NOT CHANGE
     *
     * DO NOT CHANGE
     */
    private static final String[] RGB_COLUMNS = {"r",     "g",     "b",
                                                 "adj_r", "adj_g", "adj_b"};
    private static final String[] RGBALPHA_COLUMNS = {
        "r", "g", "b", "a", "adj_r", "adj_g", "adj_b", "adj_a"};

    @SuppressWarnings("unused") // API says images sans alpha always get 255
    private static final String RGB_HEADER =
        String.join(DELIMITER, RGB_COLUMNS);
    private static final String RGBALPHA_HEADER =
        String.join(DELIMITER, RGBALPHA_COLUMNS);

    private final BufferedImage image;
    private final boolean dontRelateDiagonals;
    private final boolean diagonalsAreRelated;
    private final List<String> lines;
    private final ColorModel cm;
    private final Map<Integer, Set<Integer>> adjacencies;
    private final int maxX;
    private final int maxY;
    private final boolean hasAlpha;
    private final Comparator<Integer> comparator;
    private final String header = RGBALPHA_HEADER; //

    private boolean adjacenciesAreComputed = false;
    private boolean linesAreComputed = false;

    public ColourAdjacencies(BufferedImage image, boolean dontRelateDiagonals) {
        this.image = image;
        this.dontRelateDiagonals = dontRelateDiagonals;
        cm = image.getColorModel();
        maxX = image.getWidth() - 1;
        maxY = image.getHeight() - 1;
        hasAlpha = cm.hasAlpha();
        comparator = hasAlpha ? new SortByRgbAlpha() : new SortByRgb();
        adjacencies = new TreeMap<>(comparator);
        lines = new ArrayList<>();
        diagonalsAreRelated = this.dontRelateDiagonals ? false : true;
    }

    public void computeAdjacencies() {
        if (adjacenciesAreComputed) {
            return;
        }

        for (int x = 0; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                evaluateAllNeighbours(x, y);
            }
        }

        adjacenciesAreComputed = true;
        return;
    }

    private void evaluateAllNeighbours(int x, int y) {
        evaluateOneNeighbour(x, y, +1, 0);
        evaluateOneNeighbour(x, y, 0, +1);
        evaluateOneNeighbour(x, y, 0, -1);
        evaluateOneNeighbour(x, y, -1, 0);

        if (diagonalsAreRelated) {
            evaluateOneNeighbour(x, y, +1, +1);
            evaluateOneNeighbour(x, y, +1, -1);
            evaluateOneNeighbour(x, y, -1, +1);
            evaluateOneNeighbour(x, y, -1, -1);
        }

        return;
    }

    private void evaluateOneNeighbour(int x, int y, int xOffset, int yOffset) {
        final int pixelX = x;
        final int pixelY = y;
        final int neighX = x + xOffset;
        final int neighY = y + yOffset;

        if (!validXY(neighX, neighY)) {
            return;
        }

        final int pixel = image.getRGB(pixelX, pixelY);
        final int neigh = image.getRGB(neighX, neighY);

        if (pixel != neigh) {
            register(pixel, neigh);
        }

        return;
    }

    private boolean validXY(int x, int y) {
        final boolean isValidX = 0 <= x && x <= maxX;
        final boolean isValidY = 0 <= y && y <= maxY;
        return isValidX && isValidY;
    }

    private void register(int pixel, int neigh) {
        adjacencies.putIfAbsent(pixel, new TreeSet<>(comparator));
        adjacencies.get(pixel).add(neigh);
    }

    public void computeLines() {
        if (linesAreComputed) {
            return;
        } else if (!adjacenciesAreComputed) {
            computeAdjacencies();
        }

        lines.add(header);
        for (Map.Entry<Integer, Set<Integer>> entry : adjacencies.entrySet()) {
            final int pixel = entry.getKey();
            final Set<Integer> allNeighbours = entry.getValue();
            for (final int neigh : allNeighbours) {
                final String adjacency = formatAdjacency(pixel, neigh);
                lines.add(adjacency);
            }
        }

        linesAreComputed = true;
        return;
    }

    private String formatAdjacency(int pixel, int neigh) {
        Color pixelColour = new Color(pixel, hasAlpha);
        Color neighColour = new Color(neigh, hasAlpha);

        final int r = pixelColour.getRed();
        final int g = pixelColour.getGreen();
        final int b = pixelColour.getBlue();
        final int a = hasAlpha ? pixelColour.getAlpha() : 255;

        final int adj_r = neighColour.getRed();
        final int adj_g = neighColour.getGreen();
        final int adj_b = neighColour.getBlue();
        final int adj_a = hasAlpha ? neighColour.getAlpha() : 255;

        // Based https://stackoverflow.com/a/38425624 by spirit
        final int[] components = {r, g, b, a, adj_r, adj_g, adj_b, adj_a};
        final String result = Arrays.stream(components)
                                  .mapToObj(String::valueOf)
                                  .collect(Collectors.joining(DELIMITER));

        return result;
    }

    public List<String> toLines() {
        if (!linesAreComputed) {
            computeLines();
        }
        return lines;
    }

    class SortByRgb implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            final Color a2 = new Color(a, hasAlpha);
            final Color b2 = new Color(b, hasAlpha);
            if (a2.getRed() != b2.getRed()) {
                return a2.getRed() - b2.getRed();

            } else if (a2.getGreen() != b2.getGreen()) {
                return a2.getGreen() - b2.getGreen();

            } else {
                return a2.getBlue() - b2.getBlue();
            }
        }
    }

    class SortByRgbAlpha implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            final Color a2 = new Color(a, hasAlpha);
            final Color b2 = new Color(b, hasAlpha);
            if (a2.getRed() != b2.getRed()) {
                return a2.getRed() - b2.getRed();

            } else if (a2.getGreen() != b2.getGreen()) {
                return a2.getGreen() - b2.getGreen();

            } else if (a2.getBlue() != b2.getBlue()) {
                return a2.getBlue() - b2.getBlue();

            } else {
                return a2.getAlpha() - b2.getAlpha();
            }
        }
    }
}
