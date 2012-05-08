<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%
    /*
     * AWS Elastic Beanstalk checks your application's health by periodically
     * sending an HTTP HEAD request to a resource in your application. By
     * default, this is the root or default resource in your application,
     * but can be configured for each environment.
     *
     * Here, we report success as long as the app server is up, but skip
     * generating the whole page since this is a HEAD request only. You
     * can employ more sophisticated health checks in your application.
     */
    if (request.getMethod().equals("HEAD")) return;
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <title>SMASMS - Stock Mood Analysis of Social Media Streams</title>
    <link rel="stylesheet" href="styles/styles.css" type="text/css" media="screen">
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<script type="text/javascript" src="js/jqBarGraph.1.1.min.js"></script>
    <script type="text/javascript">
    <!-- // shields up
    jQuery.fn.onEnter = function(callback)
    {
        this.keyup(function(e)
            {
                if(e.keyCode == 13)
                {
                    e.preventDefault();
                    if (typeof callback == 'function')
                        callback.apply(this);
                }
            }
        );
        return this;
    }

    $(document).ready(function() {
    	$('#response').hide();
        $('#tickerform').submit(function(e) {
            e.preventDefault();

             $.ajax({type: 'GET',
                     url: 'analyze',
                     data: {
                         'ticker': $('#ticker').val(),
                     },
                     success: function(json) {
             			$('#getstarted').fadeOut(function() {
             				renderJsonResponse(json);
             				
             			});
             		 },
                     dataType: 'json'});
        });
    });
    
    function renderJsonResponse(json)
    {
    	var htmlOutput = [];
    	
    	htmlOutput.push('<div class="header-main"><h1>' +
    					'Analysis for: ' + json.ticker +
    					'</h1></div>');
    	
    	htmlOutput.push('<div class="section-header">Customer Sentiment</div>');
    	htmlOutput.push('<div class="section-content" id="moods-graph" style="margin: auto;"></div>');
    	
    	htmlOutput.push('<div class="section-header">Mentions by Network</div>');
    	htmlOutput.push('<div class="section-content" id="networks-graph" style="margin: auto;"></div>');
    	
    	$('#response').html(htmlOutput.join(''));
    	
    	var arrayOfData = new Array(
    		[json.facebook, 'Facebook', '#3b5998'],
            [json.twitter, 'Twitter', '#709397'],
            [json.google, 'Google', '#008a0e']
			);
    	
    	$('#response').fadeIn();

    	$('#networks-graph').jqBarGraph({ data: arrayOfData,
    		postfix: '%',
    		height: 250,
    		});
    	
    	arrayOfData = new Array(
        	[json.negative, 'Negative', '#e34646'],
        	[json.neutral, 'Neutral', '#56739b'],
        	[json.positive, 'Positive', '#60c160']
        	);
    	
    	$('#moods-graph').jqBarGraph({ data: arrayOfData,
    		postfix: '%',
    		height: 250,
    		});
    	
    }
    
    // shields down -->
    </script>
</head>

<body>
	<div id="header" style="width: 432px; height: 73px; margin: auto;">
		<img src="images/smasms.png" alt="SMASMS - Stock Mood Analysis of Social Media Streams" style="width: 432px; height: 73px"/>
	</div>
	
	<div id="getstarted" style="background-image: url(images/getstarted.png); width: 510px; height: 145px; margin: 100px auto 10px auto;">
		<form id="tickerform">
			<input type="text" id="ticker" style="position: relative; left: 120px; top: 108px;"/>
		</form>
	</div>
	
    <div id="response">
    	If you see this, something went wrong.
    </div>
</body>

</html>