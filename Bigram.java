import java.util.*;
import java.io.*;

public class Bigram{
   private String first;
   private String second;
   public Bigram(String first, String second){
      this.first = first;
      this.second = second;
   }


   public boolean startsSentence(){
      return first.charAt(0) >= 'A' && first.charAt(0) <= 'Z';
   }
   public boolean middleOfSentence(){
      return (first.charAt(0) >= 'A' && first.charAt(0) <= 'z') || first.charAt(0) == 'I';
   }
   public boolean canLinkTo(ArrayList<Bigram> generated) {
      boolean result =  this.first.equals(generated.get(generated.size() - 1).second);
      return result;
   }
   public static boolean isSentenceOver(ArrayList<Bigram> generated){
      Bigram lastBigram = generated.get(generated.size() - 1);
      return (lastBigram.lastChar() == '.' && 
         !(lastBigram.second.equals("Mrs.") 
               || lastBigram.second.equals("Mr.")
               || lastBigram.second.equals("Ms.")))
         || lastBigram.lastChar() == '?'
         || lastBigram.lastChar() == '!';
   }

   public char firstChar(){
      return first.charAt(0);
   }
   public char lastChar(){
      return second.charAt(second.length() - 1);
   }
   public String toString() {
      return first + " " + second;
   }
   public static String toSentence(ArrayList<Bigram> generated){
      String sentence = "";
      if (!generated.isEmpty()) {
         for (Bigram buh : generated) {
            sentence += buh.first + " ";
         }
         sentence += generated.get(generated.size() - 1).second;
      }
      return sentence;

   }

   public static void main(String[] args){
      // Arg Check
      if (args.length != 1) {
         System.err.println("Please provide a file to save results to.");
         System.err.println("usage: java Bigrams resultsFilename < textFileToPipe");
         System.exit(1);
      }

      // Make PrintStream
      String inputFilename = args[0];
      String outputFilename = "result_" + inputFilename;
      Scanner in = null;
      try {
         in = new Scanner(new File(inputFilename));
      }
      catch (FileNotFoundException e) {
         System.err.println("Couldn't read from '" + inputFilename + "'.");
      }

      // Warning for empty input
      if (!in.hasNext()) {
         System.err.println("WTF's this empty shit?");
         System.exit(1);
      }

      // Make bigrams
      System.err.println("Makin bigrams...");
      ArrayList<Bigram> bigrams = new ArrayList<Bigram>();
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
      while (in.hasNext()) {
         second = in.next();
         bigrams.add(new Bigram(first, second));
         first = second;
         second = "";
      }
      bigrams.add(new Bigram(first, second));
//      for (Bigram buh : bigrams) {
//         System.err.println("'" + buh + "'");
//      }

      // Generate sentence
      System.err.println("Generating sentence...");
      Random random = new Random();
      int size = bigrams.size();
      ArrayList<Bigram> generated = new ArrayList<Bigram>();
      Bigram randomBigram = bigrams.get(random.nextInt(size));
      // Find beginning
      System.err.println("Looking for start...");
      while (!randomBigram.startsSentence()) {
         randomBigram = bigrams.get(random.nextInt(size));
      }
      generated.add(randomBigram);
         System.err.println("Added: " + randomBigram);

      // Add to as long as we can link them together.
      randomBigram = bigrams.get(random.nextInt(size));
      System.err.println("Generating sentence...");
      while (!isSentenceOver(generated)){
         randomBigram = bigrams.get(random.nextInt(size));
         while ( !randomBigram.canLinkTo(generated)) {
            randomBigram = bigrams.get(random.nextInt(size));
         }
         System.err.println("Added: " + randomBigram);
         generated.add(randomBigram);
      }
//      for (Bigram buh : generated) {
//         System.err.print(buh + " ");
//      }

      String sentence = Bigram.toSentence(generated);
      System.err.println();
      System.err.println(sentence);
//      outStream.println(sentence);
      try {
         Date date = new Date();
          PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilename, true)));
          out.println(date);
          out.println(sentence);
          out.println();
          out.close();
      } catch (IOException e) {
          //exception handling left as an exercise for the reader
          System.err.println("couldn't append to '" + outputFilename + "' for some reason");
      }
   }
}
