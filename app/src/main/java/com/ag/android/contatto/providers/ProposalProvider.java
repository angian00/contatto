package com.ag.android.contatto.providers;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.ag.android.contatto.Profile;
import com.ag.android.contatto.util.HttpClient;
import com.ag.android.contatto.util.MiscUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProposalProvider {
    private static final String TAG = "BackendProvider";

    private static final String PROPOSAL_PATH = "proposals";
    private static final int MAX_N_PROPOSALS = 5;

    private final Context mContext;
    private final Listener mListener;

    List<MatchProposal> mAllProposals;
    List<MatchProposal> mFilledProposals;
    int mToFill;

    public interface Listener {
        void onNewProposals(List<MatchProposal> proposals);
    }


    public ProposalProvider(Context context, Listener listener) {
        mContext = context;
        mListener = listener;

        mAllProposals = new ArrayList<>();
    }

    public void downloadNextProposals(Profile profile) {
        if (mAllProposals.isEmpty()) {
            try {
                loadLocalProposals();
            } catch (IOException ioe) {
                Log.e(TAG, "Could not load local proposals", ioe);
            }
        }

        List<MatchProposal> newProposals = filterProposals(mAllProposals, profile, MAX_N_PROPOSALS);
        mFilledProposals = new ArrayList<>();
        mToFill = newProposals.size();
        if (mToFill == 0) {
            mListener.onNewProposals(mFilledProposals);
        } else {
            for (MatchProposal newProp : newProposals) {
                new ProposalFiller(newProp).execute();
            }
        }
    }



    private void loadLocalProposals() throws IOException {
        mAllProposals.clear();

        AssetManager am = mContext.getAssets();
        String[] fileList = am.list(PROPOSAL_PATH);
        for (String file : fileList) {
            if (file.endsWith(".json")) {
                MatchProposal mp = loadProposal(PROPOSAL_PATH + "/" + file);
                mAllProposals.add(mp);
            }
        }
    }

    private MatchProposal loadProposal(String path) throws IOException {
        Gson gson = new Gson();

        String jsonStr = MiscUtils.readTextFile(mContext.getAssets(), path);
        MatchProposal mp = gson.fromJson(jsonStr, MatchProposal.class);

        if (mp.getGender() == null)
            throw new IOException("Could not deserialize gender for: " + path);

        return mp;
    }


    private static List<MatchProposal> filterProposals
            (List<MatchProposal> allProposals, Profile profile, int maxNProposals) {

        List<MatchProposal> filteredProposals = new ArrayList<>();

        for (MatchProposal mp: allProposals) {
            if (!profile.getInterests().contains(mp.getGender()))
                continue;

            //add further conditions here
            //...

            filteredProposals.add(mp);
            if (filteredProposals.size() >= MAX_N_PROPOSALS)
                break;
        }

        return filteredProposals;
    }


    private class ProposalFiller extends AsyncTask<Void, Void, Bitmap> {
        private static final String TAG = "ImageDownloader";

        private MatchProposal mProposal;

        ProposalFiller(MatchProposal proposal) {
            mProposal = proposal;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            String urlStr = mProposal.getImageUrl();
            try {
                byte[] bitmapBytes = HttpClient.getUrl(urlStr).getData();
                return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            } catch (IOException ioe) {
                Log.e(TAG, "Could not download url " + urlStr, ioe);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                mProposal.setImageData(bitmap);
            }

            mFilledProposals.add(mProposal);
            if (mFilledProposals.size() == mToFill) {
                Collections.shuffle(mFilledProposals);
                mListener.onNewProposals(mFilledProposals);
            }
        }
    }

}
