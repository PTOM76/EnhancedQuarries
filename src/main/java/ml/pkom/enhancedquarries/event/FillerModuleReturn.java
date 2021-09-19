package ml.pkom.enhancedquarries.event;

import java.util.Objects;

public class FillerModuleReturn {
    public static FillerModuleReturn RETURN_FALSE = new FillerModuleReturn(0);
    public static FillerModuleReturn RETURN_TRUE = new FillerModuleReturn(1);
    public static FillerModuleReturn CONTINUE = new FillerModuleReturn(2);
    public static FillerModuleReturn BREAK = new FillerModuleReturn(3);
    public static FillerModuleReturn NOTHING = new FillerModuleReturn(4);

    private int mode;

    public FillerModuleReturn(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FillerModuleReturn that = (FillerModuleReturn) o;
        return mode == that.mode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mode);
    }
}
