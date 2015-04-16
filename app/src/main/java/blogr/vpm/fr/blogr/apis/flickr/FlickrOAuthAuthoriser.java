package blogr.vpm.fr.blogr.apis.flickr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FlickrApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import blogr.vpm.fr.blogr.R;

/**
 * Created by vince on 16/04/15.
 */
public class FlickrOAuthAuthoriser {


  public OAuthService getService(Context context) {
    OAuthService service = new ServiceBuilder()
        .provider(FlickrApi.class)
        .apiKey(context.getResources().getString(R.string.flickr_api_key))
        .apiSecret(context.getResources().getString(R.string.flickr_api_secret))
        .build();
    return service;
  }

  public void openAuthorizationWebPage(final Activity activity) {
    final OAuthService service = getService(activity);

    new AsyncTask<String, Integer, Token>() {

      @Override
      protected Token doInBackground(String... strings) {
        Token requestToken = service.getRequestToken();
        return requestToken;
      }

      @Override
      protected void onPostExecute(Token requestToken) {
        String authorizationUrl = service.getAuthorizationUrl(requestToken);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authorizationUrl));
        new VerifierDialogFragment().openDialog(activity, service, requestToken);
        activity.startActivity(intent);
      }
    }.execute();
  }

  public static class VerifierDialogFragment extends DialogFragment {

    private Context context;

    private OAuthService service;

    private Token requestToken;

    public void openDialog(Activity activity, OAuthService service, Token requestToken) {
      this.context = activity;
      this.service = service;
      this.requestToken = requestToken;
      show(activity.getFragmentManager(), "verifierCollecter");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      final EditText flickrVerifier = new EditText(context);
      return new AlertDialog.Builder(context)
          .setTitle(R.string.type_flickr_code)
          .setView(flickrVerifier)
          .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              String verifier = flickrVerifier.getText().toString();
              new AsyncTask<String, Integer, Token>() {

                @Override
                protected Token doInBackground(String... verifiers) {
                  Token accessToken = service.getAccessToken(requestToken, new Verifier(verifiers[0]));
                  return accessToken;
                }

                @Override
                protected void onPostExecute(Token accessToken) {
                  // use the token
                }
              }.execute(verifier);
            }
          })
          .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              // do nothing - just close the dialog
            }
          })
          .create();
    }
  }

}
