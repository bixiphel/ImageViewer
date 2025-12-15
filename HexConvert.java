import java.io.FileInputStream;
import java.io.IOException;

public class HexConvert {

	public HexConvert() {}
	
	public void decode() {
		try {
			FileInputStream input = new FileInputStream("testImage.png");
			
			int i;
			
			while ((i = input.read()) != -1) {
				// Values are read in as integers, then converted to hexadecimal below
				String hexValue = Integer.toHexString(i).toUpperCase();
				String stringAsHex = (i < 17) ? "0" + hexValue : hexValue;
				
				System.out.print(stringAsHex + " ");
			} 
		} catch (IOException e) {
				System.out.println("Error has occurred.");
		} 	
	}
}