package coastlines;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class MyCoastLines {
	private MyUTMPoint[][] utmPoints;
	/*
	 * The constructor initializes the utmPoints[][] which is used by the DrawGraph class
	 */
	public MyCoastLines() {
		String[][] coastLineArray = getCoastLineArray();
		utmPoints = new MyUTMPoint[coastLineArray.length][coastLineArray[0].length];
		for(int i = 0; i < coastLineArray.length; i++) {
			for(int j = 0; j < coastLineArray[0].length ; j++) {
				if(coastLineArray[i][j] != null && !coastLineArray[i][j].contains(">")) {
					String[] lineArray = coastLineArray[i][j].split("\\s");
					utmPoints[i][j] = toUTMPoint(Double.parseDouble(lineArray[1]), Double.parseDouble(lineArray[0]));
				}
			}
		}
	}

	/*
	 * Convert a lat/long point to a UTM point.
	 * This method is heavily based on the math made by
	 * Jonathan Stott, http://www.jstott.me.uk/jcoord/
	 * 
	 * @param lat The latitude of the point
	 * @param lon The longitude of the point
	 */
	public MyUTMPoint toUTMPoint(double lat, double lon) {

		if (lat < -80 || lat > 84) {
			System.out.println("Latitude (" + lat + ") falls outside the UTM grid.");
		}

		if (lon == 180.0) {
			lon = -180.0;
		}

		double UTM_F0 = 0.9996;
		/*double a = WGS84Ellipsoid.getInstance().getSemiMajorAxis();
		double eSquared = WGS84Ellipsoid.getInstance().getEccentricitySquared();
		System.out.println(WGS84Ellipsoid.getInstance().toString());
		System.out.println(eSquared);*/
		double a = 6378137.0; //WGS84Ellipsoid.getInstance().getSemiMajorAxis();
		double eSquared = 0.006694380004260827; // WGS84Ellipsoid.getInstance().getEccentricitySquared(); or (((6378137.0*6378137.0)-(6356752.3142*6356752.3142))/6378137.0)

		double latitudeRad = lat * (Math.PI / 180.0);
		double longitudeRad = lon * (Math.PI / 180.0);
		int longitudeZone = 32;

		double longitudeOrigin = (longitudeZone - 1) * 6 - 180 + 3;
		double longitudeOriginRad = longitudeOrigin * (Math.PI / 180.0);

		double ePrimeSquared = (eSquared) / (1 - eSquared);

		double n = a / Math.sqrt(1 - eSquared * Math.sin(latitudeRad) * Math.sin(latitudeRad));
		double t = Math.tan(latitudeRad) * Math.tan(latitudeRad);
		double c = ePrimeSquared * Math.cos(latitudeRad) * Math.cos(latitudeRad);
		double A = Math.cos(latitudeRad) * (longitudeRad - longitudeOriginRad);

		double M = a * ((1 - eSquared / 4 - 3 * eSquared * eSquared / 64 - 5 * eSquared	* eSquared * eSquared / 256) * latitudeRad
				- (3 * eSquared / 8 + 3 * eSquared * eSquared / 32 + 45 * eSquared * eSquared * eSquared / 1024) * Math.sin(2 * latitudeRad)
				+ (15 * eSquared * eSquared / 256 + 45 * eSquared * eSquared * eSquared / 1024) * Math.sin(4 * latitudeRad) - 
				(35 * eSquared * eSquared * eSquared / 3072) * Math.sin(6 * latitudeRad));

		double UTMEasting = (UTM_F0	* n	* (A + (1 - t + c) * Math.pow(A, 3.0) / 6 + (5 - 18 * t + t * t + 72
						* c - 58 * ePrimeSquared) * Math.pow(A, 5.0) / 120) + 500000.0);

		double UTMNorthing = (UTM_F0 * (M + n * Math.tan(latitudeRad) * (A * A / 2 + (5 - t + (9 * c) 
				+ (4 * c * c)) * Math.pow(A, 4.0) / 24 + (61 - (58 * t) + (t * t) + (600 * c) - (330 * ePrimeSquared))
				* Math.pow(A, 6.0) / 720)));

		// Adjust for the southern hemisphere
		if (lat < 0) {
			UTMNorthing += 10000000.0;
		}

		return new MyUTMPoint(UTMEasting, UTMNorthing);
	}


	/*
	 * Reads the input file which contains lon/lat points
	 * 
	 * @return Returns a String[][] where each array contains all points of a connected piece of land
	 */
	private String[][] getCoastLineArray() {
		String[][] coastLineArray = new String[784][1885];
		File file = new File("coast1.txt");
		int i = -1;
		int j = 0;
		try {
			Scanner sc = new Scanner(file);
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				if(line.contains(">")) {
					i++;
					j = 0;

				}
				else {
					coastLineArray[i][j] = line;
					j++;
				}
			}
			sc.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return coastLineArray;
	}

	/*
	 * @return Returns the MyUTMPoint[][] where each array contains all points of a connected piece of land
	 */
	public MyUTMPoint[][] getUTMPoints() {
		
		return utmPoints;
	}


}
