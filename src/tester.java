import java.util.*;
import java.io.*;
public class tester {
    public static void main(String[] args) {
        Vector<String> vs = new Vector<String>();
        try {
            FileInputStream fstream = new FileInputStream("/Users/somadityasingh/Documents/GitHub/english-words/Pruning Radix Trie/alpha.txt");
            Scanner s = new Scanner(fstream);
            while (s.hasNextLine()) vs.add(s.nextLine());
        } catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        String[] answer = new String[vs.size()];
        int h = 0;
        for (String string : vs) {
            answer[h++] = string;
        }
        String cons = "aqwertyuiopasdfghjklzxcvbnm";
        HashMap<Integer, String> hm=prt.ArrayToMap(answer);
        prt rt = prt.FormTrie(hm);
        rt.setminrank(rt.Root);
        String queryy = "carbon";
        int k = 10;
        for (int i = 0; i < queryy.length(); i++) {
            String query = queryy.substring(0, i+1);
            long starttime = System.currentTimeMillis();
            Vector<String> pq=rt.search(query);
            long stoptime1 = System.currentTimeMillis();
            Vector<String> pq1=rt.search(10, query, hm);
            long stoptime2 = System.currentTimeMillis();
            System.out.print("Query ");
            System.out.println(query);
            System.out.println("Total number of results");
            System.out.println(pq.size());
            System.out.println("Relevant Results:");
            System.out.println(pq1.size());
            for (int j = 0; j < pq1.size(); j++) {
                System.out.println(pq1.get(j));
            }
            System.out.println("end of results\nTime taken:-");

            // System.out.println(pq1.length);
            System.out.print(stoptime1-starttime);
            System.out.print(" ");
            System.out.println(stoptime2-stoptime1);
            System.out.println("_____________________________");

        }



    }

}
