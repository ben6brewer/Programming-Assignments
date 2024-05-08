/*

picturegramSDK

To use this SDK class in app.js, create an instance of it:
    
    var pg = new picturegramSDK("https://csc346picturegram.test.apps.uits.arizona.edu/")

From there, you should only need to call the convenience methods:

    pg.authenticate(ticket, service)
    pg.getPosts()
    pg.getPosts(startTime)
    pg.getPostsBefore(endTime)
    pg.createPost(postText)

*/
class picturegramSDK {

    apiBase;
    authJWT;
    username;

    constructor(apiBase) {
        // If apiBase was NOT passed in with a trailing slash, add it.
        if (apiBase[apiBase.length - 1] != "/") {
            this.apiBase = `${apiBase}/`;
        } else {
            this.apiBase = apiBase;
        }
    }

    /*
        Make an API call to the PictureGram API.

        apiCall() should only be called from within this class, and is marked as 
        private with the leading `#` symbol.

        Parameters:
            method: The HTTP method for this API call, [GET, POST, PUT]
            action: The path portion of the API call, ie `posts` or `authenticate`
                    See the API documentation for a complete list of actions
            requiresAuth: A boolean value to indicate if this API call needs to
                            send the Authorization header along with the call.
                            It defaults to `false` if not passed in.
            data:   A simple javascript data object with key/value pairs to
                    send as the body of a POST or PUT request. This object will
                    be encoded with JSON.stringify before being sent.
    */
    async #apiCall(method, action, requiresAuth=false, data=null) {
        let options = {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            }
        }

        if (requiresAuth) {
            if (this.authJWT == null) {
                console.error("authJWT is null. Are you logged in?");
                return;
            }
            options.headers["Authorization"] = `Bearer ${this.authJWT}`;
        }

        if (data != null) {
            if (method != "POST" && method != "PUT") {
                console.error(`Method not allowed when sending data: ${method}`);
                return;
            }

            options.body = JSON.stringify(data);
        }

        // If the passed in action starts with a leading slash (/), remove it.
        if (action[0] == "/") {
            action = action.slice(1);
        }

        // Await the fetch() response and handle errors.
        let response = await fetch(this.apiBase + action, options)
                        .then((response) => {
                            if (response.status != 200) {
                                resp_object = response.json().then((errorJson) => {
                                    throw new Error(`${response.status}: ${errorJson.message}`);
                                })
                                .catch((error) => { console.error(error); })
                                return `Error status: ${response.status}`;
                            } else {
                                return response.json();
                            }
                        })
                        .catch((error) => { console.error(error); });
        
        // Return the response.
        return response;
    }


    /*
        getPosts()
        getPosts(startTime)

        This method is called to either fetch the most recent 5 posts, or to pull all posts
        since a given timestamp. If startTime is not provided in the API call, then just
        the basic `posts` action will be called on the API. If the startTime is set, then
        the `posts/after/{timestamp}` action will be called.

        startTime: A string version of the timestamp of a post, ie 1666235802.930319
    */
    getPosts(startTime) {
        let action = "";
        if (startTime == null) {
            action = "posts";
        } else {
            action = `posts/after/${startTime}`;
        }
        return this.#apiCall("GET", action);
    }

    /*
        getPostsBefore(endTime)

        This method is will call the `posts/before/${endTime}` action, where timestamp
        is the endTime of posts to fetch, and posts before that time will be fetched.

        endTime: A string version of the timestamp of a post, ie 1666235802.930319
    */
        getPostsBefore(endTime) {
        // Similar to the aboive method, this method should return the result of 
        // a call to this.#apiCall with an HTTP GET method, and the `posts/before/${endTime}`
        // action on the API.
        let action = `posts/before/${endTime}`;
        return this.#apiCall("GET", action);
    }

    /*
        authenticate(ticket, service)
        
        This method will call the `authenticate/${ticket}?service=${service}` action on 
        the API. Ticket will be the WebAuth CAS ticket returned after a successful login
        to WebAuth. Service is the URL of your application, so the verification of the 
        ticket matches the initial request. See app.js for hints on how to construct this.
    */
    async authenticate(ticket, service) {
        let action = `authenticate/${ticket}?service=${service}`;
        let authResponse = await this.#apiCall("GET", action);
        
        if (authResponse.status == "OK") {
            this.authJWT = authResponse.jwt;
            this.username = authResponse.username;
            sessionStorage.setItem("username", this.username);
            sessionStorage.setItem("authJWT", this.authJWT);
        } else {
            console.error(authResponse.message);
        }

        return authResponse.status;
    }

    logout() {
        // Set `this.username` to null
        this.username = null;

        // Set `this.authJWT` to null
        this.authJWT = null;

        // Remove `username` from sessionStorage.
        window.sessionStorage.removeItem("username");

        // Remove `authJWT` from sessionStorage.
        window.sessionStorage.removeItem("authJWT");
    }
    
    /*
        isLoggedIn()

        Check to see if we have an active logged in session.
    */
    isLoggedIn() {
        // If `this.authJWT` is null, then see if we can load a session.
        if (this.authJWT == null) {
            // Try and load `username` from window.sessionStorage and store it in a temporary
            // variable `const username`.
            const username = window.sessionStorage.getItem("username");
            
            // If we get a value back, set `this.username` to that.
            if (username != null) {
                this.username = username;
            }
    
            // Try and load `authJWT` from window.sessionStorage.
            const authJWT = window.sessionStorage.getItem("authJWT");
    
            // If we get a value back, set `this.authJWT` to that.
            if (authJWT != null) {
                this.authJWT = authJWT;
            }
        }
    
        // Test `this.authJWT` again to see if it is null, or if we managed to 
        // load it from sessionStorage. If it is null, return false, else return true.
        return this.authJWT != null;
    }
    /*
        createPost(postText)

        This method will call the `POST posts` action on the API. There's just a single
        postText containing the text of the new post to store. Times and user will be
        recorded based on the time its posted, and the logged in user. This call must
        be authenticated.
    */
    createPost(postText) {
        // The HTTP method should be "POST".
        // The API action should be "posts".
        // We need to pass `true` in for the `requiresAuth` argument.
        // We need to pass in an object for the `data` argument containing a "message" key 
        //   and the postText that was passed in.
        return this.#apiCall("POST", "posts", true, { message: postText });
    }
}
