import java.io.Serializable;
import java.util.*;

public class Topology implements Serializable {
    ArrayList<TopologyRow> topology = new ArrayList<>();

    public Topology() {
    }
    public Topology(ArrayList<TopologyRow> topology){
        this.topology = topology;
    }
    public ArrayList<TopologyRow> getTopology(){
	return topology;
    }
    public void setTopology(ArrayList<TopologyRow> topology){
	this.topology = topology;
    }
    public void addRow(TopologyRow r){
        topology.add(r);
    }
	
	
    public void printTopology(){
	
	for(TopologyRow row: topology){
		System.out.println(row.getSource()+" "+row.getDestination()+" "+row.getCost());
	}
    }    
    
}

