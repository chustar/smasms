package in.smasms;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.IOException;
import java.lang.Math;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;

/**
 * Use analyze data in simpleDB and assign each value with negative, neutral, positive value 
 * will add a column named moodVal with values [-1, 0, 1] [negative, neutral, positive]
 * then counts are analyzed and available as public methods
 * Example:
 * 		AnalysisOfDB myDBAnalyzer = new AnalysisOfDB("Tweets", "Tweet");
 *		int pos = myDBAnalyzer.getPositiveCount();
 *		int neu = myDBAnalyzer.getNeutralCount();
 *		int neg = myDBAnalyzer.getNegativeCount();
 *		int tot = myDBAnalyzer.getTotalCount();
 *		
 */
public class AnalysisOfDB {
	private static final int NEGATIVE = -1;
	private static final int NEUTRAL = 0;
	private static final int POSITIVE = 1;
	private static String myDomain = "Tweets";
	private static String colName = "Tweet";
    private static int negCount;
    private static int posCount;
    private static int neuCount;
    private static int totalCount;
    private static AmazonSimpleDB sdb;
	
    /**
     * Constructor with defined AmazonSimpleDB and supplied domain name and colName to analyze
     * @param givenDB
     * @param givenDomain
     * @param givenColName
     */
    AnalysisOfDB(AmazonSimpleDB givenDB, String givenDomain, String givenColName){
    	//System.out.println("in testDb constructor with defined DB and domain");
    	sdb = givenDB;
    	myDomain = givenDomain;
    	colName = givenColName;
    	setup();
    }
    
    /**
     * Constructor with supplied domain name and colName to analyze, using default AmazonSimpleDB
     * @param givenDomain
     * @param givenColName
     */
    AnalysisOfDB(String givenDomain, String givenColName){
        try {
			sdb = new AmazonSimpleDBClient(new PropertiesCredentials(
			        AnalysisOfDB.class.getResourceAsStream("AwsCredentials.properties")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	myDomain = givenDomain;
    	colName = givenColName;
    	setup();
    }
    
    /**
     * Constructor using default AmazonSimpleDB and Domain 
     */
    AnalysisOfDB(){
    	//System.out.println("in testDb constructor with default DB and domain");
        try {
			sdb = new AmazonSimpleDBClient(new PropertiesCredentials(
			        AnalysisOfDB.class.getResourceAsStream("AwsCredentials.properties")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        setup();
    }
    
    /**
     * Will analyze given string, CURRENTLY USING RANDOM ASSIGNMENT
     */
    private static void setup(){
    	System.out.println("in testDb setup()");
    	 try {
	        String selectExpression2 = "select " + colName + " from `" + myDomain + "`";
	        //System.out.println("Selecting =: " + selectExpression2 + "\n");
	        SelectRequest selectRequest2 = new SelectRequest(selectExpression2);
	        int moodValue = 0;
	    	for (Item item : sdb.select(selectRequest2).getItems()) {
	            //System.out.println("  rg_Item");
	            //System.out.println("        rg_Name: " + item.getName());
	            for (Attribute attribute2 : item.getAttributes()) {
	                //System.out.println("       rg_Attribute");
	                //System.out.println("        rg_Name:  " + attribute2.getName());
	                //System.out.println("        rg_Value: " + attribute2.getValue());
	                moodValue = GetMood(attribute2.getValue());
	            }
	            
	            //set the moodValue
	            List <ReplaceableItem> moodValInput = new ArrayList<ReplaceableItem>();
	            moodValInput.add(new ReplaceableItem(item.getName()).withAttributes(
	                    new ReplaceableAttribute("moodVal", ""+moodValue, true)));
	            sdb.batchPutAttributes(new BatchPutAttributesRequest(myDomain, moodValInput));
	            
	            
	        }
	    	
	    	getNeutralCount(sdb);
	    	getPositiveCount(sdb);
	    	getNegativeCount(sdb);
	    	totalCount = negCount + posCount + neuCount;
	    	
	    	
	    	
    	 } catch (AmazonServiceException ase) {
             System.out.println("Caught an AmazonServiceException, which means your request made it "
                     + "to Amazon SimpleDB, but was rejected with an error response for some reason.");
             System.out.println("Error Message:    " + ase.getMessage());
             System.out.println("HTTP Status Code: " + ase.getStatusCode());
             System.out.println("AWS Error Code:   " + ase.getErrorCode());
             System.out.println("Error Type:       " + ase.getErrorType());
             System.out.println("Request ID:       " + ase.getRequestId());
    	 }
    }
    
  

    /**
     * input string to analyze, currently only return a random value of [-1, 0, 1]
     * @param s
     * @return
     */
    private static int GetMood(String s){
    		Random rn = new Random();
    		int r = Math.abs(rn.nextInt());
    		//System.out.println("in GetMood function random number :" + r);
    		return ((r % 3) -1 );
    }
    
    private static void getNeutralCount(AmazonSimpleDB db){
    	String selectExpression2 = "select moodVal from `" + myDomain + "` where moodVal = '0'";
        //System.out.println("Selecting =: " + selectExpression2 + "\n");
        SelectRequest selectRequest2 = new SelectRequest(selectExpression2);
        int count = 0;
        for (Item item : db.select(selectRequest2).getItems()){
        	count ++;

            }
        neuCount = count;
    }
    
    /**
     * number of neutral posts
     * @return
     */
    public static int getNeutralCount(){
    	return (neuCount);
    }
        
        
    private static void getNegativeCount(AmazonSimpleDB db){
    	String selectExpression2 = "select moodVal from `" + myDomain + "` where moodVal = '-1'";
        //System.out.println("Selecting =: " + selectExpression2 + "\n");
        SelectRequest selectRequest2 = new SelectRequest(selectExpression2);
        int count = 0;
        for (Item item : db.select(selectRequest2).getItems()){
        	count ++;
        }
        negCount = count;
    }
    
    /**
     * number of negative posts
     * @return
     */
    public static int getNegativeCount(){
    	return (negCount);
    }
    
    private static void getPositiveCount(AmazonSimpleDB db){
    	String selectExpression2 = "select moodVal from `" + myDomain + "` where moodVal = '1'";
        //System.out.println("Selecting =: " + selectExpression2 + "\n");
        SelectRequest selectRequest2 = new SelectRequest(selectExpression2);
        int count = 0;
        for (Item item : db.select(selectRequest2).getItems()){
        	count ++;
        }       
        posCount = count;
    }
    
    /**
     * number of positive posts
     * @return
     */
    public static int getPositiveCount(){
    	return (posCount);
    }

    /**
     * return the total number of analyzed values
     * @return
     */
    public static int getTotalCount(){
    	return (totalCount);
    }
    
    /* 
    public static void main(String[] args) throws Exception {
    	
        sdb = new AmazonSimpleDBClient(new PropertiesCredentials(
                AnalysisOfDB.class.getResourceAsStream("AwsCredentials.properties")));

        try {
        	setup();
            // Selecting from domain. and adding attribute
            String selectExpression2 = "select Subcategory from `" + myDomain + "`";
            //System.out.println("Selecting =: " + selectExpression2 + "\n");
            SelectRequest selectRequest2 = new SelectRequest(selectExpression2);
            int moodValue = 0;
            
            //Get Sting to Analyze
            for (Item item : sdb.select(selectRequest2).getItems()) {
                //System.out.println("  rg_Item");
                //System.out.println("        rg_Name: " + item.getName());
                for (Attribute attribute2 : item.getAttributes()) {
                    //System.out.println("       rg_Attribute");
                    //System.out.println("        rg_Name:  " + attribute2.getName());
                    //System.out.println("        rg_Value: " + attribute2.getValue());
                    moodValue = GetMood(attribute2.getValue());
                }
                
                //set the moodValue
                List <ReplaceableItem> moodValInput = new ArrayList<ReplaceableItem>();
                moodValInput.add(new ReplaceableItem(item.getName()).withAttributes(
                        new ReplaceableAttribute("moodVal", ""+moodValue, true)));
                sdb.batchPutAttributes(new BatchPutAttributesRequest(myDomain, moodValInput));
            }

            //print number of positve and negative values

            totalCount = negCount + posCount + neuCount;

            System.out.println("Number of Negative values: " + negCount);
            System.out.println("Number of Neutral values: " + neuCount);
            System.out.println("Number of Positive values: " + posCount);
            System.out.println("Total Number of entries considered: " + totalCount);

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon SimpleDB, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with SimpleDB, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
        
        
    }
    */

}

