import java.util.*;

public class Trie {
    TrieNode Root=new TrieNode();
    public void insert(String s){
        TrieNode curr=Root;
        int ind=0;
        int len=s.length();
        while (true) {
            if(ind==len){
                break;
            }
            if(curr.arr[(int) s.charAt(ind) - 97]==null){
                break;
            }
            curr = curr.arr[(int) s.charAt(ind) - 97];
            ind += 1;
        }
        while (ind<len) {
            curr.arr[(int) s.charAt(ind) - 97] = new TrieNode();
            curr.arr[(int) s.charAt(ind) - 97].par=curr;
            curr = curr.arr[(int) s.charAt(ind) - 97];
            ind += 1;
        }
        curr.EOW=true;
    }
    public boolean query(String s){
        TrieNode curr=Root;
        int ind=0;
        int len=s.length();
        while (curr!=null && ind<len){
            curr=curr.arr[(int) s.charAt(ind) - 97];
            ind+=1;
        }
        return curr!=null && curr.EOW;
    }
    public void delete(String s) throws StringNotFound{
        if(!query(s)){
            throw new StringNotFound();
        }
        else{
            TrieNode curr=Root;
            int len=s.length();
            int ind=0;
            while (ind<len){
                curr=curr.arr[(int)(s.charAt(ind))-97];
                ind+=1;
            }
            curr.EOW=false;
            boolean flag=true;
            while (flag && curr!=null){
                for (int i = 0; i < 26; i++) {
                    if (curr.arr[i]!=null){
                        flag=false;
                    }
                }
                if (flag){
                    TrieNode tt=curr.par;
                    if (tt==null)Root=null;
                    for (int i = 0; i < 26; i++) {
                        if (tt.arr[i]==curr) {
                            curr=curr.par;
                            tt.arr[i]=null;
                            break;
                        }
                    }
                    curr=curr.par;
                }
            }
        }
    }
    //This method returns a vector(sorted) of all strings in the sub-Trie of the given root
    public Vector<String> getAll(TrieNode root){
        Vector<String> vec=new Vector<String>();
        if (root==null){
            return vec;
        }
        TrieNode curr=root;
        if (curr.EOW) {
            vec.add("");
        }
        for (int i = 0; i < 26; i++) {
            Vector<String> vv = getAll(curr.arr[i]);
            for (String ss : vv) {
                vec.add((char) (i + 97) + ss);
            }
        }
        return vec;
    }
    //This method returns a vector consisting of all strings having the given prefix
    public Vector<String> AutoComplete(String pref){
        Vector<String> vec=new Vector<String>();
        TrieNode curr=Root;
        int ind=0;
        int len=pref.length();
        while (ind<len) {
            if (curr.arr[(int)(pref.charAt(ind))-97]==null){
                break;
            }
            else{
                curr=curr.arr[(int)(pref.charAt(ind))-97];
            }
            ind+=1;
        }
        if(ind==len){
            Vector<String> vvec=getAll(curr);
            for (String ss:vvec) {
                vec.add(pref+ss);
            }
        }
        return vec;
    }
    //This method sorts the given vector of strings
    public static Vector<String> sort(Vector<String> vec){
        Trie tt=new Trie();
        for (String ss:vec) {
            tt.insert(ss);
        }
        return tt.getAll(tt.Root);
    }
    public static void main(String[] args) {
    }
}
