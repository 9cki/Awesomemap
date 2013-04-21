package graph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.ximpleware.*;

public class MyNodeVTD {
	private static String kdv;
	private static String xcoord;
	private static String ycoord;
	
	private static String[] sArray = new String[675902]; //Initialized to the exact number of nodes in the dataset
	
		public static String[] parse(String name) throws IOException, NavException, XPathParseException, XPathEvalException, ParseException{
			 File f = new File(name);
			 FileInputStream fis = new FileInputStream(f);
			 byte[] b = new byte[(int) f.length()];
			 fis.read(b);
			 VTDGen vg = new VTDGen();

			 vg.setDoc(b);
			 vg.parse(true); // set namespace awareness to true
			 
			 // Objects to assist navigation in XML
			 VTDNav vn = vg.getNav(); 
			 AutoPilot ap = new AutoPilot(vn);
			 ap.bind(vn);
			 
			 int count = 0;
			 int t;
			 		 
			 //First of all select element. The VTD Navigator VTDNav will iterate over all 
			 //such elements
			 ap.selectElement("node");
			 while(ap.iterate()){
				
				 //get first child that is a kdv number
				 vn.toElement(VTDNav.FIRST_CHILD,"kdv");
				 
				 //getText returns an index in the XML document so take this index t and
				 //then if t!=-1 print the text of the respective element
				 if ((t=vn.getText())!= -1) {
					kdv = null;
				 	kdv = vn.toNormalizedString(t);
				 }
				 vn.toElement(VTDNav.NEXT_SIBLING); //navigates to next sibling on the XML tree
				 if ((t=vn.getText())!= -1) {
					xcoord = null;
				 	xcoord = vn.toNormalizedString(t);
				 }
				 vn.toElement(VTDNav.NEXT_SIBLING); //navigates to next sibling on the XML tree
				 if ((t=vn.getText())!= -1) {
					 ycoord = null;
					 ycoord = vn.toNormalizedString(t);
					 //Add all the information to one string
					 String finalString = kdv + " &&& " + xcoord + " &&& " + ycoord;
					 sArray[count] = finalString;
					 count++;
				 }
			 }
			 ap.resetXPath();
			 
			 b=null;
			 fis.close();
			 return sArray;
		}


}
