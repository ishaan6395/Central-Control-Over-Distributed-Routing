import java.io.*;
import java.util.*;

public class test{

	public static HashMap<String,String> map = new HashMap<>();
	public static void main(String args[]){
		try{
			String requirement = "Fail:A;B,A,G,A;*;C;*;G";
			BufferedReader br = new BufferedReader(new FileReader(new File("topology.txt")));
			String line = br.readLine();
			Topology t = new Topology();
			ArrayList<TopologyRow> r = new ArrayList<>();
			while(line!=null){
				String contents[] = line.split(",");
				String source = contents[0];
				String dest = contents[1];
				double cost = Double.parseDouble(contents[2]);
				r.add(new TopologyRow(source, dest, cost, dest));
				line = br.readLine();
			}
			t.setTopology(r);
			
			String req_break[] = requirement.split(",");
			String link[] = req_break[0].split(":")[1].split(";");
			List<String> path2 = new ArrayList<>();
			String source = req_break[1];
			String dest = req_break[2];
			String temp_path[] = req_break[3].split(";");
			for(String node : temp_path){
				path2.add(node);
			}



		        ArrayList<TopologyRow> r1 = removeLink(r, link[0], link[1]); 
			r1 = removeLink(r1, "E","C");
			r1 = removeLink(r1,"A","C");
			Graph g = new Graph();
                        for(TopologyRow row: r1){

                                g.addVertex(row.getSource(), new Vertex(row.getDestination(), row.getDestination(),(int)row.getCost()));
                        }

                        
                        
			List<String> path = g.getShortestPath(source, dest);
			path.add(source);
			Collections.reverse(path);
			System.out.println("The path which will be taken according to Dijkistra on Failure of link "+link[0]+"-"+link[1]+": "+path+"\n");

			//t.printTopology();	
					
			//Augmenting the Topology
			augmentTopology(g, path, path2, source, dest, r1);
			

			//Print path from current node to destintion
                         List<String> p = g.getShortestPath(source, dest);
			 p.add(source);
                         Collections.reverse(p);
			//
			ArrayList<String> p1 = new ArrayList<>();
			printPath(g, p, p1, dest);
			System.out.println("\nThe path which will be chosen by Fibbing Controller: "+p1);
			
			//Printing Augmented Topology
			System.out.println("\nThe augmented Topology is: ");
			t.setTopology(r1);
			t.printTopology();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public static void augmentTopology(Graph g, List<String> path, List<String> path2, String source, String dest, ArrayList<TopologyRow> r1){
		 for(int idx = 0;idx<path2.size();idx++){
                                if(!path.get(idx).equals(path2.get(idx))){
					if(path2.get(idx).equals("*")&&idx==path2.size()-1){
						
						break;
					}else if(path2.get(idx).equals("*")){
						String fakenode = "Fake"+path2.get(idx-1);
						g.addVertex(path2.get(idx-1), new Vertex(fakenode, path2.get(idx+1),1));
						g.addVertex(fakenode, new Vertex(path2.get(idx-1), path2.get(idx+1),1));
						g.addVertex(fakenode, new Vertex(path2.get(idx+1), path2.get(idx+1),1));
						g.addVertex(path2.get(idx+1), new Vertex(fakenode, path2.get(idx+1),1));
						map.put(fakenode, path2.get(idx+1));
					}else if(path2.get(idx-1)!="*"){

                                        System.out.println("Adding fake on: "+path2.get(idx-1));
                                        System.out.println(path2.get(idx-1)+","+"Fake"+path2.get(idx-1)+",1,"+path2.get(idx));
                                        System.out.println("Fake"+path2.get(idx-1)+","+"C"+",1,"+path2.get(idx));

                                        path.add(idx,path2.get(idx));
                                        String fakenode = "Fake"+path2.get(idx-1);
                                        TopologyRow row1 = new TopologyRow(path2.get(idx-1), fakenode, (double)1, path2.get(idx));

                                        TopologyRow row2 = new TopologyRow(fakenode, path2.get(idx-1), (double)1, path2.get(idx));
                                        TopologyRow row3 = new TopologyRow(fakenode, dest, (double)1, path2.get(idx));
                                        TopologyRow row4 = new TopologyRow(dest, fakenode, (double)1, path2.get(idx));
                                        r1.add(new TopologyRow(path2.get(idx-1), fakenode, (double)1, path2.get(idx)));
                                        r1.add(new TopologyRow(fakenode, path2.get(idx-1), (double)1, path2.get(idx)));
                                        r1.add(row3);
                                        r1.add(row4);
                                        map.put(fakenode, path2.get(idx));

                                        g.addVertex(path2.get(idx-1),new Vertex(fakenode,path2.get(idx),1));
                                        g.addVertex(fakenode, new Vertex(path2.get(idx-1), path2.get(idx),1));
                                        g.addVertex(fakenode, new Vertex(dest,path2.get(idx) ,1));
                                        g.addVertex(dest, new Vertex(fakenode, path2.get(idx),1));

					}
				}            
		 }

	}
	public static ArrayList<TopologyRow> removeLink(ArrayList<TopologyRow> r, String source, String dest){
		ArrayList<TopologyRow> r1 = new ArrayList<>();
                for(TopologyRow row: r){
	                if(!((row.getSource().equals(source)&&row.getDestination().equals(dest) ||
                                           (row.getSource().equals(dest)&&row.getDestination().equals(source) )))) 
        		//			System.out.println(row.getSource()+" "+row.getDestination());	
                                                r1.add(row);
                                        
                
		}
		return r1;
        	}                
	
	//Constructing the Path	
	public static void printPath(Graph g, List<String> p, ArrayList<String> p1, String dest){
		//Printing Path
		for(String node: p){
			if(node.contains("Fake")){
				List<String> path = g.getShortestPath(map.get(node),dest);
				path.add(map.get(node));
				Collections.reverse(path);
				printPath(g, path, p1, dest);
			}else{
				if(!p1.contains(node)){
					p1.add(node);
				}
			}
		}
	}

}
