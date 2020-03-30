package com.udacity.gradle.builditbigger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Pair;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.angadcheema.androidjokes.AndroidJoke;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;
import java.io.IOException;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
  Context context = getActivity();

  private ProgressBar spinner;
  private InterstitialAd mInterstitialAd;
  public MainActivityFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View root = inflater.inflate(R.layout.fragment_main, container, false);

    mInterstitialAd = new InterstitialAd(getActivity());
    mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
    mInterstitialAd.loadAd(new AdRequest.Builder().build());

    spinner = root.findViewById(R.id.progressBar);
    spinner.setVisibility(View.GONE);
    AdView mAdView = (AdView) root.findViewById(R.id.adView);
    AdRequest adRequest = new AdRequest.Builder()
        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        .build();
    mAdView.loadAd(adRequest);

    mInterstitialAd.setAdListener(new AdListener() {
      @Override
      public void onAdClosed() {
        super.onAdClosed();
        spinner.setVisibility(View.VISIBLE);
        asyncTaskLoading();
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
      }
      @Override
      public void onAdLoaded() {
        super.onAdLoaded();
      }
    });


    Button button = (Button) root.findViewById(R.id.button);

    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isOnline()) {
          mInterstitialAd.show();

        } else {
          Toast.makeText(getActivity(), "Please Connect to Internet", Toast.LENGTH_LONG)
              .show();
          spinner.setVisibility(View.GONE);
        }
      }
    });

    return root;
  }

  public void loadData(String jokeValue) {
      Context context = getActivity();
      Intent intent = new Intent(context, AndroidJoke.class);
      intent.putExtra("JOKE", jokeValue);
      context.startActivity(intent);
      spinner.setVisibility(View.GONE);

  }

  public void asyncTaskLoading() {

    EndpointsAsyncTask runner = new EndpointsAsyncTask();
    runner.execute(new Pair<Context, String>(getContext(), "Manfred"));
  }

  public class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private MyApi myApiService = null;

    @Override
    protected String doInBackground(Pair<Context, String>... params) {

      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (myApiService == null) {  // Only do this once
        MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
            new AndroidJsonFactory(), null)
            .setRootUrl("https://builditbigger-272507.appspot.com/_ah/api/");
        myApiService = builder.build();
      }

      context = params[0].first;
      String name = params[0].second;

      try {
        return myApiService.sayHi(name).execute().getData();
      } catch (IOException e) {
        return e.getMessage();
      }
    }

    @Override
    protected void onPostExecute(String result) {
      loadData(result);
      super.onPostExecute(result);

    }
  }

  public boolean isOnline() {
    Runtime runtime = Runtime.getRuntime();
    try {
      Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
      int exitValue = ipProcess.waitFor();
      return (exitValue == 0);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }

    return false;

  }
}
