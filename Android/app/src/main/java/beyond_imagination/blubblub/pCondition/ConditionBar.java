package beyond_imagination.blubblub.pCondition;

import android.content.Context;
import android.content.res.TypedArray;

import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import beyond_imagination.blubblub.MainActivity;
import beyond_imagination.blubblub.R;
import beyond_imagination.blubblub.Setting;

/**
 * Created by cru65 on 2017-07-28.
 */
/**
 * @file ConditionBar.java
 * @breif
 * Class include all of things about bowl condition data
 * By this data, you can check bowl condition and maintain it.
 * And you can check total score of bowl. Maybe this can give interesting about maintaining your bowl
 * @author Yehun Park
 */
public class ConditionBar extends LinearLayout {
    /****************/
    /*** Variable ***/
    /****************/
    MainActivity mainActivity;

    Setting setting;

    // 탁도가 좋지 않을 경우 계속해서 total점수를 깍기 위해 만든 count
    double turbiditycount;

    // For condition datas
    ImageView imagefeed;
    Button feedbtn;
    TextView texttemperature;
    ImageView imagetemperature;
    TextView textquality;
    ImageView imagequality;
    Switch lightswitch;
    ImageView imagelight;
    ImageView imagetotal;
    TextView texttotal;

    /****************/
    /*** Function ***/
    /****************/
    public ConditionBar(Context context) {
        super(context);
        Log.d("ConditionBar", "Constructor execute");
        mainActivity = (MainActivity) context;
        init();
    }

    public ConditionBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.d("ConditionBar", "Constructor execute");
        mainActivity = (MainActivity) context;
        init();
        getAttrs(attrs);
    }

    public ConditionBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d("ConditionBar", "Constructor execute");
        mainActivity = (MainActivity) context;
        init();
        getAttrs(attrs, defStyleAttr);
    }

    private void init() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.layout_conditionbar, this, false);
        addView(v);

        turbiditycount = 0;

        setting = mainActivity.getSetting();

        feedbtn = (Button) findViewById(R.id.feedBtn);
        // 처음에는 비활성화
        feedbtn.setEnabled(false);

        feedbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 누르면 먹이 줌.
                mainActivity.sendRequestToBowl("먹이");

                // 먹이주고 다시 비활성화.
                setEnabled(false);
            }
        });

        lightswitch = (Switch) findViewById(R.id.lightSwitch);
        lightswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mainActivity.sendRequestToBowl("어두움");
                } else {
                    mainActivity.sendRequestToBowl("밝음");
                }
            }
        });

        texttemperature = (TextView) findViewById(R.id.textTemperature);
        textquality = (TextView) findViewById(R.id.textQuality);
        texttotal = (TextView) findViewById(R.id.textTotal);

        imagefeed = (ImageView) findViewById(R.id.viewFeed);
        imagetemperature = (ImageView) findViewById(R.id.viewTemperature);
        imagequality = (ImageView) findViewById(R.id.viewQuality);
        imagelight = (ImageView) findViewById(R.id.viewLight);
        imagetotal = (ImageView) findViewById(R.id.viewTotal);

        Log.d("ContidionBar", "init()-success)");
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ConditionBar);

        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ConditionBar, defStyle, 0);

        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        int img_id = typedArray.getResourceId(R.styleable.ConditionBar_feed_img, R.mipmap.ic_launcher);
        imagefeed.setBackgroundResource(img_id);
        img_id = typedArray.getResourceId(R.styleable.ConditionBar_temperature_img, R.mipmap.ic_launcher);
        imagetemperature.setBackgroundResource(img_id);
        img_id = typedArray.getResourceId(R.styleable.ConditionBar_quality_img, R.mipmap.ic_launcher);
        imagequality.setBackgroundResource(img_id);
        img_id = typedArray.getResourceId(R.styleable.ConditionBar_light_img, R.mipmap.ic_launcher);
        imagelight.setBackgroundResource(img_id);

        typedArray.recycle();
    }

    /**
     * @breif
     * Get condition data and processing condition data to suit BlubBlub UI
     * feed time is last feeding time. So we compare with now and check is it time to feed.
     * you can use feed button 1 hour before setted feedtime.
     * In turbidity, 0 is Good! and high score is Bad...
     * Finally, we can check total bowl condition score.
     * @param feedtime
     * @param temperature
     * @param illumination
     * @param turbidity
     */
    public void onConditionUpdate(String feedtime, String temperature, String illumination, String turbidity) {
        Log.d("ConditionBar", "onConditionUpdate");

        setting = mainActivity.getSetting();

        // time after feeding (min)
        // 8시간 이상이면 자동으로 버튼 활성화
        /* 에러 발생 */
        // 해결 : http://freeism.co.kr/tc/801
        Calendar calendar = Calendar.getInstance(Locale.KOREA);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        int time = (hour * 60) + min;

        // 1시간 전 부터 먹이 주기 가능
        if (Math.abs(time - Integer.valueOf(feedtime)) > (setting.getFeed_cycle() - 1) * 60)
            feedbtn.setEnabled(true);

        String showtime;
        showtime = (Integer.valueOf(feedtime) / 60) + ":";
        if (Integer.valueOf(feedtime) % 60 < 10)
            showtime += "0" + (Integer.valueOf(feedtime) % 60);
        else
            showtime += (Integer.valueOf(feedtime) % 60);
        feedbtn.setText(showtime);

        // temperature
        texttemperature.setText(temperature);

        // illumination
        //lightbtn.setText(illumination);
        lightswitch.setText(illumination);

        // turbidity
        if (Float.valueOf(turbidity) == 0.0) {
            textquality.setText("좋음");
            turbiditycount = 0;
        } else if (Float.valueOf(turbidity) == 1.0) {
            textquality.setText("보통");
            turbiditycount += 0.0042;   // 10초마다 0.0042점 감소
        } else {
            textquality.setText("나쁨");
        }

        // check total bowl score.
        texttotal.setText(getTotalScore(feedtime, temperature, illumination, turbidity));
    }

    /**
     * @breif
     * You can check your total management score.
     * If temperature over and under your setted temperature value, score will be decrease.
     * If you maintain your turbidity bad, your total score will be decrease.
     * @param feedtime
     * @param temperature
     * @param illumination
     * @param turbidity
     * @return
     */
    private String getTotalScore(String feedtime, String temperature, String illumination, String turbidity) {
        int score = 100;
        float feedtime_t = Float.valueOf(feedtime);
        float temperature_t = Float.valueOf(temperature);
        float illumination_t = Float.valueOf(illumination);
        float turbidity_t = Float.valueOf(turbidity);

        Log.d("ConditionBar", "getTotalScore");

        // 온도 점수 40점 부여
        // 최대30, 최소20 온도가 되면 0점이 되도록
        double tmpAverage = (double)(setting.getTmp_max() + setting.getTmp_min())/2;
        double tmpDiff = setting.getTmp_max() - tmpAverage;
        score = score - (int)((Math.abs(tmpAverage - temperature_t) / tmpDiff) * 40);

        // 탁도 점수
        // 10초 0.0042점 감소.
        score = score - (int)turbiditycount;

        return String.valueOf(score);
    }

    /**
     * @breif
     * Control Feed Button, Visible and invisible.
     * @param order
     */
    public void controllFeedBtn(boolean order) {
        if (order == true) {
            if (feedbtn.getVisibility() == View.INVISIBLE) {
                feedbtn.setVisibility(View.VISIBLE);
            }
        } else {
            if (feedbtn.getVisibility() == View.VISIBLE) {
                feedbtn.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * @breif
     * Control Illumination Switch, On and Off.
     * @param order
     */
    public void controllIllumSwitch(boolean order) {
        if (order) {
            lightswitch.setChecked(true);
        } else {
            lightswitch.setChecked(false);
        }
    }

    ////
    // Getter, Setter
    ////
    public ImageView getImagefeed() {
        return imagefeed;
    }

    public void setImagefeed(ImageView imagefeed) {
        this.imagefeed = imagefeed;
    }

    public ImageView getImagetemperature() {
        return imagetemperature;
    }

    public void setImagetemperature(ImageView imagetemperature) {
        this.imagetemperature = imagetemperature;
    }

    public ImageView getImagequality() {
        return imagequality;
    }

    public void setImagequality(ImageView imagequality) {
        this.imagequality = imagequality;
    }

    public ImageView getImagelight() {
        return imagelight;
    }

    public void setImagelight(ImageView imagelight) {
        this.imagelight = imagelight;
    }

    public Button getFeedbtn() {
        return feedbtn;
    }
}
