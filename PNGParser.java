import java.util.ArrayList;

/**
* This class contains methods to parse and display information about PNGs.
*/
public class PNGParser {
    // Instance Variables
    private boolean isValid;

    // Default Constructor
    public PNGParser() {
        isValid = true;
    }
    
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
        
        // Changes the 'isValid' flag if the PNG does not contain the signature
        isValid = isValid && expected_signature.equals(obtained_signature);
        
        // Sets the index variable ahead to parse the chunk type
        i = 8;
        
        // Instruction counter
        int instructionCount = 1;
        
        try {
            while(i < byteList.size() && expected_signature.equals(obtained_signature)) {
                // Each step in the parser breaks down the chunk into its 4 components: the length of the data field, the chunk type, the data field, and the CRC error detecting code
                System.out.printf("----- Chunk #%s -----%n", instructionCount);
            
                // -----
                // 1. Gets the length of the chunk's data field
                int dataLength = Integer.decode("0x" + String.join("", byteList.subList(i, i + 4)));
                System.out.printf("The length of the chunk #%d's data field is: %d%n", instructionCount++, dataLength);
            
                // Sets index 4 bytes ahead
                i += 4;
            
                // -----
                // 2. Gets the Chunk Type
                String chunkType = "";
                for(int j = 0; j < 4; j++) {
                int currentChar = Integer.decode("0x" + byteList.get(i++));
                    chunkType = chunkType + (char) currentChar;
                }
            
                String chunkImportance = (Character.isUpperCase(chunkType.charAt(0))) ? "critical" : "ancillary";
                System.out.printf("The chunk type is: %s | Critical/Ancillary chunk: %s%n", chunkType, chunkImportance);
                
                // -----
                // 3. Gets the Data Field
                System.out.printf("The '%s' chunk has data field: ", chunkType);
                ArrayList<String> data = new ArrayList<String>();
                for(int j = 0; j < dataLength; j++) {
                    if(j < 10) {
                        System.out.print(byteList.get(i) + " ");
                    } else if(j == 11) {
                        System.out.print("...");
                    }
                    data.add(byteList.get(i++) + "");
                }
                System.out.println();
            
                // -----
                // 4. Get the CRC code
                String crcCode = "";
                for(int j = 0; j < 4; j++) {
                    crcCode = crcCode + byteList.get(i++) + " ";
                }
                System.out.printf("The CRC code is: %s%n", crcCode);   
                
                // ----
                // 5. Print out other information regarding the specific chunk
                System.out.printf("%n>> Chunk specific data <<%n");
                switch(chunkType){
                    case "IHDR":
                        parseIHDR(data);
                        break;
                    case "IEND":
                        System.out.printf(" > End of file.");
                        break;
                    default:
                        System.out.printf("(No additional information to display)");
                        break;
                }
                
                // line break to separate each chunk
                System.out.println("\n");
            }
        } catch (Exception e) {
            System.out.println("An error has occured.");
        }
    }
    
    // Helper method to parse the IHDR chunk 
    private void parseIHDR(ArrayList<String> dataField) {
        // Pieces of data outlined by the 'IDHR' chunk
        int width = Integer.decode("0x" + String.join("", dataField.subList(0, 4)));
        int height = Integer.decode("0x" + String.join("", dataField.subList(4, 8)));
        int bitDepth = Integer.decode("0x" + dataField.get(8));
        int colorType = Integer.decode("0x" + dataField.get(9));
        int compressionMethod = Integer.decode("0x" + dataField.get(10));
        int filterMethod = Integer.decode("0x" + dataField.get(11));
        int interlaceMethod = Integer.decode("0x" + dataField.get(12));
        
        // Creates strings that detail which type of some of the data above is specified by the PNG to determine if it is valid or not
        // 1) Width & Height; this is a numeric value so we only need to check if they are strictly greater than 0
        if(width <= 0 || height <= 0) {
            isValid = false;
        }
        
        // 2) Bit Depth; this is a numeric value, check if it is valid
        if(!(bitDepth == 1 || bitDepth == 2 || bitDepth == 4 || bitDepth == 8 || bitDepth == 16)) {
            isValid = false;
        }
        
        // 3) Color Type; this has different valid values depending on the value of 'colorType'
        String colorTypeLabel = "";
        switch(colorType) {
            case 0:
                if(bitDepth == 1 || bitDepth == 2 || bitDepth == 4 || bitDepth == 8 || bitDepth == 16) {
                    colorTypeLabel = "Greyscale";
                } else {
                    isValid = false;
                }
                break;
            case 2:
                if(bitDepth == 8 || bitDepth == 16) {
                    colorTypeLabel = "Truecolor";
                } else {
                    isValid = false;
                }
                break;
            case 3:
                if(bitDepth == 1 || bitDepth == 2 || bitDepth == 4 || bitDepth == 8) {
                    colorTypeLabel = "Indexed-Color";
                } else {
                    isValid = false;
                }
                break;
            case 4:
                if(bitDepth == 8 || bitDepth == 16) {
                    colorTypeLabel = "Greyscale (with Alpha)";
                } else {
                    isValid = false;
                }
                break;
            case 6:
                if(bitDepth == 8 || bitDepth == 16) {
                    colorTypeLabel = "Truecolor (with Alpha)";
                } else {
                    isValid = false;
                }
                break;
            default:
                colorTypeLabel = "(invalid color type)";
                isValid = false;
                break;
        }
        
        // 4) Compression Method; Per PNG specifications as of June 24th, 2025, there is only one value allowed here (namely 0, the 'deflate' compression)
        String compressionMethodLabel = "";
        switch (compressionMethod) {
            case 0:
                compressionMethodLabel = "Deflate Compression";
                break;
            default:
                isValid = false;
                break;
        }
        
        // 5) Filter Method; Per PNG specifications as of June 24th, 2025, there is only one value allowed here (namely 0, adaptive filtering)
        String filterMethodLabel = "";
        switch (filterMethod) {
            case 0:
                filterMethodLabel = "Adaptive Filtering";
                break;
            default:
                isValid = false;
                break;
        }
        
        // 6) Interlace Method
        String interlaceMethodLabel = "";
        switch (interlaceMethod) {
            case 0:
                interlaceMethodLabel = "No Interlacing";
                break;
            case 1:
                interlaceMethodLabel = "Adam7 Interlacing";
                break;
            default:
                isValid = false;
                break;
        }
        
        System.out.printf(" > Width: %d (px)%n > Height: %d (px)%n > Bit Depth: %d (bits per sample)%n > Color Type: %d (%s)%n > Compression Method: %d (%s)%n > Filter Method: %d (%s)%n > Interlace Method: %d (%s)", 
            width, 
            height, 
            bitDepth, 
            colorType, colorTypeLabel, 
            compressionMethod, compressionMethodLabel, 
            filterMethod, filterMethodLabel,
            interlaceMethod, interlaceMethodLabel
        );
    }
    
    // Put helper method for parsing 'IDAT' chunks below
}