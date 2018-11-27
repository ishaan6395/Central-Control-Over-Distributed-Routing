
import java.util.*;

public class Topology {
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
    
    
}

