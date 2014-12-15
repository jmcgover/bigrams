import java.io.*;
import java.util.*;

public class SentenceGenerator{
   private Corpus corpus;
   private ArrayList<Bigram> bigrams;
   private Random random;
   public SentenceGenerator(Corpus corpus){
      this.corpus = corpus;
      this.bigrams = this.corpus.getBigrams();
      this.random = null;
   }
   // Generate random
   public String generateSentence(){
      this.random = new Random();
      Bigram firstBigram = findStart();
      System.err.println("Added: " + firstBigram);

      ArrayList<Bigram> generated = completeSentence(firstBigram);
      return Bigram.toSentence(generated);
   }
   // Generate seeded
   public String generateSentence(String key){
      this.random = new Random();
      Bigram firstBigram = findStart(key);
      System.err.println("Added: " + firstBigram);

      ArrayList<Bigram> generated = completeSentence(firstBigram);
      return Bigram.toSentence(generated);
   }
   // Finish Sentence
   private ArrayList<Bigram> completeSentence(Bigram firstBigram){
      ArrayList<Bigram> generated = new ArrayList<Bigram>();
      generated.add(firstBigram);

      // Add to as long as we can link them together.
      int seed = bigrams.size();
      Bigram randomBigram = bigrams.get(random.nextInt(seed));

      System.err.println("Generating sentence...");
      while (!Bigram.isSentenceOver(generated)){
         randomBigram = bigrams.get(random.nextInt(seed));
         while ( !randomBigram.canLinkTo(generated) ) {
            randomBigram = bigrams.get(random.nextInt(seed));
         }
         System.err.println("Added: " + randomBigram);
         generated.add(randomBigram);
      }
      return generated;
   }
   // Find random first
   public Bigram findStart(){
      System.err.println("Looking for start...");
      int seed = bigrams.size();
      Bigram randomBigram = bigrams.get(random.nextInt(seed));
      while (!randomBigram.startsSentence()){
         randomBigram = bigrams.get(random.nextInt(seed));
      }
      return randomBigram;
   }
   // Find first with key
   public Bigram findStart(String key) throws NoSuchElementException{
      System.err.println("Looking for '" + key + "'...");
      ArrayList<Bigram> starters = new ArrayList<Bigram>();
      for (Bigram b : bigrams) {
         if (b.startsWith(key)) {
            starters.add(b);
         }
      }
      if (starters.size() == 0) {
         throw new NoSuchElementException();
      }
      return starters.get(random.nextInt(starters.size()));
   }
}
