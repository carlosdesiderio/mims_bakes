package uk.me.desiderio.mimsbakes;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import uk.me.desiderio.mimsbakes.data.model.Step;

/**
 * Fragment to show video and description of the current {@link Step}
 * <p>
 * The class will be attached to either {@link StepVideoActivity} and
 * {@link RecipeDetailsActivity} depending on the device and rotation.
 */
public class StepVideoFragment extends Fragment implements Player.EventListener {

    private static final String TAG = StepVideoFragment.class.getSimpleName();

    private Step step;
    private TextView descriptionTextView;
    private ProgressBar progressBar;
    private ImageView replayIcon;
    private ImageView pauseIcon;
    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView playerView;
    private ExtractorMediaSource.Factory mediaSourceFactory;

    public StepVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_video, container, false);
        descriptionTextView = rootView.findViewById(R.id.step_description_text_view);
        progressBar = rootView.findViewById(R.id.step_video_progress_bar);
        replayIcon = rootView.findViewById(R.id.step_video_replay_image_view);
        pauseIcon = rootView.findViewById(R.id.step_video_pause_image_view);

        playerView = rootView.findViewById(R.id.player_view);

        playerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.recipe_image_loading));

        playerView.setControllerHideOnTouch(false);
        playerView.setControllerShowTimeoutMs(0);

        initializePlayer();


        playerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    playWhenReady();
                }
                return true;
            }

            ;
        });

        return rootView;
    }

    /**
     * updates view with data provided as its parameter
     */
    public void swapData(Step step) {
        this.step = step;
        updateViewOnDataChange(step);
    }

    private void updateViewOnDataChange(Step step) {
        step.getVideoURLString();
        descriptionTextView.setText(step.getDescription());
        // at this stage of implementation, error handling is relied upon the player itself
        Uri mediaUri = Uri.parse(step.getVideoURLString());
        preparePlayer(mediaUri);
    }

    private void initializePlayer() {
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            playerView.setPlayer(exoPlayer);
            exoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), "MimsBakes");
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), userAgent);
            mediaSourceFactory = new ExtractorMediaSource.Factory
                    (dataSourceFactory);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void preparePlayer(Uri mediaUri) {
        if (exoPlayer != null) {
            exoPlayer.stop();
        }
        MediaSource mediaSource = mediaSourceFactory.createMediaSource(mediaUri);
        exoPlayer.prepare(mediaSource);
    }

    private void playWhenReady() {
        int playerState = exoPlayer.getPlaybackState();
        switch (playerState) {
            case Player.STATE_ENDED:
                exoPlayer.seekTo(0);
                exoPlayer.setPlayWhenReady(true);
                break;
            case Player.STATE_READY:
                if (exoPlayer != null) {
                    exoPlayer.setPlayWhenReady(!exoPlayer.getPlayWhenReady());
                }
                break;
        }
    }

    private void releasePlayer() {
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    // toggles pause icon visibility
    private void showPauseButton(boolean isPaused) {
        if (isPaused) {
            pauseIcon.setVisibility(View.VISIBLE);
        } else {
            pauseIcon.setVisibility(View.GONE);
        }
    }

    // toggles replay icon visibility
    private void showReplayIcon(int playbackState) {
        if (playbackState == Player.STATE_ENDED) {
            replayIcon.setVisibility(View.VISIBLE);
        } else {
            replayIcon.setVisibility(View.GONE);
        }
    }

    // toggles progress bar visibility
    private void showProgressBar(boolean playWhenReady, int playbackState) {
        if (playWhenReady && playbackState == Player.STATE_BUFFERING) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        releasePlayer();
    }

    /** Player.EventListener implementation */

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        // updates ui
        showProgressBar(playWhenReady, playbackState);
        showReplayIcon(playbackState);
        if (playbackState == Player.STATE_READY) {
            showPauseButton(!playWhenReady);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.d(TAG, "Tosca onPlayerError: " + error.getMessage());

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
