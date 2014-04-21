import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Compress {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		AdaptiveHuffmanTree max = new AdaptiveHuffmanTree();
		String input;
		if (args.length != 0) {
			input = args[0];
		} else {
			input = BinaryStdIn.readString();
		}
		char[] inputArray = input.toCharArray();
		String output = "";
		int bitsRead = 0;
		
		//compress and determine compressed output
		for (int i = 0; i < inputArray.length; i++) {
			String tempString = "";
			
			if ((int)inputArray[i] == 10 && (i+1) == inputArray.length) break;
			if (max.characterInTree(inputArray[i])) {
				tempString = "" + max.getCodeWordFor(inputArray[i]);
				max.update(inputArray[i]);
			} else {
				tempString = "" + max.getCodeWordForNYT();
				String str = Integer.toBinaryString((int)inputArray[i]);
				while(str.length() < 8) {
					str = "0" + str;
				}
				tempString = tempString + str;
				max.update(inputArray[i]);
			}
			bitsRead += 8;
			output = output + tempString;
		}
		
		//convert output to binary for proper hexdump
		Boolean outputToBinary;
		int bitsTransmitted = 0;
		for (int i = 0; i < output.length(); i++) {
			if (output.charAt(i) == '0') {
				outputToBinary = false;
				BinaryStdOut.write(outputToBinary);
				bitsTransmitted += 1;
			} else if (output.charAt(i) == '1') {
				outputToBinary = true;
				BinaryStdOut.write(outputToBinary);
				bitsTransmitted += 1;
			}
		}
		BinaryStdOut.flush();
		BinaryStdOut.close();
		
		writeToFile(input, bitsRead, bitsTransmitted);

	}
	
	//write results to file
	private static void writeToFile(String input, int bitsRead, int bitsTransmitted) throws IOException {
		File file = new File("statistics.txt");
		
		if(!file.exists()) {
			file.createNewFile();
		}
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(input + "\n");
		bw.write("Bits Read: " + bitsRead + "\n");
		bw.write("Bits Transmitted: " + bitsTransmitted + "\n");
		
		double compression = 1- (double)bitsTransmitted/bitsRead;
		
		bw.write("Compression Ratio: " + compression + "\n");
		
		bw.close();
	}
}