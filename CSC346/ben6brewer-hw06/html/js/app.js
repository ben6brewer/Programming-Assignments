
window.addEventListener("load", () => {
    setup();
    loadPosts();
    checkLoginTicket();
});

var apiBase = "https://csc346picturegram.test.apps.uits.arizona.edu/";
var pg = null;
var locationURL = new URL(document.location);
var newestTimestamp = null;
var oldestTimestamp = null;

function setup() {
    // Create a picturegramSDK object and set the apiBase. Store the
    // result on the `pg` global variable declared on line 8.
    pg = new picturegramSDK(apiBase);

    // *************************************
    //               PART 3
    // *************************************
    
    // Check `pg.isLoggedIn()` to see if we have a current session.
    if (pg.isLoggedIn()) {
        // If `pg.isLoggedIn()` returns true, call `updateLoginButton` and pass
        // in the username from `pg.username`.
        updateLoginButton(pg.username);
    } else {
        // If we're not logged in, then call `updateLoginButton` with no argument.
        updateLoginButton();
    }

    // Attach a click handler to the new post button
    const newPostButton = document.getElementById("newpostbutton");
    newPostButton.addEventListener("click", handleNewPost);

    // Attach a click handler to the load older posts button
    const olderPostsButton = document.getElementById("olderpostsbutton");
    olderPostsButton.addEventListener("click", handleOlderPosts);
}

/*
    checkLoginTicket()

    This function checks to see if there is a ticket key in the query string
    arguments of the current URL. If this is, we will call the `pg.authenticate()`
    method to log in the current user. 
*/
async function checkLoginTicket() {
    // Get the query string parameters out of the locationURL object created above.
    let params = locationURL.searchParams;

    // We can construct a service signature for our app by getting the origin from
    // the locationURL object. This doesn't have a trailing slash, so we need to 
    // append that to create our serivce URL. This serves as both an identifier to
    // WebAuth, as well as being the URL that the login process will return the 
    // user to. This will be something like "http://localhost:8080/" if you're working
    // locally, or "http://35.98.109.25/" if you're running on your EC2 instance.
    let service = `${locationURL.origin}/`;

    if (params.has("ticket")) {
        let ticket = params.get("ticket");
        let loginResponse = await pg.authenticate(ticket, service);
        handleLogin(loginResponse);
    } else {
        console.log("No CAS Ticket in query string.");
    }
}

/*
    loadPosts(startTime=null, endTime=null)

    The loadPosts function can be called without any arguments, or with 
    start or end times. You should only pass in the startTime or endTime
    in a given call, not both.
*/
async function loadPosts(startTime=null, endTime=null) {
    var posts_response = null;
    if (endTime != null) {
        posts_response = await pg.getPostsBefore(endTime);
    } else {
        posts_response = await pg.getPosts(startTime);
    }

    if (posts_response == undefined) {
        console.error("posts_response not set");
        return;
    }

    const posts = posts_response.messages

    // Depending on if we're loading new posts or old posts, we need
    // to add them to the top or bottom of the existing posts.
    var position = "end";
    if (startTime != null) {
        position = "beginning";
    }

    // Get a reference to the postcontainer element.
    let container = document.getElementById("postscontainer")

    // Process each post and insert it into the DOM.
    posts.forEach(post => {
        // We need to track the newest and oldest timestamps
        // These will be referenced when loading newer or older posts.
        if (newestTimestamp == null) {
            newestTimestamp = post.timestamp
        } else if (post.timestamp > newestTimestamp) {
            newestTimestamp = post.timestamp
        }
    
        if (oldestTimestamp == null) {
            oldestTimestamp = post.timestamp
        } else if (post.timestamp < oldestTimestamp) {
            oldestTimestamp = post.timestamp
        }
        
        // Call makeNewPostCard() to create a new HTML element tree.
        let newPostCard = makeNewPostCard(post);

        if (position == "end") {
            // If the new post needs to go on the end, we call appendChild
            container.appendChild(newPostCard)
        } else {
            // If the new post needs to go at the beginning, we need to use
            // insertBefore, and the position of the first child.
            container.insertBefore(newPostCard, container.children[0])
        }
    });

}

function makeNewPostCard(post) {

    let postCard = document.createElement("div")
    postCard.classList.add("card")
    postCard.classList.add("mb-4")

    // See if this post has an image
    if (post.image_thumbnail_url != undefined) {
        let postImage = document.createElement("img")
        postImage.src = post.image_thumbnail_url
        if (post.image_description != undefined) {
            postImage.alt = post.image_description
            postImage.title = post.image_description
        }
        postCard.appendChild(postImage)
    }

    let postBody = document.createElement("div")
    postBody.classList.add("card-body")
    postCard.appendChild(postBody)

    let postMessage = document.createElement("div")
    postMessage.textContent = post.message
    postBody.appendChild(postMessage)
    
    let postFooter = document.createElement("div")
    postFooter.classList.add("card-text")
    postBody.appendChild(postFooter)

    let username = document.createElement("small")
    d = new Date(Number(post.timestamp) * 1000)
    username.textContent = "@" + post.username + " (" + d.toLocaleDateString() + ")"
    postFooter.appendChild(username)

    return postCard
}


/*
    handleLogin(loginResponse)

    This function is called after pg.authenticate if there was a ticket
    in the URL of the page. If the login is "OK" we need to update the user
    interface to reflect that.
*/
function handleLogin(loginResponse) {
    // Re-write the URL to remove the ?ticket=ST- login ticket 
    // so you can bookmark or reload the page easily
    window.history.replaceState(null, '', '/');

    // Update the login button with the current username.
    updateLoginButton(pg.username);
}

/*
    handleLogout()

    Log the user out and reload the page.
*/
function handleLogout() {
    // Call the `pg.logout()` method.
    pg.logout();

    // Reload the page to trigger a refresh of the state.
    window.location.reload(true);
}

/*
    updateLoginButton(username=null)

    Update the user interface depending on if someone is logged in or not.
*/
function updateLoginButton(username=null) {
    const loginElement = document.getElementById("userlogin");
    const newPostForm = document.getElementById("newpostcontainer");
    let loginLinkElement = document.createElement("a");
    if (username == null || username == "") {
        // <a href="https://csc346picturegram.test.apps.uits.arizona.edu/login?service=http://localhost:8080/">Login</a>
        let loginURL = `${apiBase}login?service=${locationURL.origin}${locationURL.pathname}`;
        loginLinkElement.href = loginURL;
        loginLinkElement.textContent = "Login";

        // Hide the new post form
        newPostForm.hidden = true;
    } else {
        // Set up a logout link
        let loginURL = "#";
        loginLinkElement.href = loginURL;
        loginLinkElement.textContent = "Logout: " + username;
        loginLinkElement.addEventListener("click", handleLogout);

        // Show the new post form
        newPostForm.hidden = false;
    }

    // Remove all existing HTMLElement children from loginElement
    while (loginElement.firstChild) {
        loginElement.removeChild(loginElement.firstChild);
    }

    // Append our new Login Link
    loginElement.appendChild(loginLinkElement);
}

/*
    handleNewPost(event)

    This function handles the button click for posting a new post.
    It gets the text of the new post input field, and calls the createPost
    method on the API.
*/
async function handleNewPost(event) {
    // Don't submit the form through the default mechanism
    event.preventDefault();

    const postTextInputElement = document.getElementById("newpostinput");
    const postText = postTextInputElement.value;

    let newPostResponse = await pg.createPost(postText);

    if (newPostResponse.status != "OK") {
        console.error(newPostResponse.message);
        return;
    }

    // If everything worked, clear out the post input field
    postTextInputElement.value = "";

    // Load the new post
    loadPosts(newestTimestamp, null);
}

/*
    handleOlderChats(event)

    This function handles the button click for loading older chats.
*/
function handleOlderPosts(event) {
    // Don't submit the form through the default mechanism
    event.preventDefault();

    // Load the old posts
    loadPosts(null, oldestTimestamp);
}
