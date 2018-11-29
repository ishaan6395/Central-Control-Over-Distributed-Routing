import java.io.*;
import org.apache.commons.io.FileUtils;
public class filecheck{

	public static void main(String args[]){
		try{
		File file1 = new File("topology.txt");
		File file2 = new File("top.txt");
		boolean isTwoEqual = FileUtils.contentEquals(file1, file2);
		}catch(Exception e){

		}
	}
}
