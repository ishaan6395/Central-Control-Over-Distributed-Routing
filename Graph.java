import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.*;
import java.util.*;
import java.util.PriorityQueue;
class Graph {
	
	private final HashMap<String, List<Vertex>> vertices;
	
	public Graph() {
		this.vertices = new HashMap<String, List<Vertex>>();
	}
	
	public void addVertex(String ip, Vertex vertex) {
		//this.vertices.put(ip, vertex);
		if(!vertices.containsKey(ip)){
			List<Vertex> v1 = new ArrayList<>();
			v1.add(vertex);
			vertices.put(ip,v1);	
		}else{
			List<Vertex> v = vertices.get(ip);
			v.add(vertex);
			vertices.put(ip,v);
		}
	}
	public void printGraph(){
		Iterator it = vertices.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			String source = (String)pair.getKey();
			System.out.println(source);
			List<Vertex> val = (List<Vertex>)pair.getValue();
			for(Vertex v: val){
				System.out.println(v.id+" "+v.distance);
			}
		}
	}
	public List<String> getShortestPath(String start, String finish) {
		final Map<String, Integer> distances = new HashMap<String, Integer>();
		final Map<String, Vertex> previous = new HashMap<String, Vertex>();
		PriorityQueue<Vertex> nodes = new PriorityQueue<Vertex>();
		
		for(String vertex : vertices.keySet()) {
			if (vertex.equals(start)) {
				distances.put(vertex, 0);
				nodes.add(new Vertex(vertex, 0));
			} else {
				
				distances.put(vertex, Integer.MAX_VALUE);
				nodes.add(new Vertex(vertex, Integer.MAX_VALUE));
			}
			previous.put(vertex, null);
		}
		
		while (!nodes.isEmpty()) {
			Vertex smallest = nodes.poll();
			//System.out.println("here: "+smallest.getId());
			if (smallest.getId().equals(finish)) {
				final List<String> path = new ArrayList<>();
				while (previous.get(smallest.getId()) != null) {
					path.add(smallest.getId());
					smallest = previous.get(smallest.getId());
				}
                                System.out.println(distances.get(finish));
                                return path;
                                
			}

			if (distances.get(smallest.getId()) == Integer.MAX_VALUE) {
				break;
			}
						
			for (Vertex neighbor : vertices.get(smallest.getId())) {
				Integer alt = distances.get(smallest.getId()) + neighbor.getDistance();
				if (alt < distances.get(neighbor.getId())) {
					distances.put(neighbor.getId(), alt);
					previous.put(neighbor.getId(), smallest);
					
					forloop:
					for(Vertex n : nodes) {
						if (n.getId().equals(neighbor.getId())) {
							nodes.remove(n);
							n.setDistance(alt);
							nodes.add(n);
							break forloop;
						}
					}
				}
			}
		}
		String x = distances.keySet().toString();
                System.out.println("here");
		return new ArrayList<String>(distances.keySet());
	}

	
}
