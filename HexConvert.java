import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

public class HexConvert {

	public HexConvert() {}
	
	public void decode(File infile, File outfile) {
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
				
				// Prints out the current byte in hexadecimal format.
				writer.write(stringAsHex + " ");
			} 
			
			// Closes the FileWriter object, which technically isn't necessarily with the try-catch block but I wanted it here :p
			writer.close();
		} catch (IOException e) {
				System.out.println("Error has occurred.");
		} 	
	}
}