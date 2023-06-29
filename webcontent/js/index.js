/**
 * 
 */
document.addEventListener('DOMContentLoaded', () => {
	document.querySelectorAll('button').forEach(button => {
		button.onclick = () => {
			
		    var hostname = window.location.hostname;
		    var port = window.location.port;
		    var context = '/anonymously';
		
		    var requestURLf = 'http://' + hostname;
		    if (port && port !== '80') {
		      requestURLf += ':' + port;
		    }
			// check for rid and do redirect
			if(localStorage.getItem('rid')){
				window.location.href = requestURLf + context + '/dashboard.html';
				return;
			}
	        const buttonType = button.value;
	        
	        // Determine the destination page based on the button's data value
	        let destinationPage;
	        if (buttonType === 'login') {
	          destinationPage = '/login.html';
	        } else if (buttonType === 'signup') {
	          destinationPage = '/signup.html';
	        } 
	        // Redirect to the destination page

			redirectURL = requestURLf + context + destinationPage;
			
			// Redirect to the destination page
			window.location.href = redirectURL;
		};
	});
});
  