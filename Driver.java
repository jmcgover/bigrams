import java.util.*;
import java.io.*;

public class Driver{
   // Usage
   public static void printUsage(String msg){
      System.err.println(msg);
      printUsage();
   }
   public static void printUsage(){
      String usage = "";
      usage += "java";
      usage += " ";
      usage += "Bigram";
      usage += " ";
      usage += "[options...]";
      usage += " ";
      usage += "inputFilename";
      System.err.println(usage);
      System.err.println();
      System.err.println("Options:");
      printOption("[-h   | --help]","\t\tdisplays this help and exits");
      printOption("[-i   | --in]","\t\tloads text from stdin");
      printOption("[<-l  | --limit> numChars]","restricts length to numChars");
      printOption("[<-t  | --hashtag> \'#tag\']","adds ' #tag' to end of sentence");
      printOption("[<-r  | --results> filename]","explicitly name the results file");
      printOption("[<-s  | --starts-with> word]","begins sentence with 'word' explicitly");
      System.exit(1);
   }
   public static void printOption(String flag, String explanation){
      System.err.printf("\t%s\t%s\n",flag,explanation);
   }
   // Error handling
   public static void printError(String msg, int err){
      System.err.println(msg);
      System.exit(err);
   }
   // MAIN
   public static void main(String[] args){
      // Arg Check
      if (args.length < 1) {
         printUsage("Please provide at least an input filename for saving the results.");
      }
      boolean readStdin = false;
      boolean limitLength = false;
      boolean addHashtag = false;
      boolean nameResults = false;
      boolean startsWith = false;
      int charLimit = -1;
      String hashtag = "";
      String inputFilename = null;
      String outputFilename = null;
      String startingWord = null;

      // Args Parsing
      int i = 0;
      try{
         System.err.println("You provided " + args.length + " arguments.");
         for (i = 0; i < args.length - 1; i++) {
            // Help
            if (args[i].equals("-h") || args[i].equals("--help")) {
               printUsage();
            }
            // Stdin read
            if (args[i].equals("-i") || args[i].equals("--in")) {
               readStdin = true;
               System.err.println("Read from stdin.");
            }
            // Limit characters
            if (args[i].equals("-l") || args[i].equals("--limit")) {
               limitLength = true;
               charLimit = Integer.parseInt(args[++i]);
               System.err.println("Limit characters to " + charLimit + ".");
            }
            // Append hashtag
            if (args[i].equals("-t") || args[i].equals("--hashtag")) {
               addHashtag = true;
               hashtag = " " + args[++i];
               System.err.println("Add hashtag '" + hashtag + "'.");
            }
            // Explicit results
            if (args[i].equals("-r") || args[i].equals("--results")) {
               nameResults = true;
               outputFilename = args[++i];
               System.err.println("Add hashtag '" + hashtag + "'.");
            }
            // Starts with
            if (args[i].equals("-s") || args[i].equals("--starts-with")) {
               startsWith = true;
               startingWord = args[++i];
               System.err.println("Starts with '" + startingWord + "'.");
            }
         }
      } catch(IndexOutOfBoundsException e){
         System.err.println(args[args.length - 1] + " needs an argument.");
         System.exit(1);
      }

      // Another Help Check
      if (args[i].equals("-h") || args[i].equals("--help")) {
         printUsage();
      }
      // Read input filename
      try{
         inputFilename = args[args.length - 1];
      } catch(IndexOutOfBoundsException e){
         printError("Please provide a filename.",1);
      }
      // Create output filename
      if (nameResults) {
         // Check if messes up input
         if (outputFilename.equals(inputFilename)) {
            printError("Please do NOT name the output the same as the input.",1);
         }
      }
      else {
         outputFilename = "result_" + inputFilename;
      }

      // Make Appropriate PrintStream
      Corpus corpus = null;
      if (readStdin) {
         corpus = new Corpus(System.in);
      }
      else {
         try {
            corpus = new Corpus(new FileInputStream(new File(inputFilename)));
         } catch (FileNotFoundException e) {
            printError("Couldn't read from '" + inputFilename + "'.",1);
         }
      }

      // Make bigrams
      System.err.println("Makin bigrams...");
      corpus.makeBigrams();

      // Generate sentence
      System.err.println("Generating sentences...");
      SentenceGenerator senGen = new SentenceGenerator(corpus);
      String sentence = null;
      do {
         // Start with seed string
         if (startsWith) {
            try{
               sentence = senGen.generateSentence(startingWord);
            } catch (NoSuchElementException e) {
               System.err.println("Could not find any bigrams starting with '" + startingWord + "'.");
               System.exit(0);
            }
         }
         // Randomly Generate
         else {
            sentence = senGen.generateSentence();
         }

         // Add (potentially empty) hashtag
         sentence += hashtag;
         System.err.println();
         System.err.println(sentence);
         if (limitLength) {
            System.err.println("Length: " + sentence.length());
         }
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
         } // CATCH
      } while(limitLength && sentence.length() > charLimit);
   }
}
