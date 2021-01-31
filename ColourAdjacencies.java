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

    private BufferedImage image;
    private boolean dontRelateDiagonals;
    private List<String> lines;
    private ColorModel cm;
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

        // TODO

        adjacenciesAreComputed = true;
        return;
    }

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
