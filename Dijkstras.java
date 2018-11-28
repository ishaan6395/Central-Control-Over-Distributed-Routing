import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.*;
import java.util.Map;
import java.util.PriorityQueue;
import java.io.*;
import java.util.*;
public class Dijkstras {

	public static void main(String[] args) throws Exception {

		File f = new File("topology.txt");
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);

		String line = br.readLine();
		HashMap<String, HashMap<String,Double>> map = new HashMap<>();
	
		Graph g = new Graph();

		while(line!=null){
			String[] content = line.split(",");
			String source = content[0];
			String destination = content[1];
			double cost = Double.parseDouble(content[2]);
			
			if(!map.containsKey(source)){
				HashMap<String,Double> val = new HashMap<>();
				val.put(destination,cost);
				map.put(source, val);
			}else{
				HashMap<String,Double> val = new HashMap<>();
				val = map.get(source);
				val.put(destination,cost);
				map.put(source, val);
			}
			line = br.readLine();
		}
		
		Iterator it = map.entrySet().iterator();
		while(it.hasNext()){

			Map.Entry pair = (Map.Entry)it.next();
			HashMap<String, Double> val = (HashMap<String, Double>)pair.getValue();
			System.out.println(pair.getKey());
			Iterator it2 = val.entrySet().iterator();
			List<Vertex> temp = new ArrayList<>();
			while(it2.hasNext()){
				Map.Entry pair1 = (Map.Entry)it2.next();
				double c= (double)pair1.getValue();
				int c1 = (int)c;
				System.out.println(c1);
				temp.add(new Vertex(pair1.getKey().toString(),c1));
				System.out.println(pair1.getKey()+" "+c1);				
			}
			String source = pair.getKey().toString();
			g.addVertex(source, temp);
		}
		List<String> x = g.getShortestPath("129.21.22.196","129.21.37.49");
		Collections.reverse(x);
		System.out.println(x);
		
	/*	
		g.addVertex('A', Arrays.asList(new Vertex('B', 7), new Vertex('C', 8)));
		g.addVertex('B', Arrays.asList(new Vertex('A', 7), new Vertex('F', 2)));
		g.addVertex('C', Arrays.asList(new Vertex('A', 8), new Vertex('F', 6), new Vertex('G', 4)));
		g.addVertex('D', Arrays.asList(new Vertex('F', 8)));
		g.addVertex('E', Arrays.asList(new Vertex('H', 1)));
		g.addVertex('F', Arrays.asList(new Vertex('B', 2), new Vertex('C', 6), new Vertex('D', 8), new Vertex('G', 9), new Vertex('H', 3)));
		g.addVertex('G', Arrays.asList(new Vertex('C', 4), new Vertex('F', 9)));
		g.addVertex('H', Arrays.asList(new Vertex('E', 1), new Vertex('F', 3)));
		List<Character> x = g.getShortestPath('A', 'H');
                Collections.reverse(x);
                System.out.println(x);
		*/
	}
        
	
}

