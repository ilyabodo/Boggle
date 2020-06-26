package boggle;

/**
 * This is the TrieNode class. The trienode class is used exclusively by the bogglesolver to create the trie structure
 * that is used to solve the boggle board
 */
public class TrieNode{
    private boolean isLeaf;
    private TrieNode[] child;

    /*
     * Constructor for TrieNode.
     */
    public TrieNode() {
        isLeaf = false;
        child = new TrieNode[Constants.ALPHABET_SIZE];
        //each element the child array is initialized to null
        for (int i = 0; i < Constants.ALPHABET_SIZE; i++) {
            child[i] = null;
        }
    }

    /*
     * Getter method for the isLeaf boolean
     */
    public boolean getLeaf() {
        return isLeaf;
    }

    /*
     * Setter method for the isLeaf boolean
     */
    public void setLeaf(boolean t) {
        isLeaf = t;
    }

    /*
     * Getter method for the child array of TrieNodes
     */
    public TrieNode getChild(int i) {
        return child[i];
    }

    /*
     * Setter method for the child array of TrieNodes
     */
    public void setChild(int i, TrieNode node) {
        child[i] = node;
    }
}
