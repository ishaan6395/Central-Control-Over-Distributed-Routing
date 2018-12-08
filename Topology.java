import java.io.Serializable;
import java.util.*;
import java.net.*;
import java.io.*;


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

    public void printShortestPath(){
	
	try{
		
		File f1 = new File("top.txt");
                FileWriter fr1 = new FileWriter(f1, false);
		int len = topology.size();
		int ind = 0;
		for(TopologyRow row: topology){
			fr1.write(row.getSource()+","+row.getDestination()+","+(int)row.getCost());
			if(ind<len-1)
				fr1.write("\n");
			ind++;
		}
		fr1.close();
		
		//Shortest Path Read file
		File f = new File("top.txt");
                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);

                String line = br.readLine();

                Graph g = new Graph();
                List<String> names = new ArrayList<>();
                while(line!=null){
                        String[] content = line.split(",");
                        String source = content[0];
			if(!names.contains(source))
                        names.add(source);
			
                //      System.out.println(source.hashCode());
                        String dest = content[1];
                        if(!names.contains(dest))
			names.add(dest);
			int cost = Integer.parseInt(content[2]);
                       
                        g.addVertex(source, new Vertex(dest,dest,cost));
			g.addVertex(dest, new Vertex(source,dest,cost));
                        line = br.readLine();
                }

                //System.out.println("129.21.22.196".hashCode())
                //
	
		//System.out.println(names.size());
	
		String l1 = InetAddress.getLocalHost().toString().split("/")[1];
		for(String name:names){
			if(!name.equals(l1)){
				List<String> x = g.getShortestPath(l1,name);
				Collections.reverse(x);
				System.out.println(x);
			}

		}
		
	}catch(Exception e){
		System.out.println("In Shortest Path function: "+e.getMessage());
		e.printStackTrace();
	}
    }    
    
}

