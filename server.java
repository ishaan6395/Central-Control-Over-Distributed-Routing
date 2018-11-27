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
				TopologyRow row = new TopologyRow(source, destination, cost);
				
				t.addRow(row);
				if(!sources.contains(source))
					sources.add(source);
				line = br.readLine();
				
			}
			listener l = new listener();
			l.start();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}		
	}

	

	static class listener extends Thread{
	
		public void run(){
			
			try{
				DatagramSocket send_socket = new DatagramSocket();
				DatagramSocket ds = new DatagramSocket(8585);
			
				while(true){
				
					// Receive packets
					byte[] b = new byte[1024];
	                        	DatagramPacket dp = new DatagramPacket(b, b.length);
					ds.receive(dp);
					byte[] data = dp.getData();
					ByteArrayInputStream in = new ByteArrayInputStream(data);
					ObjectInputStream is = new ObjectInputStream(in);
					LinkState ls = (LinkState) is.readObject();
					String source = ls.getSource();
					boolean state = ls.getState();				
					ArrayList<TopologyRow> rows = t.getTopology();
					for(TopologyRow row: rows){
						if(source.equals(row.getSource())){
							row.setStateActive(state);

						}
					}
					t.setTopology(rows);
					// BroadCast Topology
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					ObjectOutputStream os = new ObjectOutputStream(outputStream);
					os.writeObject(t);
					os.flush();
					byte[] buf = outputStream.toByteArray();

					for(String s:sources){
						DatagramPacket dp_send = new DatagramPacket(buf, buf.length, InetAddress.getByName(s), 8585);
						send_socket.send(dp_send);	
					}	
				}

			}catch(Exception e){
				System.out.println("In listener run: "+e.getMessage());
			}

			
		}

	}
}

