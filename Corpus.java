import java.io.*;
import java.util.*;

public class Corpus{
   private Scanner in;
   private ArrayList<Bigram> bigrams;
   // Constructor to read in files
   public Corpus(InputStream inputStream){
      this.in = new Scanner(inputStream);
      this.bigrams = null;
   }
   public ArrayList<Bigram> getBigrams(){
      if (bigrams == null) {
         this.makeBigrams();
      }
      return bigrams;
   }
   public void makeBigrams(){
      // Warning for empty input
      if (!in.hasNext()) {
         System.err.println("WTF's this empty shit?");
         System.exit(1);
      }

      // Reads in the stream
      int size = 0;
      bigrams = new ArrayList<Bigram>();
      String first = "";
      String second = "";
      if (in.hasNext()) {
         first = in.next();
      }
      if (in.hasNext()) {
         second = in.next();
      }
      bigrams.add(new Bigram(first, second));
      first = second;
      second = "";
      try{
      while (in.hasNext()) {
         second = in.next();
         bigrams.add(new Bigram(first, second));
         first = second;
         second = "";
         size++;
      }
      bigrams.add(new Bigram(first, second));
      }
      catch (OutOfMemoryError e) {
         bigrams.clear();
         System.err.println("Ran out of memory at " + size + " bigrams.");
         System.exit(1);
      }
   }
}
