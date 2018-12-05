import java.io.Serializable;

public class TopologyRow implements Serializable{
    String source,  destination, alias;
    double cost;
    boolean isActive;

    public void setAlias(String alias){
	this.alias = alias;
    }
    public String getAlias(){
	return alias;
    }
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
    public boolean getIsActive(){
	return isActive;
    }
    public void setIsActive(boolean isActive){
	this.isActive = isActive;
    }

    public TopologyRow(String source, String destination, double cost, String alias) {
        this.source = source;
        this.destination = destination;
        this.cost = cost;
	this.alias = alias;
	this.isActive = true;
    }

    public TopologyRow() {
	    this.isActive = true;
    }
    
}

