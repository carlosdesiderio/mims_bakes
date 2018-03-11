package uk.me.desiderio.mimsbakes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uk.me.desiderio.mimsbakes.data.model.Step;


public class StepVideoActivity extends AppCompatActivity {

    public static final String EXTRA_STEP = "extra_step";

    Step step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_video);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        boolean isDualPane = getResources().getBoolean(R.bool.has_two_panes);
        if(isDualPane) {
            setResult(Activity.RESULT_OK, intent);
            finish();
        }


        step = intent.getParcelableExtra(EXTRA_STEP);
    }

    public Step getStepData() {
        return this.step;
    }
}
