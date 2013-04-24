package gui;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Reader {

	ArrayList<String> finalList = new ArrayList<String>();
	
	public Reader(){
		readFile("road_names.txt");
		readFile("city_names.txt");
		
		Collections.sort(finalList);
	}
	
	public void readFile(String s){
		try {
			FileInputStream input = new FileInputStream(s);
			Scanner sc = new Scanner(input);
		
			while(sc.hasNextLine()) {
				finalList.add(sc.nextLine());
			}
			sc.close();
			input.close();
		}
		
		catch(IOException e1) {
			System.out.println("File " + s + " was not found....");
		}
	}
	
	public ArrayList<String> getList(){
		return finalList;
	}
	
}
