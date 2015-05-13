package blogr.vpm.fr.blogr.apis.flickr;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.scribe.model.Token;

/**
 * Created by vince on 13/05/15.
 */
public class FlickrOAuthTokenStore {

  private static final String TOKEN_PREF_KEY = "blogr.secure.flickr.oauth.token";
  private static final String SECRET_PREF_KEY = "blogr.secure.flickr.oauth.token.secret";

  /**
   * Determines whether the store keeps a token
   *
   * @param context the current Android context
   * @return whether the store keeps a token
   */
  public boolean hasStoredToken(Context context) {
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    String token = prefs.getString(TOKEN_PREF_KEY, "");
    return ((token != null) && (!token.isEmpty()));
  }

  /**
   * If the store keeps a token, it returns it.
   *
   * @param context the current Android context
   * @return a Token if one is stored
   * @pre {@link #hasStoredToken(Context)} must be called before to check whether a token is stored
   */
  public Token getStoredToken(Context context) {
    Token oauthToken = null;
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    String token = prefs.getString(TOKEN_PREF_KEY, "");
    if ((token != null) && (!token.isEmpty())) {
      String secret = prefs.getString(SECRET_PREF_KEY, "");
      oauthToken = new Token(token, secret);
    }
    return oauthToken;
  }

  /**
   * Stores a token for later retrieval
   *
   * @param context the current Android context
   * @param token   the token to store
   */
  public void storeToken(Context context, Token token) {
    SharedPreferences.Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    prefsEditor.putString(TOKEN_PREF_KEY, token.getToken());
    prefsEditor.putString(SECRET_PREF_KEY, token.getSecret());
    prefsEditor.apply();
  }
}
