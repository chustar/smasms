package in.smasms;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class AnalysisController
 */
public class AnalysisController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static int ranPos;
	private static int ranNeg;
	private static int ranNeu;
	private static int ranTot;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AnalysisController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @SuppressWarnings({"unchecked"})
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
    	String tickerSymbol = request.getParameter("ticker");

    	// do something to get data we need from the Amazon SimpleDB
    	
    	// encode data as JSON and write the response
		JSONObject obj = new JSONObject();

		obj.put("ticker", tickerSymbol);
		
		
		/*
		 * below is dynamic db setting
		 * getting values from tweets database
		 */
		/*
		AnalysisOfDB myDBAnalyzer = new AnalysisOfDB();
		int pos = myDBAnalyzer.getPositiveCount();
		int neu = myDBAnalyzer.getNeutralCount();
		int neg = myDBAnalyzer.getNegativeCount();
		int tot = myDBAnalyzer.getTotalCount();
		pos = (pos  * 100 / tot);
		neu = (neu  * 100 / tot);
		neg = (neg  * 100 / tot);
		obj.put("negative", neg);
		obj.put("neutral", neu);
		obj.put("positive", pos);		
		*/
		
		/*
		 * below is random dynamic setting
		 */
		GetMoodRandom();
		obj.put("negative", ranNeg);
		obj.put("neutral", ranNeu);
		obj.put("positive", ranPos);
		
		/*
		 * below is static setting
		 */
		/*
		obj.put("negative", 26);
		obj.put("neutral", 50);
		obj.put("positive", 34);
		*/
		
		/*
		 * below is static source setting
		 */
		/*
		obj.put("facebook", 17);
		obj.put("twitter", 43);
		obj.put("google", 40);
		*/
		
		/*
		 * below is random dynamic source setting
		 */
		GetMoodRandom();
		obj.put("facebook", ranPos);
		obj.put("twitter", ranNeg);
		obj.put("google", ranNeu);

		response.getWriter().println(obj);
	}
    
    /**
     * set ranPos, ranNeg, ranNeu, ranTot for prototyping 
     */
    private static void GetMoodRandom(){
		Random rn = new Random();
		ranPos = (Math.abs(rn.nextInt())) % 1000;
		ranNeu = (Math.abs(rn.nextInt())) % 1000;
		ranNeg = (Math.abs(rn.nextInt())) % 1000;
		ranTot = ranPos + ranNeu + ranNeg;
		ranPos = (ranPos * 100 / ranTot);
		ranNeu = (ranNeu * 100 / ranTot);
		ranNeg = 100 - ranPos - ranNeu;
    }
}
