import java.util.*;
import java.net.*;
import java.io.*;

public class server extends Thread{

	static Topology t;
	static ArrayList<String> sources;
	public static void main(String args[]){
		t = new Topology();
		sources = new ArrayList<>();
		try{
			File f = new File("topology.txt");
	                FileReader fr = new FileReader(f);
        	        BufferedReader br = new BufferedReader(fr);

			String line = br.readLine();
			while(line != null){
				String[] content = line.split(",");
				String source = content[0];
				String destination = content[1];
				double cost = Double.parseDouble(content[2]);
				TopologyRow row = new TopologyRow(source, destination, cost, destination);
				
				t.addRow(row);
				if(!sources.contains(source))
					sources.add(source);
				line = br.readLine();
				
			}
			listener l = new listener();
			System.out.println("Starting");
			l.start();
			System.out.println("Started");
		}catch(Exception e){
			System.out.println(e.getMessage());
		}		
	}

	

	static class listener extends Thread{
	
		public void run(){
			
			try{
				DatagramSocket send_socket = new DatagramSocket();
				DatagramSocket ds = new DatagramSocket(6789);
			
				while(true){
				
					// Receive packets
					byte[] b = new byte[3000];
	                        	DatagramPacket dp = new DatagramPacket(b, b.length);
					ds.receive(dp);
					byte[] data = dp.getData();
					ByteArrayInputStream in = new ByteArrayInputStream(data);
					ObjectInputStream is = new ObjectInputStream(in);
					System.out.println("FROM: "+dp.getAddress().toString());
					LinkState ls = (LinkState) is.readObject();
					String source = ls.getSource();
					boolean state = ls.getState();
					System.out.println(source+" "+state);				
					ArrayList<TopologyRow> rows = t.getTopology();
					ArrayList<TopologyRow> rows_to_send = new ArrayList<>();
					for(TopologyRow row: rows){
						if(source.equals(row.getSource())){
						//	row.setStateActive(state);

						}
						if(source.equals(row.getSource())){
							rows_to_send.add(row);
						}
					}

					Topology topology_to_send = new Topology(rows_to_send);
					t.setTopology(rows);
					// BroadCast Topology
					
					System.out.println("Broadcasting");
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					ObjectOutputStream os = new ObjectOutputStream(outputStream);
					os.writeObject(topology_to_send);
					os.flush();
					byte[] buf = outputStream.toByteArray();
					
									
					DatagramPacket dp_send = new DatagramPacket(buf, buf.length, InetAddress.getByName(source), 6790);
					System.out.println(source);
					
					send_socket.send(dp_send);	
					
					System.out.println("sent");		
				}

			}catch(Exception e){
				System.out.println("In listener run: "+e.getMessage());
				e.printStackTrace();
			}

			
		}

	}
}

