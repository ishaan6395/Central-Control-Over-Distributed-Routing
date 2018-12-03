import java.util.*;
import java.net.*;
import java.io.*;

public class Client extends Thread{
	
	static ArrayList<String> neighbours;
	static Topology t;
	public static void main(String args[]){
		neighbours = new ArrayList<>();
		
		t = new Topology(new ArrayList<TopologyRow>());
		
		Sender s = new Sender();
		s.start();
		Receiver r = new Receiver();
		r.start();
	
	}

	static class Sender extends Thread{
		public void run(){
			try{

				DatagramSocket ds = new DatagramSocket();
				LinkState ls = new LinkState(InetAddress.getLocalHost().toString().split("/")[1],true);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ObjectOutputStream os = new ObjectOutputStream(outputStream);
				os.writeObject(ls);
				os.flush();
				byte[] buf = outputStream.toByteArray();
				DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName("172.17.0.2"),6789);
		
				ds.send(dp);
				
			}
			catch(Exception e){
				System.out.println("Sender in exception: "+e.getMessage());
			}
		}
	}

	static public void sendToNeighbours(){

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

	static class Receiver extends Thread{
		
		public void run(){
			try{
			
				DatagramSocket ds = new DatagramSocket(6790);
				
				while(true){
					byte[] b = new byte[3000];
					DatagramPacket dp = new DatagramPacket(b, b.length);
					ds.receive(dp);
					byte[] data = dp.getData();
					ByteArrayInputStream in = new ByteArrayInputStream(data);
					ObjectInputStream is = new ObjectInputStream(in);
					Topology temp = (Topology) is.readObject();
					
					ArrayList<TopologyRow> rows = t.getTopology();
					String local = InetAddress.getLocalHost().toString().split("/")[1];
					boolean change = updateTopology(temp);
					//t.printTopology();
					rows = t.getTopology();
					
					for(TopologyRow r: rows){
						if(r.getSource().equals(local)){
							if(!neighbours.contains(r.getDestination())){
								neighbours.add(r.getDestination());
				//				System.out.println("added");							
							}
							
						}
					}
					if(change){
						sendToNeighbours();
					}
					//deduplicate();
					System.out.println("Topology Database is:");
					t.printTopology();
					t.printShortestPath();			
					
				}
			}catch(Exception e){
				System.out.println("Receiver in client: "+e.getMessage());
			}

		}
		static public boolean hasRow(ArrayList<TopologyRow> temp, TopologyRow r){
			for(TopologyRow row : temp){
				if(row.getSource().equals(r.getSource())){
					if(row.getDestination().equals(r.getDestination())){
				//		System.out.println("ALREADY PRESENT"); return true;
					}
				}
			}
			//System.out.println("Will add");
			return false;
		}
		static public void deduplicate(){
			ArrayList<TopologyRow> new_rows = new ArrayList<>();
			ArrayList<TopologyRow> original = t.topology;

			for(TopologyRow r: original){
				if(!hasRow(new_rows, r)){
					new_rows.add(r);
				}
			}
			System.out.println(new_rows.size());
			t.setTopology(new_rows);
			
		}		
		
		public synchronized boolean updateTopology(Topology temp){
			ArrayList<TopologyRow> temp_rows = temp.topology;
			ArrayList<TopologyRow> rows = t.topology;
			
			HashSet<TopologyRow> new_rows = new HashSet<>();
			boolean change = false;
			for(TopologyRow row: temp_rows){
				boolean add = true;
				
				for(TopologyRow row1: rows){
					if((row1.getSource().equals(row.getSource()))&&(row1.getDestination().equals(row.getDestination()))){
						add = false;
							
					}
				}
				if(add){
					change = true;
					rows.add(row);
				}
			}
			t.setTopology(rows);
			return change;
		}

	}
}
