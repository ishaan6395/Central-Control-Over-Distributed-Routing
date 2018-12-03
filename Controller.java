import java.io.*;

public class Controller{
	public static void main(String args[]){
		try{
			BufferedReader br = new BufferedReader(new FileReader(new File("requirements.txt")));
			String line = br.readLine();
			while(line!=null){
		
				String[] contents = line.split(",");
				String problem = contents[0].split(":")[0];
				String problem_router = contents[0].split(":")[1];

				String source = contents[1];
				String dest = contents[2];
				String path = contents[3];

				System.out.println("The problem is router "+problem_router+", "+problem);
				System.out.println("The source is "+source+" and the destination is "+dest);
				System.out.println("Now take the path: "+path);

				line = br.readLine();
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}
