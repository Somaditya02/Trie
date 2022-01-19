import java.util.Collections;
import java.util.Set;
import java.util.Vector;
import java.util.*;
public class RadixTrie {
    RadixTrieNode Root=new RadixTrieNode();
    public void insert(String s, int i){
        RadixTrieNode curr=Root;
        int ind=0;
        int len=s.length();
        while (ind<len){
            if (curr.arr.get((int)s.charAt(ind)-97)!=null){
                RadixTrieNode rr=curr.arr.get((int)s.charAt(ind)-97).first;
                String pref=curr.arr.get((int)s.charAt(ind)-97).second;
                int ind2=0;
                int len2=pref.length();
                while (ind<len && ind2<len2 && (s.charAt(ind)==pref.charAt(ind2))){
                    ind+=1;
                    ind2+=1;
                }
                if (ind==len && ind2==len2){
                    rr.EOW=true;
                    rr.rank=i;
                }
                else if(ind==len){
                    RadixTrieNode rtn=new RadixTrieNode();
                    String pp=pref.substring(ind2);
                    curr.arr.set((int)pref.charAt(0)-97, new Pair(rtn, pref.substring(0, ind2)));
                    curr.sizes[(int)pref.charAt(0)-97]=ind2;
                    curr=rtn;
                    rtn.EOW=true;
                    rtn.rank=i;
                    curr.arr.set((int)pref.charAt(ind2)-97, new Pair(rr, pp));
                    curr.sizes[(int)pref.charAt(ind2)-97]=len2-ind2;
                }
                else if(ind2==len2){
                    curr=rr;
                }
                else{
                    curr.sizes[(int)pref.charAt(0)-97]=ind2;
                    RadixTrieNode mid=new RadixTrieNode();
                    curr.arr.set((int)pref.charAt(0)-97, new Pair(mid, pref.substring(0, ind2)));
                    curr=mid;
                    curr.sizes[(int)pref.charAt(ind2)-97]=len2-ind2;
                    RadixTrieNode rtn1=new RadixTrieNode();
                    curr.arr.set((int)s.charAt(ind)-97,new Pair(rtn1, s.substring(ind)));
                    curr.sizes[(int)s.charAt(ind)-97]=len-ind;
                    curr.arr.set((int)pref.charAt(ind2)-97, new Pair(rr, pref.substring(ind2)));
                    rtn1.EOW=true;
                    rtn1.rank=i;
                }
            }
            else{
                RadixTrieNode rtn=new RadixTrieNode();
                rtn.EOW=true;
                rtn.rank=i;
                curr.arr.set((int)s.charAt(ind)-97, new Pair(rtn, s.substring(ind)));
                curr.sizes[(int)s.charAt(ind)-97]=len-ind;
                break;
            }
        }
    }
    public boolean query(String s){
        RadixTrieNode curr=Root;
        int ind=0;
        int len=s.length();
        while (ind<len){
            if (curr.arr.get((int)s.charAt(ind)-97)!=null){
                String pref=curr.arr.get((int)s.charAt(ind)-97).second;
                int temp=0;
                int len2=curr.sizes[(int)s.charAt(ind)-97];
                while (ind<len && temp<len2 && s.charAt(ind)==pref.charAt(temp)){
                    temp+=1;
                    ind+=1;
                }
                if (temp<len2){
                    return false;
                }
                else if (ind<len) {
                    curr = curr.arr.get((int) pref.charAt(0) - 97).first;
                }
                else{
                    return curr.arr.get((int) pref.charAt(0) - 97).first.EOW;
                }
            }
            else{
                return false;
            }
        }
        return true;
    }
    public Vector<String> getAll(RadixTrieNode root){
        Vector<String> vec=new Vector<String>();
        if (root==null){
            return vec;
        }
        if (root.EOW){
            vec.add("");
        }
        for (int i = 0; i < 26; i++) {
            if (root.arr.get(i)!=null) {
                Vector<String> vvec = getAll(root.arr.get(i).first);
                for (String ss : vvec) {
                    vec.add(root.arr.get(i).second + ss);
                }
            }
        }
        return vec;
    }
    public void delete(String s) throws StringNotFound{
        if (!query(s)) throw new StringNotFound();
        else{
            RadixTrieNode curr=Root;
            int ind=0;
            int len=s.length();
            RadixTrieNode prev=null;
            int temp=ind;
            while (ind<len){
                temp=ind;
                ind+=curr.sizes[(int)s.charAt(ind)-97];
                prev=curr;
                curr=curr.arr.get((int)s.charAt(temp)-97).first;
            }
            for (int i = 0; i < 26; i++) {
                if (curr.arr.get(i)!=null){
                    curr.EOW=false;
                    return;
                }
            }
            prev.arr.set((int)s.charAt(temp)-97, null);
        }
    }

    public Vector<String> AutoComplete(String s){
        Vector<String> ans=new Vector<String>();
        int ind=0;
        int len=s.length();
        RadixTrieNode curr=Root;
        String s0="";
        while(true) {
            if (len-ind > curr.sizes[(int) (s.charAt(ind)) - 97]) {
                if (s.substring(0, curr.sizes[(int) (s.charAt(ind)) - 97]).equals(curr.arr.get((int) (s.charAt(ind)) - 97).second)) {
                    s0+=curr.arr.get((int) (s.charAt(ind)) - 97).second;
                    int temp=ind;
                    ind+=curr.sizes[(int) (s.charAt(ind)) - 97];
                    curr = curr.arr.get((int) (s.charAt(temp)) - 97).first;
                }
                else{
                    return ans;
                }
            }
            else{
                for (int i = ind; i < len; i++) {
                    if (s.charAt(i)!=curr.arr.get((int) (s.charAt(ind)) - 97).second.charAt(i-ind)){
                        return ans;
                    }
                }
                s0+=curr.arr.get((int)s.charAt(ind)-97).second;
                if (len-ind==curr.sizes[(int) (s.charAt(ind)) - 97] && curr.arr.get((int) (s.charAt(ind)) - 97).first.EOW)ans.add(s0);
                for (String ss:getAll(curr.arr.get((int) (s.charAt(ind)) - 97).first)) {
                    ans.add(s0+ss);
                }
                return ans;
            }
        }
    }
    public void setminrank(RadixTrieNode root){
        for (int i = 0; i < 26; i++) {
            if (root.arr.get(i)!=null){
                setminrank(root.arr.get(i).first);
            }
        }
        if (root.EOW){
            root.minrank=root.rank;
        }
        for (int i = 0; i < 26; i++) {
            if (root.arr.get(i)!=null){
                root.minrank=Math.min(root.minrank, root.arr.get(i).first.minrank);
            }
        }
    }
    //Assumption: the strings are ranked on the basis of order of insertion.
    public void most_relev(int k, RadixTrieNode root, Set<Integer> ss){
        if (root==null){
            return;
        }
        if (root.EOW){
            if (ss.size()<k){
                ss.add(root.rank);
            }
            else if(root.rank< Collections.max(ss)){
                ss.remove(Collections.max(ss));
                ss.add(root.rank);
            }
        }
        for (int i = 0; i < 26; i++) {
            if (root.arr.get(i)!=null) {
                if (ss.size()<k) {
                    most_relev(k, root.arr.get(i).first, ss);
                }
                else if (root.arr.get(i).first.minrank<Collections.max(ss)){
                    most_relev(k, root.arr.get(i).first, ss);
                }
            }
        }
    }
    // Assumption: There is at least one string in the RT having the given prefix.
    public RadixTrieNode reach(String pref){
        RadixTrieNode curr=Root;
        int ind=0;
        int len=pref.length();
        while (ind<len){
            if (curr.sizes[(int)pref.charAt(ind)-97]>=len-ind){
                return curr.arr.get((int)pref.charAt(ind)-97).first;
            }
            else{
                int temp=ind;
                curr=curr.arr.get((int)pref.charAt(ind)-97).first;
                ind+=curr.sizes[(int)pref.charAt(temp)-97];
            }
        }
        return curr;
    }
    public static void main(String[] args) {
        RadixTrie rt=new RadixTrie();
        HashMap<Integer, String > hm=new HashMap<Integer, String>();
        rt.insert("som", 1);
        hm.put(1, "som");
        rt.insert("dhruv" ,2);
        hm.put(2, "dhruv");
        rt.insert("dhrub", 3);
        hm.put(3, "dhrub");
        rt.insert("bhaibhav", 4);
        hm.put(4, "bhaibhav");
        rt.insert("hemank", 5);
        hm.put(5, "hemank");
        rt.insert("hemnak", 6);
        hm.put(6, "hemnak");
        rt.insert("hamank", 7);
        hm.put(7, "hamank");
        rt.insert("soma", 8);
        hm.put(8, "soma");
        rt.insert("somad", 9);
        hm.put(9, "somad");
        rt.insert("somadi", 10);
        hm.put(10, "somadi");
        rt.insert("somadit", 11);
        hm.put(11, "somadit");
        rt.insert("somaditya", 12);
        hm.put(12, "somaditya");
        rt.insert("somaa", 13);
        hm.put(13, "somaa");
        rt.insert("somaaad", 14);
        hm.put(14, "somaaad");
        System.out.println(rt.query("hamank"));
        System.out.println(rt.query("hama"));
        System.out.println(rt.query("hem"));
        System.out.println(rt.query("dhrub"));
        System.out.println(rt.getAll(rt.Root));
        System.out.println(rt.AutoComplete("hem"));
        System.out.println(rt.query("som"));
        System.out.println(rt.AutoComplete("so"));
        TreeSet<Integer> ss=new TreeSet<Integer>();
        rt.setminrank(rt.Root);
        rt.most_relev(6, rt.reach("so"), ss);
        for (int rank: ss) {
            System.out.print(hm.get(rank)+" ");
        }
    }
}
