import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;

public class TwitterScraper {
	public static void main(String[] args) throws Exception {
		AmazonSimpleDB sdb = new AmazonSimpleDBClient(
				new PropertiesCredentials(SimpleDBSample.class
						.getResourceAsStream("AwsCredentials.properties")));
		try {
			String myDomain = "Tweets";
			System.out.println("Creating domain called " + myDomain + ".\n");
			sdb.createDomain(new CreateDomainRequest(myDomain));

			List<ReplaceableItem> tweets = searchTweets("microsoft");
			for(ReplaceableItem tweet : tweets) {
				sdb.putAttributes(new PutAttributesRequest(myDomain, tweet.getName(), tweet.getAttributes()));				
				System.out.println("Saving: " + tweet.getName());
			}
		} catch (AmazonServiceException ase) {
			System.out
					.println("Caught an AmazonServiceException, which means your request made it "
							+ "to Amazon SimpleDB, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
		} catch (AmazonClientException ace) {
			System.out
					.println("Caught an AmazonClientException, which means the client encountered "
							+ "a serious internal problem while trying to communicate with SimpleDB, "
							+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
		}
	}

	public static List<ReplaceableItem> searchTweets(String searchString) throws Exception {
        List<ReplaceableItem> tweets = new ArrayList<ReplaceableItem>();

		Twitter twitter = new TwitterFactory().getInstance();
		QueryResult result;
		Query query = new Query(searchString);
		query.setRpp(100);
		for (int i = 1; i < 16; i++) {
			query.setPage(i);
			result = twitter.search(query);
			for (Tweet tweet : result.getTweets()) {
				tweets.add(new ReplaceableItem(tweet.getFromUser() + ":" + tweet.getCreatedAt().toString()).withAttributes(
						new ReplaceableAttribute("Username", tweet.getFromUser(), true),
						new ReplaceableAttribute("Tweet", tweet.getText(), true),
						new ReplaceableAttribute("Date", tweet.getCreatedAt().toString(), true)));
				System.out.println(tweet.getFromUser() + ":" + tweet.getText());
			}
		}
		return tweets;
	}
}