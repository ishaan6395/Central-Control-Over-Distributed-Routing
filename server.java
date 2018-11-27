import java.util.*;
import java.net.*;
import java.io.*;

public class server extends Thread{

	static Topology t;
	static HashSet<String> sources;
	public static void main(String args[]){
		t = new Topology();
		sources = new HashSet<>();
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
				sources.add(source);
				line = br.readLine();

			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}		
	}

	static class sender extends Thread{
	

	}

	static class listener extends Thread{
	
		public void run(){
			
			try{
				DatagramSocket ds = new DatagramSocket(8585);
			
				while(true){
				

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

					// Send it to all the available destinations

						
				}

			}catch(Exception e){
				System.out.println("In listener run: "+e.getMessage());
			}

			
		}

	}
}

