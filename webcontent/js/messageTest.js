/**
 * 
 */
document.addEventListener('DOMContentLoaded',()=>{
	document.querySelector('form').onsubmit = (event) =>{
		event.preventDefault();
		
	  const urlParams = new URLSearchParams(window.location.search);
	  const rid = urlParams.get('rid');
	  
	  if (rid === null) {
		document.getElementById('error').innerHTML = 'recipiant not found';
	    console.error('Missing "rid" parameter in the URL.');
	    // Handle the case when "rid" is missing
	    return;
	  }

	  const message = document.getElementById('messageInput').value;
	  
	  const messageData = {
	    rid: rid,
	    message: message
	  };
	  
	    var hostname = window.location.hostname;
	    var port = window.location.port;
	    var context = '/anonymously';
	    var servlet = '/MessageTestServlet';
	
	    var requestURL = 'http://' + hostname;
	    if (port && port !== '80') {
	      requestURL += ':' + port;
	    }
	    requestURL += context + servlet;
	    
	  
	    fetch(requestURL, {
	      method: 'POST',
	      headers: {
	        'Content-Type': 'application/json'
	      },
	      body: JSON.stringify(messageData)
	    })
	    .then(response => response.json())
	    .then(data => {
			const status = data.status;
			const log = data.log;
			
			document.getElementById('messageStatus').innerHTML = `${status} : ${log}`;                // Handle the response data
	      // Optionally, redirect the user to a new page or display a success message
	    })
	    .catch(error => {
	      console.error('Error:', error);
	      // Handle any error or display an error message
	    });
	    
	};
});