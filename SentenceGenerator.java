import java.util.*;
import java.io.*;

public class SentenceGenerator{
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
      printOption("[-h  | --help]","\t\tdisplays this help and exits");
      printOption("[-i | --in]","\t\tloads text from stdin");
      printOption("[<-l | --limit> numChars]","restricts length to numChars");
      printOption("[<-t  | --hashtag> #tag]","adds ' #tag' to end of sentence");
      printOption("[<-r  | --results> filename]","explicitly name the results file");
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
            startingWord = args[++i];
            System.err.println("Starts with '" + startingWord + "'.");
         }
      }
      }
      catch(IndexOutOfBoundsException e){
         System.err.println(args[args.length - 1] + " needs an argument.");
         System.exit(1);
      }
      // Read input filename
      try{
         inputFilename = args[args.length - 1];
      }
      catch(IndexOutOfBoundsException e){
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

      // Make PrintStream
      Scanner in = null;
      if (readStdin) {
         in = new Scanner(System.in);
      }
      else {
         try {
            in = new Scanner(new File(inputFilename));
         }
         catch (FileNotFoundException e) {
            printError("Couldn't read from '" + inputFilename + "'.",1);
         }
      }

      // Warning for empty input
      if (!in.hasNext()) {
         printError("WTF's this empty shit?",1);
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
      try{
      while (in.hasNext()) {
         second = in.next();
         bigrams.add(new Bigram(first, second));
         first = second;
         second = "";
      }
      bigrams.add(new Bigram(first, second));
      }
      catch (OutOfMemoryError e) {
         printError("Ran out of memory at " + bigrams.size() + " bigrams.",1);
      }

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
      while (!Bigram.isSentenceOver(generated)){
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
