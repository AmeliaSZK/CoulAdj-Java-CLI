import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.ArrayList;
import java.util.List;

public class ColourAdjacencies {
    public static final boolean DIAGONALS_DEFAULT = false;

    private BufferedImage image;
    private boolean dontRelateDiagonals;
    private List<String> lines;

    private boolean adjacenciesAreComputed = false;
    private boolean linesAreComputed = false;

    public ColourAdjacencies(BufferedImage image) {
        this.image = image;
        this.dontRelateDiagonals = DIAGONALS_DEFAULT;
    }

    public ColourAdjacencies(BufferedImage image, boolean dontRelateDiagonals) {
        this.image = image;
        this.dontRelateDiagonals = dontRelateDiagonals;
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
}
