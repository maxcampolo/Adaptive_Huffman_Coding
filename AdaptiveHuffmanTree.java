public class AdaptiveHuffmanTree{
     Node[] rlo;          //list of nodes in the tree in reverse-level order rlo[255], rlo[254], ...
     Node NYT;            //special node representing unseen characters
     Node[] characters;   //pointers to the nodes in the tree holding each character (if null then the character has not been seen)
     int size;            //number of Nodes in the tree
     Node root;
     Node newNode;
     Node charNode;

     public AdaptiveHuffmanTree(){
          characters = new Node[256];
          rlo = new Node[256];

          for(int k=0; k<256; k++){
              characters[k] = null;
          }

          NYT = new Node(0);
          NYT.place = 255;               //NYT is going to be placed at the root of the tree
          NYT.weight = 0;                //NYT has weight 0
          NYT.label = '0';               //NYT is always a leftchild
          NYT.leaf = true;               //NYT is always a leaf
          rlo[NYT.place] = NYT;          //place NYT at the root of the tree
          NYT.parent = null;             //the root of the tree has no parent
          size = 1;
     }

   /*
    * Update the adaptive Huffman tree after inserting character c. This is the UPDATE procedure discussed in class.
    */
   public void update(char c){
	   Node toUpdate = null;
	   
	   //when adding first node
	   if (size == 1) {
		   addFirstNode(c);
	   } else if (!characterInTree(c)) {
		   newNode = new Node(1);
		   newNode.place = NYT.place;
		   newNode.parent = NYT.parent;
		   newNode.leaf = false;
		   rlo[NYT.place] = newNode;
		   newNode.label = '0';
		   size++;
		   
		   charNode = new Node(1, c);
		   charNode.place = newNode.place - 1;
		   rlo[charNode.place] = charNode;
		   characters[charNode.place] = charNode;
		   charNode.parent = newNode;
		   charNode.label = '1';
		   size++;
		   
		   NYT.place -= 2;
		   rlo[NYT.place] = NYT;
		   NYT.parent = newNode;
		   
		   toUpdate = newNode.parent;
	   } else {
		   toUpdate = findNode(c);
	   }
	   
	   while (toUpdate != null) {
		  // if (!satisfiesSiblingProperty()) {
			   Node nodeToSwap = findNodeToSwap(toUpdate.weight);
			   if (nodeToSwap != null && toUpdate != nodeToSwap && toUpdate.parent != nodeToSwap && nodeToSwap.parent != toUpdate) {
				   swap(toUpdate, nodeToSwap);
			   }
		  // }
		   toUpdate.weight++;
		   toUpdate = toUpdate.parent;
	   }   
   }
   
   private void addFirstNode(char c) {
	   newNode = new Node(1);
	   newNode.place = NYT.place;
	   newNode.parent = null;
	   newNode.leaf = false;
	   rlo[NYT.place] = newNode;
	   root = newNode;
	   size ++;
	   
	   charNode = new Node(1, c);
	   charNode.place = NYT.place - 1;
	   charNode.label = '1';
	   charNode.parent = newNode;
	   rlo[charNode.place] = charNode;
	   characters[charNode.place] = charNode;
	   size++;
	   
	   NYT.place -= 2;
	   rlo[NYT.place]= NYT;
	   NYT.parent = newNode;
   }
   
   private Node findNode(char c) {
	   for (int i = 0; i < characters.length; i++) {
		   if (characters[i] != null && characters[i].character == c) {
			   return characters[i];
		   }
	   }
	   return null;
   }


   /*
    * Swap Nodes u and v in the reverse-level ordered array. Note: Node u is at index u.place!
    */
   private void swap(Node u, Node v){
	   //find indexes
	   int uIndex = u.place;
	   int vIndex = v.place;
	   
	   //get parents
	   Node parentOfU = u.parent;
	   Node parentOfV = v.parent;
	   
	   //swap positions
	   rlo[uIndex] = v;
	   rlo[vIndex] = u;
	   
	   //switch places
	   u.place = vIndex;
	   v.place = uIndex;
	   
	   //if nodes have different parents
	   if (parentOfU != parentOfV) {
		   if (u.label == '0') {
			   v.label = '0';
		   } else {
			   v.label = '1';
		   }
		   
		   if (v.label == '0') {
			   u.label = '0';
		   } else {
			   u.label = '1';
		   }
	   } else {
		   v.label = '0';
		   u.label = '1';
	   }
	   u.parent = parentOfV;
	   v.parent = parentOfU;
   }

   /*
    * Return true if character c has been seen, otherwise, return false.
    */
   public boolean characterInTree(int  c){
	   for (int i = 0; i < characters.length; i++) {
		   if (characters[i] != null && c == characters[i].character) {
			   return true;
		   }
	   }
	   return false;
   }


   /*
    * Return true if the reverse-level order traversal is a monotonically decreasing sequence, otherwise, return false.
    */
   private boolean satisfiesSiblingProperty(){
	   for(int i = rlo.length - 1; i > 0; i--) {
		   if (rlo[i-1] != null && rlo[i].weight < rlo[i-1].weight){
			   return false;
		   }
	   }
	   return true;
   }
   
   private Node findNodeToSwap(int weight) {
	   //find node with same weight
	   for (int i = rlo.length -1; i >=0; i--) {
		   if (rlo[i] != null && rlo[i].weight == weight) {
			   return rlo[i];
		   }
	   }
	   StdOut.println("Returning Null");
	   return null;
   }


   /*
    * Return the sequence of labels (characters) from the root to the NYT node.
    */
   public StringBuffer getCodeWordForNYT(){
	   StringBuffer sb = new StringBuffer();
	   Node n = NYT;
	   
	   while (n.parent != null) {
		   sb.append(n.label);
		   n = n.parent;
	   }
       return sb.reverse();   //change to the correct return value.
   }


   /*
    * Return the sequence of labels (characters from the root to the Node for character c.
    */
   public StringBuffer getCodeWordFor(char c){
	   StringBuffer sb = new StringBuffer();
	   Node n = findNode(c);
	   
	   while (n != null) {
		   sb.append(n.label);
		   n = n.parent;
	   }
       return sb.reverse();    //change to the correct return value.
   }


   /*
    * return the reference to the root node.
    */
   public Node root(){
      return root;   //change to the correct return value.
   }


   public int size(){
      return size;
   }


   /*
    * I've provided this to help debug your tree.
    */
   public String toString(){
       String result = "[";

       for(int k=255; k>=0 ; k--){
           if(rlo[k] != null)
                result += "(" + rlo[k].weight + "," + rlo[k].place + "," + rlo[k].character + ") ";
       }

       return result + "]\nsize = " + size;
   }
   
   public static void main (String[] args) {
	   AdaptiveHuffmanTree max = new AdaptiveHuffmanTree();
	   String input;
	   if (args.length != 0) {
		   input = args[0];
	   } else {
		   input = BinaryStdIn.readString();
	   }
	   char[] inputArray = input.toCharArray();
	   
	   for (int i = 0; i < inputArray.length; i++) {
		   max.update(inputArray[i]);
	   }
	   /*
	   max.update('a');
	   max.update('a');
	   max.update('r');
	   max.update('d');
	   max.update('v');
	   max.update('a');
	   max.update('d');
	   max.update('r');
	   
	   max.update('a');
	   max.update('b');
	   max.update('b');
	   */
	   
	   StdOut.println(max.toString());
	   //StdOut.println(max.getCodeWordForNYT());
	   //StdOut.println(max.getCodeWordFor(size));
	   
   }
}
