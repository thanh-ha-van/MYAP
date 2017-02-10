/**
 * Created by HaVan on 11/25/2016.
 */
public class nightmode {
    private static nightmode ourInstance = new nightmode();

    public static nightmode getInstance() {
        return ourInstance;
    }

    private nightmode() {
    }
}
