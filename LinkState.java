
public class LinkState {
    String source;
    boolean state;

    public LinkState() {
    }

    public LinkState(String source, boolean state) {
        this.source = source;
        this.state = state;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getSource() {
        return source;
    }

    public boolean getState() {
        return state;
    }
    
}

