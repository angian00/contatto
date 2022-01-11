package com.ag.android.contatto.ui.matching;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ag.android.contatto.R;
import com.ag.android.contatto.providers.ProfileProvider;
import com.ag.android.contatto.providers.ProposalProvider;
import com.ag.android.contatto.providers.MatchProposal;
import com.ag.android.contatto.util.ThumbnailDownloader;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.List;

public class MatchingFragment extends Fragment
        implements ProposalProvider.Listener, CardStackListener {

    private static final String TAG = "MatchingFragment";
    private static final long DOWNLOAD_PROPOSALS_DELAY = 1000;

    public static Fragment newInstance() {
        return new MatchingFragment();
    }

    private final List<MatchProposal> mProposals;
    ProposalAdapter mAdapter;

    private ProposalProvider mBackendProvider;
    private ThumbnailDownloader<ProposalHolder> mThumbnailDownloader;

    private View mProgress;
    private CardStackView mCardStackView;
    private CardStackLayoutManager mCardLayoutManager;
    private TextView mEndOfStack;


    public MatchingFragment() {
        super();
        mProposals = new ArrayList<>();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ProposalAdapter(mProposals);
        mBackendProvider = new ProposalProvider(getActivity().getApplicationContext(), this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.fragment_matching, container, false);

        mProgress = v.findViewById(R.id.progress_container);

        mEndOfStack = v.findViewById(R.id.end_of_stack);

        mCardStackView = v.findViewById(R.id.card_stack_view);
        mCardStackView.setVisibility(View.GONE);

        mCardLayoutManager = new CardStackLayoutManager(getActivity(), this);
        mCardStackView.setLayoutManager(mCardLayoutManager);
        mCardStackView.setAdapter(mAdapter);

        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloaderListener(
                (target, thumbnail) -> {
                    Drawable drawable = new BitmapDrawable(thumbnail);
                    target.bindDrawable(drawable);
                }
        );
        mThumbnailDownloader.start();
        mThumbnailDownloader.getLooper();

        Runnable delayedTask = new Runnable() {
            public void run() {
                mBackendProvider.downloadNextProposals(new ProfileProvider(getActivity()).loadProfile());
            }
        };

        new Handler().postDelayed(delayedTask, DOWNLOAD_PROPOSALS_DELAY);


        return v;
    }

    @Override
    public void onNewProposals(List<MatchProposal> proposals) {
        Log.d(TAG, "onNewProposals");
        mProposals.clear();
        mProposals.addAll(proposals);
        mAdapter.notifyDataSetChanged();

        mCardStackView.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.GONE);
    }


    @Override
    public void onCardSwiped(Direction direction) {
        if (mCardLayoutManager.getTopPosition() == mAdapter.getItemCount()) {
            // last position reached, do something
            mEndOfStack.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {}

    @Override
    public void onCardRewound() {}

    @Override
    public void onCardCanceled() {}

    @Override
    public void onCardAppeared(View view, int position) {}

    @Override
    public void onCardDisappeared(View view, int position) {}



    private static class ProposalHolder extends CardStackView.ViewHolder {

        private final TextView mNameView;
        private final TextView mAgeView;
        private final TextView mDescriptionView;
        private final ImageView mPhotoView;

        public ProposalHolder(@NonNull View itemView) {
            super(itemView);

            mNameView = itemView.findViewById(R.id.name);
            mAgeView = itemView.findViewById(R.id.age);
            mDescriptionView = itemView.findViewById(R.id.description);
            mPhotoView = itemView.findViewById(R.id.photo);
        }

        public void bindProposal(MatchProposal proposal) {
            mNameView.setText(proposal.getName());
            mAgeView.setText(", " + proposal.getAge());

            mDescriptionView.setText(proposal.getDescription());
        }

        public void bindDrawable(Drawable drawable) {
            mPhotoView.setImageDrawable(drawable);
        }
    }


    private class ProposalAdapter extends CardStackView.Adapter<ProposalHolder> {
        private final List<MatchProposal> mProposals;

        public ProposalAdapter(List<MatchProposal> proposals) {
            mProposals = proposals;
        }

        @NonNull
        @Override
        public ProposalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.fragment_match_card, parent, false);

            return new ProposalHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProposalHolder proposalHolder, int position) {
            MatchProposal proposal = mProposals.get(position);
            proposalHolder.bindProposal(proposal);
            mThumbnailDownloader.queueThumbnail(proposalHolder, proposal.getImageUrl());
        }

        @Override
        public int getItemCount() {
            return mProposals.size();
        }
    }
}
