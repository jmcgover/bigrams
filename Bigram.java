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
   public boolean startsWithSoft(String key){
      return first.toLowerCase().startsWith(key.toLowerCase());
   }
   public boolean startsWith(String key){
      return first.startsWith(key);
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
}
