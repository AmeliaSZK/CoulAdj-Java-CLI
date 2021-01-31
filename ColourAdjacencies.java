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

    private final BufferedImage image;
    private final boolean dontRelateDiagonals;
    private List<String> lines;
    private final ColorModel cm;
    private Map<Integer, Set<Integer>> adjacencies;

    private boolean adjacenciesAreComputed = false;
    private boolean linesAreComputed = false;

    public ColourAdjacencies(BufferedImage image) {
        this.image = image;
        this.dontRelateDiagonals = DIAGONALS_DEFAULT;
        this.cm = image.getColorModel();
    }

    public ColourAdjacencies(BufferedImage image, boolean dontRelateDiagonals) {
        this.image = image;
        this.dontRelateDiagonals = dontRelateDiagonals;
        this.cm = image.getColorModel();
    }

    public void computeAdjacencies() {
        if (adjacenciesAreComputed) {
            return;
        }

        final boolean hasAlpha = cm.hasAlpha();
        final Comparator<Integer> comparator =
            hasAlpha ? new SortByRgbAlpha() : new SortByRgb();
        adjacencies = new TreeMap<>(comparator);

        final int maxX = image.getWidth() - 1;
        final int maxY = image.getHeight() - 1;

        for (int x = 0; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                evaluateAllNeighbours(x, y);
            }
        }

        adjacenciesAreComputed = true;
        return;
    }

    private void evaluateAllNeighbours(int x, int y) {
        boolean diagonalsAreRelated;
        if (dontRelateDiagonals) {
            diagonalsAreRelated = false;
        } else {
            diagonalsAreRelated = true;
        }

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
    }

    private void evaluateOneNeighbour(int x, int y, int xOffset, int yOffset) {}

    public void computeLines() {
        if (linesAreComputed) {
            return;
        } else if (!adjacenciesAreComputed) {
            computeAdjacencies();
        }

        lines = new ArrayList<String>();
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
