<%@ include file="/WEB-INF/jsp/header.jsp"%>

<%-- Inclusion CSS --%>
<link type="text/css" rel="stylesheet" href="${baseURL}/css/bootstrap.min.css">
<link type="text/css" rel="stylesheet" href="${baseURL}/css/esup-dining-portlet.css">
<link type="text/css" rel="stylesheet" href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">

<%-- Inclusion JS --%>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=true"></script>
<script type="text/javascript">$().carousel || document.write('<script src="${baseURL}/js/bootstrap.min.js"><\/script>')</script>

<%-- container --%>
<div class="pc sm">

<p>
	<spring:message code="source.label"/>
	<a href="<spring:message code="source.url"/>" target="_blank"><spring:message code="source.name"/></a>
</p>

<%-- Fin container --%>
</div>


<script type="text/javascript">

	(function($){

	var $container = $('.pc:last');
	var appUrl = "${baseURL}";
	console.log("appUrl " + appUrl);

	// things our application is knowing
	var appHistory = {};
	var lastUrlLoaded;

	function loadUrl(url, data) {

      if (lastUrlLoaded === url && !data) return 
	  lastUrlLoaded = url;
	  $.ajax({ url: url,
		   type: data ? 'POST' : 'GET',
		   data: data,
		   xhrFields: { withCredentials: true },
            success: function (data) {
	      $container.empty().html(data);
	      wrapAnchors();
   }, error: function (err) {
     var msg = 'ERROR: ' + url + " " + err.statusText;
     $container.append(msg);
     console.log(msg);
   }     
          });
 }

	function wrapAnchors() {
	    $container.find("a").click(function () {
	    var href= $(this).attr('href');
	        var relUrl = href.replace(appUrl, '');
                if (relUrl === href) return true; // not our URL

	        loadUrl(href);

	        document.location.hash = relUrl;
	        appHistory[relUrl] = true;

	        return false;
	    });

	    $container.find("form").submit(function (evt) {
            console.log("form submit");
            //var submitEntry = evt.originalEvent.explicitOriginalTarget;
        	loadUrl($(this).attr('action'), $(this).serialize());
        	return false;
    	});
	}

	function hashPart() {
	   return document.location.hash.replace(/^#/, '');
	}
	function loadUrlFromHash() {
	  var startUrl = appUrl + (hashPart() || "/restaurants");
	  loadUrl(startUrl);
	}


	$(window).bind('popstate', function () {
	   // if we did not push that hash part ourselves, it must be another web-widget
	   if (appHistory[hashPart()]) {
		  console.log("onpopstate, may load url if different...");
	      loadUrlFromHash();
	   }
	});

	wrapAnchors();
	loadUrlFromHash();
		
		$(document).ready(function() {
			$(window).resize(onWindowResize);
			onWindowResize();
		});

		function onWindowResize() {

				var $that = $container;
				var width = $that.width();
				
				$that.removeClass("xs sm md lg");
				if(width < 768) { 
					$that.addClass("xs"); 
				}
				if(width >= 768 && width < 992) { 
					$that.addClass("sm"); 
				}
				if(width >= 992 && width < 1200) { 
					$that.addClass("md"); 
				}
				if(width >= 1200) { 
					$that.addClass("lg"); 
				}	
		}

	})(jQuery);

</script>

<%@ include file="/WEB-INF/jsp/footer.jsp"%>
