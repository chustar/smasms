import twitter4j.*;

public class Scrape {
	public static void main(String[] args) throws Exception{
		Scrape.searchTweets("microsoft");
		//Scrape.pullStream("microsoft");
	}

	public static void pullStream(final String searchString) throws Exception{
	    StatusListener listener = new StatusListener(){
	        public void onStatus(Status status) {
	        	if (status.getText().contains(searchString)) {
	        		System.out.println(status.getUser().getName() + " : " + status.getText());
	        	}
	        }
	        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
	        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
	        public void onException(Exception ex) {
	            ex.printStackTrace();
	        }
			public void onScrubGeo(long arg0, long arg1) { }
	    };
	    TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
	    twitterStream.addListener(listener);
	    twitterStream.sample();
	}
	
	public static void searchTweets(String searchString) throws Exception {
		Twitter twitter = new TwitterFactory().getInstance();
		QueryResult result;
		Query query = new Query(searchString);
		query.setRpp(100);
		for(int i = 1; i < 16; i++) {
			System.out.println("CURRENT PAGE: " + i);
			query.setPage(i);
			result = twitter.search(query);
			for (Tweet tweet : result.getTweets()) {
				System.out.println(tweet.getFromUser() + ":" + tweet.getText());
			}			
		}
	}

}
