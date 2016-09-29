package radionews.com.frequency;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AudioNewsActivity extends AppCompatActivity {
    private ImageButton playButton;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();;
    private int backwardTime = 10000;
    private SeekBar seekbar;
    private TextView time;
    private TextView date;
    public static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_news);

        playButton = (ImageButton) findViewById(R.id.play_button);
        time = (TextView)findViewById(R.id.news_timer);
        date = (TextView) findViewById(R.id.date);
        mediaPlayer = MediaPlayer.create(this, R.raw.sample);
        seekbar=(SeekBar)findViewById(R.id.news_seekbar);
        seekbar.setClickable(false);

        date.setText(getFormattedTodaysDate());

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mediaPlayer.isPlaying()){
                    if(mediaPlayer!=null){
                        mediaPlayer.pause();
                        // Changing button image to play button
                        playButton.setImageResource(R.drawable.play);
                    }
                }else{
                    // Resume song
                    if(mediaPlayer!=null){
                        mediaPlayer.start();
                        // Changing button image to pause button
                        playButton.setImageResource(R.drawable.pause);
                    }
                }
               // Toast.makeText(getApplicationContext(), "Playing sound",Toast.LENGTH_SHORT).show();
               // mediaPlayer.start();

                finalTime = mediaPlayer.getDuration();
                startTime = mediaPlayer.getCurrentPosition();

                if (oneTimeOnly == 0) {
                    seekbar.setMax((int) finalTime);
                    oneTimeOnly = 1;
                }
                time.setText(String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                );

                seekbar.setProgress((int)startTime);
                myHandler.postDelayed(UpdateSongTime,100);

            }
        });

    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            time.setText(String.format("%d min, %d sec",

                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
            seekbar.setProgress((int)startTime);
            myHandler.postDelayed(this, 100);
        }
    };

    String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }

    String getFormattedTodaysDate(){
        Date todaysDate = new Date();
        SimpleDateFormat formatDayOfMonth  = new SimpleDateFormat("d");
        SimpleDateFormat monthOfDate = new SimpleDateFormat("MMMM",Locale.US);
        int day = Integer.parseInt(formatDayOfMonth.format(todaysDate));
        String month = monthOfDate.format(todaysDate);
        String formattedDate = day + getDayOfMonthSuffix(day) + " "+ month;
        return formattedDate;
    }

}
