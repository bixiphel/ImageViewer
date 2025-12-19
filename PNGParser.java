import java.util.ArrayList;

/**
* This class contains methods to parse and display information about PNGs.
*/
public class PNGParser {

    // Default Constructor
    public PNGParser() {}
    
    /**
    * Prints information about the chunks of a PNG
    * @param an ArrayList of Strings containing the hexadecimal representation of each character
    */
    public void getChunks(ArrayList<String> byteList) {
        // Stores an index variable to reference later
        int i = 0;
    
        // Performs a check to see if the inputted list of bytes contains the expected PNG signature, which is the same 8 bytes for every PNG
        String expected_signature = "89 50 4E 47 0D 0A 1A 0A";
        String obtained_signature = String.join(" ", byteList.subList(0, i + 8));
        
        // Prints out results to error stream
        System.err.printf("Obtained Signature: %s | Matches Expected Signature: %b%n", obtained_signature, expected_signature.equals(obtained_signature));
        
        // Sets the index variable ahead to parse the chunk type
        i = 8;
        
        // Instruction counter
        int instructionCount = 1;
        
        while(i < byteList.size()) {
            // Each step in the parser breaks down the chunk into its 4 components: the length of the data field, the chunk type, the data field, and the CRC error detecting code
            System.out.printf("----- Chunk #%s -----%n", instructionCount);
            
            // 1. Gets the length of the chunk's data field
            int dataLength = Integer.decode("0x" + String.join("", byteList.subList(i, i + 4)));
            System.out.printf("The length of the chunk #%d's data field is: %d%n", instructionCount++, dataLength);
            
            // Sets index 4 bytes ahead
            i += 4;
            
            // 2. Gets the Chunk Type
            String chunkType = "";
            for(int j = 0; j < 4; j++) {
            int currentChar = Integer.decode("0x" + byteList.get(i++));
                chunkType = chunkType + (char) currentChar;
            }
            String chunkImportance = (Character.isUpperCase(chunkType.charAt(0))) ? "critical" : "ancillary";
            System.out.printf("The chunk type is: %s | Critical/Ancillary chunk: %s%n", chunkType, chunkImportance);
            
            // 3. Gets the Data Field
            System.out.printf("The '%s' chunk has data field: ", chunkType);
            for(int j = 0; j < dataLength; j++) {
                if(j < 10) {
                    System.out.print(byteList.get(i++) + " ");
                } else {
                    System.out.print("...");
                    i += dataLength - j;
                    break;
                }
            }
            System.out.println();
            
            // 4. Get the CRC code
            String crcCode = "";
            for(int j = 0; j < 4; j++) {
                crcCode = crcCode + byteList.get(i++) + " ";
            }
            System.out.printf("The CRC code is: %s%n", crcCode);         
        }
    }
}