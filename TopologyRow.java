import java.io.Serializable;

public class TopologyRow implements Serializable{
    String source,  destination;
    double cost;
    boolean fake;

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public double getCost() {
        return cost;
    }
    public boolean getFake(){
	return fake;
    }
    public void setFake(boolean fake){
	this.fake = fake;
    }

    public TopologyRow(String source, String destination, double cost) {
        this.source = source;
        this.destination = destination;
        this.cost = cost;
	this.fake = false;
    }

    public TopologyRow() {
	    this.fake = false;
    }
    
}

