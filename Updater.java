import twitter4j.*;

public class Updater{
   private TwitterFactory tf;
   private Twitter twitter;
   public Updater(){
      this.tf = new TwitterFactory();
      this.twitter = tf.getInstance();

   }
   // Update Status
   public boolean updateStatus(String text){
      try {
         StatusUpdate latestStatus = new StatusUpdate(text);
         Status status = this.twitter.updateStatus(latestStatus);
      } catch (TwitterException e) {
         e.printStackTrace();
         return false;
      }
      return true;
   }
}
