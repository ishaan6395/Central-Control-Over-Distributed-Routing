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
				DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName("129.21.34.80"),8585);
		
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
		System.out.println(neighbours.size());
		for(String node: neighbours){

			DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(node), 8585);
			ds.send(dp);
			System.out.println("Sent to "+node);
		}
		}catch(Exception e){
			System.out.println("Error in sendToNeighbours: "+e.getMessage());
		}
        }

	static class Receiver extends Thread{
		
		public void run(){
			try{
			
				DatagramSocket ds = new DatagramSocket(8585);
				
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
								System.out.println("added");							
							}
							
						}
					}
					if(change){
						sendToNeighbours();
					}
					t.printTopology();
					
					
				}
			}catch(Exception e){
				System.out.println("Receiver in client: "+e.getMessage());
			}

		}

		

		public boolean updateTopology(Topology temp){
			ArrayList<TopologyRow> temp_rows = temp.topology;
			ArrayList<TopologyRow> rows = t.topology;
			boolean change = true;
			for(TopologyRow row: temp_rows){
				boolean add = true;
				change = true;
				for(TopologyRow row1: rows){
					if((row1.getSource().equals(row.getSource()))&&(row1.getDestination().equals(row.getDestination()))){
						add = false;
						change = false;	
					}
				}
				if(add){
					
					rows.add(row);
				}
			}
			t.setTopology(rows);
			return change;
		}

	}
}
