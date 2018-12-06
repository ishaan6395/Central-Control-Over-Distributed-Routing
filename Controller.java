import java.io.*;
import java.util.*;
import java.net.*;

public class Controller{

	public static HashMap<String,String> map = new HashMap<>();
	static String k;
	static ArrayList<String> neighbours;
	public ArrayList<String> main(ArrayList<String> n){
		neighbours = n;
		try{
			while(true){
				Scanner s = new Scanner(System.in);
				String command = s.nextLine();
				if(command.equals("start simulation")){
					break;
				}
				else{
					System.out.println("To start simulation please enter \"start simulation\"");
				}
			}
			BufferedReader br1 = new BufferedReader(new FileReader(new File("requirements.txt")));
			String requirement = br1.readLine();
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
			String situation = req_break[0].split(":")[0];
			
			List<String> path2 = new ArrayList<>();
			String source = req_break[1];
			String dest = req_break[2];
			k = dest;
			//String source = "172.17.0.3";
			//String dest = "172.17.0.6";
			String temp_path[] = req_break[3].split(";");
			for(String node : temp_path){
				path2.add(node);
			}
			
			//If a link is Failed send info to neighbours
			if(situation.equals("Fail")){
                                ArrayList<TopologyRow> rows_to_send = new ArrayList<>();
                                for(TopologyRow row: r){
                                        if((row.getSource().equals(link[0]) &&row.getDestination().equals(link[1]) ) || 
					 row.getSource().equals(link[1])&&row.getDestination().equals(link[0]) ){
						row.setIsActive(false);
						rows_to_send.add(row);
                                        }
                                }
				Topology to_send = new Topology();
				to_send.setTopology(rows_to_send);
				sendToNeighbours(to_send);
				
                        }

		        ArrayList<TopologyRow> r1 = removeLink(r, link[0], link[1]); 
			
			Graph g = new Graph();
			
			Graph augmented = new Graph();
                        
			for(TopologyRow row: r1){
				
                                	g.addVertex(row.getSource(), new Vertex(row.getDestination(), row.getDestination(),(int)row.getCost()));

			}
			for(TopologyRow row: r1){

                                augmented.addVertex(row.getSource(), new Vertex(row.getDestination(), row.getDestination(),(int)row.getCost()));
                        }

                        
                       	System.out.println(g.getShortestPath(source, dest)); 
			
			List<String> path = g.getShortestPath(source, dest );
			if(!path.get(path.size()-1).equals(source))
				path.add(source);
			Collections.reverse(path);
			System.out.println("The path which will be taken according to Dijkistra on Failure of link "+link[0]+"-"+link[1]+": "+path+"\n");
			
			//t.printTopology();	
					
			//Augmenting the Topology
			augmentTopology(g, augmented, path, path2, source, dest, r1);
			
			
			//Print path from current node to destintion
                         List<String> p = g.getShortestPath(source, dest);
			 
		 	 if(!path.get(path.size()-1).equals(source))	 
			 	p.add(source);
                         Collections.reverse(p);
			 
			
			ArrayList<String> p1 = new ArrayList<>();
			printPath(g, p, p1, dest);
			
			System.out.println(source+" Shorest path to 172.17.0.4"+g.getShortestPath(source, "172.17.0.4"));
			System.out.println("\nThe path which will be chosen by Fibbing Controller: "+p1);
			printPath(g, p, p1, dest);
			System.out.println(p1);	
			//Printing Augmented Topology
			System.out.println("\nThe augmented Topology is: ");
			t.setTopology(r1);
			t.printTopology();
			printShortestAugmentedPath(g, augmented, t);
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return neighbours;
	
	}
	

	static public void sendToNeighbours(Topology t){

                try{
                DatagramSocket ds = new DatagramSocket();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(outputStream);
                os.writeObject(t);
                os.flush();
                byte[] buf = outputStream.toByteArray();

                for(String node: neighbours){

                        DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(node), 6790);
                        ds.send(dp);
                        //System.out.println("Sent to "+node);
                }
                }catch(Exception e){
                        System.out.println("Error in sendToNeighbours: "+e.getMessage());
                }
        }


	public static void augmentTopology(Graph g, Graph augmented, List<String> path, List<String> path2, String source, String dest, ArrayList<TopologyRow> r1){
		 for(int idx = 0;idx<path2.size();idx++){
                               // if(!path.get(idx).equals(path2.get(idx))){
					if(path2.get(idx).equals("*")&&idx==path2.size()-1){
						
						break;
					}else if(path2.get(idx).equals("*")){
						String fakenode = "Fake"+path2.get(idx-1);
						augmented.addVertex(path2.get(idx-1), new Vertex(fakenode, path2.get(idx+1),1));
						augmented.addVertex(fakenode, new Vertex(path2.get(idx-1), path2.get(idx+1),1));
						augmented.addVertex(fakenode, new Vertex(path2.get(idx+1), path2.get(idx+1),1));
						augmented.addVertex(path2.get(idx+1), new Vertex(fakenode, path2.get(idx+1),1));
						map.put(fakenode, path2.get(idx+1));
					}else if(idx>0){

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

                                        augmented.addVertex(path2.get(idx-1),new Vertex(fakenode,path2.get(idx),1));
                                        augmented.addVertex(fakenode, new Vertex(path2.get(idx-1), path2.get(idx),1));
                                        augmented.addVertex(fakenode, new Vertex(dest,path2.get(idx) ,1));
                                        augmented.addVertex(dest, new Vertex(fakenode, path2.get(idx),1));

					}
				//}            
		 }

	}
	public static void printShortestAugmentedPath(Graph g, Graph augmented, Topology t){
		try{
			//String local = InetAddress.getLocalHost().toString().split("/")[1];
			
			String local = "172.17.0.3";
			System.out.println("\nThe Routing Table for "+local+" is: \n");
			ArrayList<TopologyRow> rows = t.topology;
			ArrayList<String> destinations = new ArrayList<>();

			for(TopologyRow row: rows){
				if(!row.getSource().equals(local) && !row.getSource().contains("Fake")){
					if(!(destinations.contains(row.getSource()) ))
			
							destinations.add(row.getSource());
				}
			}
			for(String dest: destinations){
				System.out.println(local+"For destination: "+dest);	
				List<String> p;
				if(dest.equals(k))       
					p= g.getShortestPath(local, dest);
				else
					p = augmented.getShortestPath(local, dest);
				ArrayList<String> p2 = new ArrayList<>();
				p.add(local);
				Collections.reverse(p);
				printPath(g, p, p2, dest);
				System.out.println("The path to destination :"+dest+" is : "+p2);
				
				
			}
		}catch(Exception e){
			System.out.println("Exception in print Shortest Augmented Path: "+e.getMessage());
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
