document.addEventListener('DOMContentLoaded',()=>{
    const shareableLinkInput = document.getElementById('shareable-link');
    const copyLinkBtn = document.getElementById('copy-link-btn');
    const logoutBtn = document.querySelector('.logout-btn');
    const rid = localStorage.getItem("rid");

    var hostname = window.location.hostname;
    var port = window.location.port;
    var context = '/anonymously';
    var servlet = '/GetMessageServlet';

    var requestURLf = 'http://' + hostname;
    if (port && port !== '80') {
      requestURLf += ':' + port;
    }
    
    shareableLinkInput.value = requestURLf + context + '/message.html?rid='+rid;
    
	const requestMessages = () => {
	  // Replace 'YOUR_SERVLET_URL' with the actual URL of your servlet
	  const requestURL = requestURLf + context + servlet + '?rid='+rid; // Example parameter 'rid=123'
	  fetch(requestURL)
	    .then(response => response.text())
	    .then(jsonData => {
	      console.log(jsonData);
	      
	      var messagesDiv = document.getElementById("messages");
	      
	      // Parse the JSON data
	      var data = JSON.parse(jsonData);
	      
	      // Iterate over the keys in the JSON data
	      for (var key in data) {
	        var messageElement = document.createElement("p");
	        messageElement.textContent = data[key];
	  
	        // Append the message element to the 'messages' div
	        messagesDiv.appendChild(messageElement);
	      }
	    })
	    .catch(error => {
	      console.error('Error:', error);
	      // Handle any error or display an error message
	    });
	};


  // Copy the shareable link to the clipboard
    const copyLinkToClipboard = () => {

        const textToCopy = shareableLinkInput.value;

        navigator.clipboard.writeText(textToCopy)
        .then(() => {
        // Optionally, display a success message or provide visual feedback to the user
        console.log('Link copied to clipboard');
        })
        .catch(error => {
        console.error('Error:', error);
        // Handle any error or display an error message
        });
    };

    const handleLogout = () => {
        // Redirect to the index.html page
        // Remove a key-value pair from localStorage

        localStorage.removeItem('rid');
        window.location.href = requestURLf + context + '/index.html';
    };
   
	requestMessages();
    copyLinkBtn.addEventListener('click', copyLinkToClipboard);
    logoutBtn.addEventListener('click', handleLogout);
});