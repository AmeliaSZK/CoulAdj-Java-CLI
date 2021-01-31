import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
    private final String header;

    private boolean adjacenciesAreComputed = false;
    private boolean linesAreComputed = false;

    public ColourAdjacencies(BufferedImage image) {
        this.image = image;
        dontRelateDiagonals = DIAGONALS_DEFAULT;
        cm = image.getColorModel();
        maxX = image.getWidth() - 1;
        maxY = image.getHeight() - 1;
        hasAlpha = cm.hasAlpha();
        comparator = hasAlpha ? new SortByRgbAlpha() : new SortByRgb();
        adjacencies = new TreeMap<>(comparator);
        lines = new ArrayList<>();
        diagonalsAreRelated = dontRelateDiagonals ? false : true;
        header = hasAlpha ? RGBALPHA_HEADER : RGB_HEADER;
    }

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
        diagonalsAreRelated = dontRelateDiagonals ? false : true;
        header = hasAlpha ? RGBALPHA_HEADER : RGB_HEADER;
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
        
        // TODO

        linesAreComputed = true;
        return;
    }

    public List<String> toLines() {
        if (!linesAreComputed) {
            computeLines();
        }
        return lines;
    }

    class SortByRgb implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            if (cm.getRed(a) != cm.getRed(b)) {
                return cm.getRed(a) - cm.getRed(b);

            } else if (cm.getGreen(a) != cm.getGreen(b)) {
                return cm.getGreen(a) - cm.getGreen(b);

            } else {
                return cm.getBlue(a) - cm.getBlue(b);
            }
        }
    }

    class SortByRgbAlpha implements Comparator<Integer> {
        public int compare(Integer a, Integer b) {
            if (cm.getRed(a) != cm.getRed(b)) {
                return cm.getRed(a) - cm.getRed(b);

            } else if (cm.getGreen(a) != cm.getGreen(b)) {
                return cm.getGreen(a) - cm.getGreen(b);

            } else if (cm.getBlue(a) != cm.getBlue(b)) {
                return cm.getBlue(a) - cm.getBlue(b);

            } else {
                return cm.getAlpha(a) - cm.getAlpha(b);
            }
        }
    }
}
