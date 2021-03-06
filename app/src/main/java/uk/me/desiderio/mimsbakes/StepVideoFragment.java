package uk.me.desiderio.mimsbakes;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import static uk.me.desiderio.mimsbakes.StepVideoActivity.EXTRA_STEP;

/**
 * Fragment to show video and description of the current {@link Step}
 * <p>
 * The class will be attached to either {@link StepVideoActivity} and
 * {@link RecipeDetailsActivity} depending on the device and rotation.
 */
public class StepVideoFragment extends Fragment implements
        Player.EventListener {

    private static final String TAG = StepVideoFragment.class.getSimpleName();

    public static final String EXTRA_PLAYER_POSITION = "player_position";
    public static final String EXTRA_PLAYER_PLAY_WHEN_READY = "player_when_ready";

    private TextView descriptionTextView;
    private ProgressBar progressBar;
    private ImageView replayIcon;
    private ImageView pauseIcon;
    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView playerView;
    private ExtractorMediaSource.Factory mediaSourceFactory;
    private long playerPositionMs;
    private boolean playWhenReady;
    private Step step;

    public StepVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_video,
                                         container,
                                         false);
        descriptionTextView = rootView.findViewById(R.id.step_description_text_view);
        progressBar = rootView.findViewById(R.id.step_video_progress_bar);
        replayIcon = rootView.findViewById(R.id.step_video_replay_image_view);
        pauseIcon = rootView.findViewById(R.id.step_video_pause_image_view);

        playerView = rootView.findViewById(R.id.player_view);

        playerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.recipe_image_loading));

        playerView.setControllerHideOnTouch(false);
        playerView.setControllerShowTimeoutMs(0);

        playerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    startPlaybackWhenReady();
                }
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getActivity() instanceof StepVideoActivity) {
            step = ((StepVideoActivity) getActivity()).getStepData();
        }

        playWhenReady = true;
        if(savedInstanceState != null) {
            playerPositionMs = savedInstanceState.getLong(EXTRA_PLAYER_POSITION);
            playWhenReady = savedInstanceState.getBoolean(EXTRA_PLAYER_PLAY_WHEN_READY);
            step = savedInstanceState.getParcelable(EXTRA_STEP);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();

        if(step != null) {
            swapData(step);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        if (exoPlayer != null) {
            outState.putLong(EXTRA_PLAYER_POSITION, exoPlayer.getCurrentPosition());
            outState.putBoolean(EXTRA_PLAYER_PLAY_WHEN_READY, exoPlayer.getPlayWhenReady());
        }

        if(step != null) {
            outState.putParcelable(EXTRA_STEP, step);
        }
    }

    /**
     * updates view with data provided as its parameter
     */
    public void swapData(Step step) {
        this.step = step;
        updateViewOnDataChange(step);
    }

    private void updateViewOnDataChange(Step step) {
        descriptionTextView.setText(step.getDescription());
        /* at this stage of implementation,
           error handling is relied upon the
           player itself */
        Uri mediaUri = Uri.parse(step.getVideoURLString());
        preparePlayer(mediaUri);
    }

    private void initializePlayer() {
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),
                                                           trackSelector);

            playerView.setPlayer(exoPlayer);
            exoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(),
                                                 "MimsBakes");
            DataSource.Factory dataSourceFactory =
                    new DefaultDataSourceFactory(getContext(), userAgent);
            mediaSourceFactory = new ExtractorMediaSource.Factory
                    (dataSourceFactory);
        }
    }

    private void preparePlayer(Uri mediaUri) {
        if (exoPlayer != null) {
            exoPlayer.stop();
        }
        exoPlayer.seekTo(playerPositionMs);
        exoPlayer.setPlayWhenReady(playWhenReady);
        MediaSource mediaSource = mediaSourceFactory.createMediaSource(mediaUri);
        exoPlayer.prepare(mediaSource);
    }

    private void startPlaybackWhenReady() {
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
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
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
    public void onTimelineChanged(Timeline timeline, Object manifest) { }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups,
                                TrackSelectionArray trackSelections) { }

    @Override
    public void onLoadingChanged(boolean isLoading) { }

    @Override
    public void onRepeatModeChanged(int repeatMode) { }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) { }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.d(TAG, "Tosca onPlayerError: " + error.getMessage());
    }

    @Override
    public void onPositionDiscontinuity(int reason) { }

    @Override
    public void onPlaybackParametersChanged(
            PlaybackParameters playbackParameters) { }

    @Override
    public void onSeekProcessed() { }
}
