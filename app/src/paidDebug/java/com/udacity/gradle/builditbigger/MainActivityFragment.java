package com.udacity.gradle.builditbigger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.angadcheema.androidjokes.AndroidJoke;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;
import java.io.IOException;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

  Context context = getActivity();
  public String jokeString;
  private ProgressBar spinner;

  public MainActivityFragment() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_main, container, false);

    spinner = (ProgressBar) root.findViewById(R.id.progressBar);
    spinner.setVisibility(View.GONE);

    Button button = (Button) root.findViewById(R.id.button);
    button.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (isOnline()) {
          asyncTaskLoading();
          spinner.setVisibility(View.VISIBLE);
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
    Log.d("JokeRcvd", "JokeRcvd: " + jokeString);
    Intent intent = new Intent(context, AndroidJoke.class);
    intent.putExtra("JOKE", jokeValue);
    context.startActivity(intent);
    spinner.setVisibility(View.GONE);

  }

  public void asyncTaskLoading() {

    EndpointsAsyncTask runner = new EndpointsAsyncTask();
    runner.execute(new Pair<Context, String>(getContext(), "Manfred"));


  }

  private class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {

    private MyApi myApiService = null;

    @Override
    protected String doInBackground(Pair<Context, String>... params) {
      try {
        Thread.sleep(3000);
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
      jokeString = String.valueOf(result);
      Log.d("RESULT", "onPostExecute: " + jokeString);

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
