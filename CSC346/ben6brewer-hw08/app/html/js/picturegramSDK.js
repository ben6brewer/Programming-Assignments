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

    async authenticate(ticket, service) {
        /*
            authenticate(ticket, service)

            This method will call the `authenticate/${ticket}?service=${service}` action on 
            the API. Ticket will be the WebAuth CAS ticket returned after a successful login
            to WebAuth. Service is the URL of your application, so the verification of the 
            ticket matches the initial request. See app.js for hints on how to construct this.
        */
        const authResponse = await this.#apiCall("GET", `authenticate/${ticket}?service=${service}`);

        // If the `status` from the response is "OK", perform login functions.
        if (authResponse.status == "OK") {

            // From the response, store the `jwt` token on the `authJWT` attribute on this object.
            this.authJWT = authResponse.jwt;

            // From the response, store the `username` on the `username` attribute of this object.
            this.username = authResponse.username;

            // Store `this.username` in the browser's local storage
            window.sessionStorage.setItem("username", this.username);
            // Store `this.authJWT` in the browser's local storage
            window.sessionStorage.setItem("authJWT", this.authJWT)
            
        } else {
            // If the `status` from the response is not "OK", log the `message` returned 
            // as a console error.
            console.error(authResponse.message);
        }

        // Return the `status` from the response object
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
        // If `this.authJWT` is null, then see if we can load a session from
        // window.sessionStorage.
        if (this.authJWT == null) {
            // Try and load `username` from sessionStorage. If we get a value back, 
            // set `this.username`.
            const username = window.sessionStorage.getItem("username");
            if (username != null) {
                this.username = username;
            }

            // Try and load `authJWT` from sessionStorage. If we get a value back, 
            // set `this.authJWT`.
            const authJWT = window.sessionStorage.getItem("authJWT");
            if (authJWT != null) {
                this.authJWT = authJWT;
            }
        }

        // Test `this.authJWT` again to see if it is null, or if we managed to 
        // load it from sessionStorage. If it is null, return false, else return true.
        if (this.authJWT == null) {
            return false;
        } else {
            return true;
        }
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

        This method is will call the `posts/before/${timestamp}` action, where timestamp
        is the endTime of posts to fetch, and posts before that time will be fetched.

        endTime: A string version of the timestamp of a post, ie 1666235802.930319
    */
        getPostsBefore(endTime) {
        let action = "";
        if (endTime == null) {
            action = "posts";
        } else {
            action = `posts/before/${endTime}`;
        }
        return this.#apiCall("GET", action);
    }

    /*
        createPost(postText)

        This method will call the `POST posts` action on the API. There's just a single
        postText containing the text of the new post to store. Times and user will be
        recorded based on the time its posted, and the logged in user. This call must
        be authenticated.
    */

    /************************
     * PART 2
     * Add a second input argument for the 'imageData' passed from app.js
     ************************/
    createPost = function(postText, imageData) {
        let payload = {
            "message": postText
        }

        /************************
         * PART 2
         * Test to see if imageData is undefined. If it is not
         * undefined, add new object keys and values to the payload,
         * matching the API specification including the optional image
         * keys.
         ************************/

        if (imageData) {
            payload.image_full_url = imageData.full_url;
            payload.imageUrl = imageData.url;
            payload.image_thumbnail_url = imageData.thumbnail_url;
        }


        /************************
         * PART 2
         * While testing, it might be helpful to uncomment the following line
         * to temporarily disable new chats from being posted. Don't forget to
         * comment it out again when you're really ready to post your new chat 
         * message with images!
         ************************/
        // return Promise.resolve({"status": "error", "message": "short circuit"})

        return this.#apiCall("POST", "posts", true, payload);
    }
}
