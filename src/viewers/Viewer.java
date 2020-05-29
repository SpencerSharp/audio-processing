package viewers;

abstract public class Viewer {
    public abstract String getMatrix();
    public abstract void setYZoom(double pct);
    public abstract void setYOffset(double amt);
}