import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
* Converts an inputted PNG file into its hexadecimal representation 
*/
public class HexConvert {
	// Instance variables
	private File infile;

	// 1-arg constructor
	public HexConvert(File infile) {
		this.infile = infile;
	}
	
	// Decodes the PNG file into its hexadecimal representation and returns an ArrayList
	public ArrayList<String> decode() {
		ArrayList<String> byteList = new ArrayList<String>();
		
		try {
			// Sets up FileInputStream and FileWriter objects 
			FileInputStream input = new FileInputStream(infile);

			// Holds the read in byte as an integer
			int i;
			
			// Iterates through every character in the file until a character cannot be found, which FileInputStream.read() returns as -1.
			while ((i = input.read()) != -1) {	
				// Values are read in as integers, then converted to hexadecimal below
				String hexValue = Integer.toHexString(i).toUpperCase();
				String stringAsHex = (i < 16) ? "0" + hexValue : hexValue;
				
				// Stores the converted hex value in the list
				byteList.add(stringAsHex);
			}			
			return byteList;
			
		} catch (IOException e) {
				System.out.println("Error has occurred.");
		}
		
		return byteList; 	
	}
	
	// Decodes the PNG file into its hexadecimal representation and sends the output to a specified file
	public void decode(File outfile) {		
		try {
			// Sets up FileInputStream and FileWriter objects 
			FileInputStream input = new FileInputStream(infile);
			FileWriter writer = new FileWriter(outfile);

			// Holds the read in byte as an integer
			int i;
			
			// Iterates through every character in the file until a character cannot be found, which FileInputStream.read() returns as -1.
			while ((i = input.read()) != -1) {	
				// Values are read in as integers, then converted to hexadecimal below
				String hexValue = Integer.toHexString(i).toUpperCase();
				String stringAsHex = (i < 16) ? "0" + hexValue : hexValue;
				
				// Prints out the current byte in hexadecimal format
				writer.write(stringAsHex + " ");
			}		
			writer.close();				
		} catch (IOException e) {
				System.out.println("Error has occurred.");
		}	
	}
}