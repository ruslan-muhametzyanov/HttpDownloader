import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileSplitter {
	
	private ArrayList<Pair<String, String>> pairs = new ArrayList();

	public FileSplitter(String pathOnListOfLinks){

		readFile(pathOnListOfLinks);

	}
	
	private void readFile( String file ){
		
		Scanner sc = null;
		
		try {
			sc = new Scanner(new File(file));
			while(sc.hasNext()){
		        String url = sc.next();
		        String path = sc.next();
                Pair<String, String> pair = new Pair(url, path);
                pairs.add(pair);
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally{
			sc.close();
		}
	}

	public ArrayList<Pair<String,String>> getPair(){
		return pairs;
	}

}
