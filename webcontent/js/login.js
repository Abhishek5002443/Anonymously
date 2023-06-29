/**
 * 
 */
 document.addEventListener('DOMContentLoaded', () => {
  document.querySelector('form').onsubmit = (event) => {
    event.preventDefault(); // Prevent the default form submission
    
	  const user = document.getElementById("username").value;
	  const pass = document.getElementById("password").value;

    var hostname = window.location.hostname;
    var port = window.location.port;
    var context = '/anonymously';
    var servlet = '/ValidationServlet';

    var requestURLf = 'http://' + hostname;
    if (port && port !== '80') {
      requestURLf += ':' + port;
    }
    var requestURL = requestURLf + context + servlet;

    var userData = {username : user, password : pass, validationType : "login"}; // Your user data object here

    fetch(requestURL, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(userData)
    })
    .then(response => response.json())         // {userId, status, log}
    .then(data => {
		console.log(data.status);
      	console.log(data.log); // Handle the response data
        // Optionally, redirect the user to a new page or display a success message
        const rid = data.userId;
        
        localStorage.setItem("rid",rid);
        window.location.href = requestURLf+ context +"/dashboard.html";
    })
    .catch(error => {
      console.error('Error:', error);
      // Handle any error or display an error message
    });
  };
});
