<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%
    String path = request.getContextPath();

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=request.getContextPath()%>/css/stylesheet.css" rel="stylesheet" type="text/css">
<link rel="shortcut icon" href="<%=request.getContextPath()%>/images/FaviIcon.ico" />
<title><tiles:insertAttribute name="title" ignore="true"></tiles:insertAttribute>
</title>

<meta charset="utf-8">  
<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans|Roboto" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
  <script src="<%=request.getContextPath()%>/js/nbfcCommon.js"></script>
  <link href="css/customstyle.css" type="text/css" rel="stylesheet">
   <link href="css/font-awesome.min.css" type="text/css" rel="stylesheet">
<link href="css/bootstrap.min.css" type="text/css" rel="stylesheet">
<link href="https://cdn.datatables.net/fixedcolumns/3.2.4/css/fixedColumns.dataTables.css" type="text/css" rel="stylesheet">
 



   
   <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->



<script>
function sessionTimeOut(){
	
	$(function () {
		var sessionValue="<%=session.getAttribute("uName")%>";
	   
	    var _redirectUrl = '/Aasha/nbfcLogin.html';
	    var _redirectHandle = null;

	    function resetRedirect() {
	        if (_redirectHandle) clearTimeout(_redirectHandle);
	        if(sessionValue=='null'){
	        _redirectHandle = setTimeout(function () {
		        //alert("! Session timeout !");
	            window.location.href = _redirectUrl;
	        },0);
	        }
	    }

	    $.ajaxSetup({ complete: function () { resetRedirect(); } }); // reset idle redirect when an AJAX request completes
			resetRedirect();
			
	});
}
</script>
<style type="text/css">
body{
/*background-color:#ececec";*/
width:100%;
display:block;
background-color:#ececec !important;
/*  background-image:url(images/bg4.jpg); */
 background-image:url(images/bg-logo.png);
background-size:35%;
height:100vh !important;

    overflow-y: scroll;
background-repeat:no-repeat;
background-attachment: fixed;
background-position:center;
position:relative; 

}
/* body:after{content:''; width:100%;  height: 100vh;  bacground-color:white; position:absolute; top:0;} */
</style>
</head>
<body onload="sessionTimeOut(); bgcolor:">
<div id="header-section">
<tiles:insertAttribute
					name="header"></tiles:insertAttribute>
</div>
<div  id="body-section">
<tiles:insertAttribute name="body"></tiles:insertAttribute>
</div>
<div  id="footer-section">
<tiles:insertAttribute
					name="footer"></tiles:insertAttribute>
</div>




 <script src="js/jquery-1.10.2.js" type="text/javascript"></script> 
<script src="js/bootstrap.min.js" type="text/javascript"></script>
 <!--  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script> -->
<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script>
	    $(document).ready(function(){
		    
		  	        $("#myModal").modal('show');
	    });
	</script>

<script>
$( function() {
    $( "#ratingDate" ).datepicker();
  } );
</script>

<!-- Searchable code 
<script type="text/javascript">

$("#myInput").keyup(function () {
    var value = this.value.toLowerCase().trim();
    /* var noresult = $('table tr'); */
    $("table tr").each(function (index) {
        if (!index) return;
        $(this).find("td").each(function () {
        	
            var id = $(this).text().toLowerCase().trim();
            var not_found = (id.indexOf(value) == -1);			                        
            $(this).closest('tr').toggle(!not_found);
            return not_found;
            /* if(not_found == '' ){
            	
            	alert('sdsd');
            } */
            
        });
        
    });

});

</script>-->

<script src="css/bower_components/jquery/jquery.min.js"></script>
<script src="https://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/fixedcolumns/3.2.4/js/dataTables.fixedColumns.min.js" ></script> 
<script>
$('#myTable,myTable2').DataTable( {
    "pagingType": "full_numbers",
    responsive: true

});
$('#myTable1').DataTable( {
	  //  "pagingType": "full_numbers",
	   // responsive: true,
	//  scrollY:        "500px",
      scrollX:        true,
      scrollCollapse: true,
      paging:         true,
      fixedColumns:   {
          leftColumns: 5,
          
      }		
 
	});
// $('#myTable').dataTable();
</script>




<!-- pagination code
<script type="text/javascript">
var $table = document.getElementById("myTable"),
//number of rows per page
$n = 10,
//number of rows of the table
$rowCount = $table.rows.length,
//get the first cell's tag name (in the first row)
$firstRow = $table.rows[0].firstElementChild.tagName,
//boolean var to check if table has a head row
$hasHead = ($firstRow === "TH"),
//an array to hold each row
$tr = [],
//loop counters, to start count from rows[1] (2nd row) if the first row has a head tag
$i,$ii,$j = ($hasHead)?1:0,
//holds the first row if it has a (<TH>) & nothing if (<TD>)
$th = ($hasHead?$table.rows[(0)].outerHTML:"");
//count the number of pages
var $pageCount = Math.ceil($rowCount / $n);
//if we had one page only, then we have nothing to do ..
if ($pageCount > 1) {
 // assign each row outHTML (tag name & innerHTML) to the array
 for ($i = $j,$ii = 0; $i < $rowCount; $i++, $ii++)
     $tr[$ii] = $table.rows[$i].outerHTML;
 // create a div block to hold the buttons
 $table.insertAdjacentHTML("afterend","<div id='pagination-button'></div");
 // the first sort, default page is the first one
 sort(1);
}

//($p) is the selected page number. it will be generated when a user clicks a button
function sort($p) {
 /* create ($rows) a variable to hold the group of rows
 ** to be displayed on the selected page,
 ** ($s) the start point .. the first row in each page, Do The Math
 */
 var $rows = $th,$s = (($n * $p)-$n);
 for ($i = $s; $i < ($s+$n) && $i < $tr.length; $i++)
     $rows += $tr[$i];
 
 // now the table has a processed group of rows ..
 $table.innerHTML = $rows;
 // create the pagination buttons
 document.getElementById("pagination-button").innerHTML = pageButtons($pageCount,$p);
 // CSS Stuff
 document.getElementById("id"+$p).setAttribute("class","active");
}


//($pCount) : number of pages,($cur) : current page, the selected one ..
function pageButtons($pCount,$cur) {
 /* this variables will disable the "Prev" button on 1st page
    and "next" button on the last one */
 var $prevDis = ($cur == 1)?"disabled":"",
     $nextDis = ($cur == $pCount)?"disabled":"",
     /* this ($buttons) will hold every single button needed
     ** it will creates each button and sets the onclick attribute
     ** to the "sort" function with a special ($p) number..
     */
     $buttons = "<input type='button' value=' Previous' onclick='sort("+($cur - 1)+")' "+$prevDis+">";
 for ($i=1; $i<=$pCount;$i++)
     $buttons += "<input type='button' id='id"+$i+"'value='"+$i+"' onclick='sort("+$i+")'>";
 $buttons += "<input type='button' value='Next ' onclick='sort("+($cur + 1)+")' "+$nextDis+">";
 return $buttons;
}

</script>

<script>
 /* $(document).ready(function(){
	  $("#myInput").on("keyup", function() {
	    var value = $(this).val().toLowerCase();
	    $("#myTable tr td").filter(function() {
	      $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
	    });
	  });
	});  */

/*  function myFunction() {
  var input, filter, table, tr, td, i, txtValue;
  input = document.getElementById("myInput");
  filter = input.value.toUpperCase();
  table = document.getElementById("myTable");
  tr = table.getElementsByTagName("tr");
  for (i = 0; i < tr.length; i++) {
    td = tr[i].getElementsByTagName("td")[0];
    if (td) {
      txtValue = td.textContent || td.innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        tr[i].style.display = "";
      } else {
        tr[i].style.display = "none";
      }
    }       
  } 
 */  -->
</script>
</body>
</html>