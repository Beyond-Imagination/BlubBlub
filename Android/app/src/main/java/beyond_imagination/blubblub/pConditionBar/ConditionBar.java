package beyond_imagination.blubblub.pConditionBar;

import android.content.Context;
import android.content.res.TypedArray;

import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import beyond_imagination.blubblub.MainActivity;
import beyond_imagination.blubblub.R;
import beyond_imagination.blubblub.Setting;

/**
 * Created by cru65 on 2017-07-28.
 */

public class ConditionBar extends LinearLayout {
    /*** Variable ***/
    MainActivity mainActivity;

    Setting setting;

    TextView textfeed;
    ImageView imagefeed;
    Button feedbtn;
    TextView texttemperature;
    ImageView imagetemperature;
    TextView textquality;
    ImageView imagequality;
    TextView textlight;
    ImageView imagelight;

    /*** Function ***/
    public ConditionBar(Context context) {
        super(context);
        mainActivity = (MainActivity) context;
        init();
    }

    public ConditionBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mainActivity = (MainActivity) context;
        init();
        getAttrs(attrs);
    }

    public ConditionBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mainActivity = (MainActivity) context;
        init();
        getAttrs(attrs, defStyleAttr);
    }

    private void init() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.layout_conditionbar, this, false);
        addView(v);

        feedbtn = (Button) findViewById(R.id.feedBtn);
        //feedbtn.setEnabled(false);
        feedbtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 누르면 먹이 줌.
                mainActivity.sendRequestToBowl("먹이");

                // 먹이주고 다시 비활성화.
                setEnabled(false);
            }
        });

        //textfeed = (TextView) findViewById(R.id.textFeed);
        texttemperature = (TextView) findViewById(R.id.textTemperature);
        textquality = (TextView) findViewById(R.id.textQuality);
        textlight = (TextView) findViewById(R.id.textLight);

        imagefeed = (ImageView) findViewById(R.id.viewFeed);
        imagetemperature = (ImageView) findViewById(R.id.viewTemperature);
        imagequality = (ImageView) findViewById(R.id.viewQuality);
        imagelight = (ImageView) findViewById(R.id.viewLight);
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

    // Condition Update
    public void onConditionUpdate(String feedtime, String temperature, String illumination, String turbidity) {
        if (setting == null) {
            setting = mainActivity.getSetting();
        }

        // time after feeding (min)
        // 8시간 이상이면 자동으로 버튼 활성화
        /* 에러 발생 */
        // 해결 : http://freeism.co.kr/tc/801
        Calendar calendar = Calendar.getInstance(Locale.KOREAN);
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);

        int time = (hour * 60) + min;

        // 1시간 전 부터 먹이 주기 가능
        if ((time - Integer.valueOf(feedtime)) > (setting.getFeed_cycle() - 1) * 60)
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
        textlight.setText(illumination);

        // turbidity

        if (Float.valueOf(turbidity) == 0.0) {
            textquality.setText("좋음");
        } else if (Float.valueOf(turbidity) == 1.0) {
            textquality.setText("보통");
        } else {
            textquality.setText("나쁨");
        }

    }

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
