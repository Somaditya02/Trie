import java.util.ArrayList;

public class RadixTrieNode {
    ArrayList<Pair<RadixTrieNode, String>> arr=new ArrayList<Pair<RadixTrieNode, String>>(26);
    int[] sizes=new int[26];
    int rank;
    int minrank;
    RadixTrieNode par;
    boolean EOW;
    RadixTrieNode(){
        for (int i = 0; i < 26; i++) {
            arr.add(null);
        }
    }
}
