
public class Decompress {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "";
		int count = 0;
		while (!BinaryStdIn.isEmpty()) {
			boolean bool = BinaryStdIn.readBoolean();
			if (bool == true) {
				StdOut.print('1');
			} else {
				StdOut.print('0');
			}
		}
	}

}
