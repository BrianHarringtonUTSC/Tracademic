package ca.utoronto.utsc.tracademia;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by markus on 9/15/15.
 */
public class AwardPointsActivity  extends AppCompatActivity implements OnClickListener{
    int num_points = 5;
    String name = "Bob";
    PointType pointType = PointType.Challenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.award_points);

        Intent intent = getIntent();
        String libraryNumber = intent.getStringExtra(getString(R.string.libraryNumber));
        
    }

    public void onClick(View v) {
        if(v.getId()==R.id.give_point){
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.confirm_title)
                    .setMessage("You are giving" + name +" "+ num_points + " " + pointType.toString() +"points?")
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Stop the activity
                            AwardPointsActivity.this.finish();
                        }
                    }).setNegativeButton(R.string.cancel, null)
                            .show();
        }
    }
}