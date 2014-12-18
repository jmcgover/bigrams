import twitter4j.*;
import twitter4j.conf.*;
import java.io.UnsupportedEncodingException;

public class Updater{
   private TwitterFactory tf;
   private Twitter twitter;
   public Updater(){
      ConfigurationBuilder cb = new ConfigurationBuilder();
      cb.setDebugEnabled(true)
         .setOAuthConsumerKey("Lj0mBFp3cq8ZM5zP0OdPDZuGn")
         .setOAuthConsumerSecret("Pz2fY8VCqqkAovDaJEZIANYCDQa5AAN6NiPo2eKaqCGvysn1ek")
         .setOAuthAccessToken("2902287073-gFvwqMAvUNWqEcKPIu2KXX59b4p61bDxNivgpiQ")
         .setOAuthAccessTokenSecret("XLeiKHNwVpn8Z42WpLC8fm4O2N1Z5Ps0jZQyWqhDAtf2Z");
      this.tf = new TwitterFactory(cb.build());
      this.twitter = tf.getInstance();
   }
   // Update Status
   public boolean updateStatus(String text){
      try {
         byte[] utf8TextBytes = text.getBytes("UTF8");
         String utf8TextString = new String(utf8TextBytes, "UTF8");

         StatusUpdate latestStatus = new StatusUpdate(utf8TextString);
         Status status = this.twitter.updateStatus(latestStatus);
      } catch (TwitterException e) {
         e.printStackTrace();
         return false;
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
         return false;
      }
      return true;
   }
}
