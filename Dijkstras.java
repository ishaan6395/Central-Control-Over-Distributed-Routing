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
		//	System.out.println(source.hashCode());
			String dest = content[1];
			names.add(dest);
			int cost = Integer.parseInt(content[2]);
			System.out.println(source+" "+dest+" "+cost);
			g.addVertex(source, new Vertex(dest,dest,cost));
			g.addVertex(dest, new Vertex(source,source,cost));
			line = br.readLine();
		}
	
		System.out.println(names.size());	
		//System.out.println("129.21.22.196".hashCode())
		//
		List<String> x = g.getShortestPath(names.get(0),names.get(1));
                Collections.reverse(x);
                System.out.println(x);
		g.printGraph();		
	}
        
	
}

