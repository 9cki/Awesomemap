package graph;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.ximpleware.*;

public class MyEdgeVTD {
	private static String nodeFrom;
	private static String nodeTo;
	private static String length;
	private static String roadType;
	private static String roadName;
	private static String speedLimit;
	
	private static String[] sArray = new String[812301]; //Initialized to the exact number of edges in the dataset
	
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
			 ap.selectElement("edge");
			 while(ap.iterate()){
				
				 //get first child that is a node from
				 
				 // vn.toElement(VTDNav.FIRST_CHILD,"nodeFrom");
				 
				 //getText returns an index in the XML document so take this index t and
				 //then if t!=-1 print the text of the respective element
				 
				 //ap.selectXPath("/@nodeFrom");
				 //ap.evalXPath();
				 //ap.selectAttr();
				 
				/*
				 t=vn.getAttrVal("roadName");
				System.out.println(vn.toNormalizedString(t));
				 */
				 if ((t=vn.getAttrVal("nodeFrom"))!= -1) {
					nodeFrom = null;
				 	nodeFrom = vn.toNormalizedString(t);
				 }
				 
				 if ((t=vn.getAttrVal("nodeTo"))!= -1) {
					nodeTo = null;
				 	nodeTo = vn.toNormalizedString(t);
				 }

				 if ((t=vn.getAttrVal("length"))!= -1) {
					length = null;
				 	length = vn.toNormalizedString(t);
				 }
				 if ((t=vn.getAttrVal("roadType"))!= -1) {
					roadType = null;
				 	roadType = vn.toNormalizedString(t);
				 }
				 if ((t=vn.getAttrVal("roadName"))!= -1) {
					roadName = null;
				 	roadName = vn.toNormalizedString(t);
				 }
				 if ((t=vn.getAttrVal("speedLimit"))!= -1) {
					 speedLimit = null;
					 speedLimit = vn.toNormalizedString(t);
					 //Add all the information to one string
					 String finalString = nodeFrom + " &&& " + nodeTo + " &&& " + length + " &&& " 
							 + roadType + " &&& " + roadName + " &&& " + speedLimit;
					 sArray[count] = finalString;
					 //System.out.println(finalString);
					 count++;
				 }
				 
			 }
			 ap.resetXPath();
			 b=null;
			 fis.close();
			 return sArray;
		}

}
