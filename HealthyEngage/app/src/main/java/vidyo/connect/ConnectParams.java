package vidyo.connect;

public final class ConnectParams {

    /**
     * Global connection host. **PLEASE DO NOT CHANGE**
     */
    public static final String HOST = "prod.vidyo.io";

    /**
     * Display name on the conference
     */
    public static final String DISPLAY_NAME = "Guest Name";

    /**
     * Resources are strings of text denoting meeting points in your application.
     */
    public static final String RESOURCE = "1574775582608";

    /**
     * To connect to Vidyo.io, the VidyoClient SDK needs to pass a token.
     * A token is a short-lived authentication credential that grants access to the Vidyo.io service on behalf of the developer to a specific user.
     * When an endpoint requests access to the service, your application backend should generate a token and pass it on to the client application.
     * <p>
     * Important: Never generate a token in the client application itself - this exposes your DeveloperKey which can then be used by other developers.
     * <p>
     * Generated on https://developer.vidyo.io/#/generate-token
     */
    public static final String TOKEN = "cHJvdmlzaW9uAHBhdGllbnRAZTNkMzMyLnZpZHlvLmlvADYzNzQxOTk2OTA1AAAxNDU3MTMwZDk5YzNlNWNiY2FiMGRjYWMzYTBmZmFlOTk0OTkzNGE3YmEyODVlMTU2MGYwNjYyZTEzZjU3M2NkNDcwYzQ0MGU5N2YyYmZiNWM1OGQzZTQ2NjViOGQyNTk=";

    /**
     * CONNECT AS GUEST [BETA]
     */
    //public static final String PORTAL_HOST = "<tenant>.vidyo.portal.com";
    //public static final String ROOM_KEY = "cHJvdmlzaW9uAHBhdGllbnRAZTNkMzMyLnZpZHlvLmlvADYzNzM5MTI4NzI5AABjNWUwZmViYjBhNWYwYTVkMTdhY2MzZTZiNTQyM2VjODg5ODA3Yzk5Y2Y2NWYyOGYzYTg0OGUzNjdmOGIzZDFkZWRkZTJiZWU2NjEzNDliYzhmYTgxMjJkNzFkM2NhYjI=";
    //public static final String ROOM_DISPLAY_NAME = "Guest Name";
    //public static final String ROOM_PIN = "1574671641739";

}