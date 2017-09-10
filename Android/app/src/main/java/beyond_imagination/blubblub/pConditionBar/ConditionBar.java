package beyond_imagination.blubblub.pConditionBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import beyond_imagination.blubblub.R;
/**
 * Created by cru65 on 2017-07-28.
 */

public class ConditionBar extends LinearLayout{
    /*** Variable ***/
    TextView textfeed;
    ImageView imagefeed;
    TextView texttemperature;
    ImageView imagetemperature;
    TextView textquality;
    ImageView imagequality;
    TextView textlight;
    ImageView imagelight;

    /*** Function ***/
    public ConditionBar(Context context) {
        super(context);
        init();
    }

    public ConditionBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        getAttrs(attrs);
    }

    public ConditionBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        getAttrs(attrs, defStyleAttr);
    }

    private void init() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.layout_conditionbar, this, false);
        addView(v);

        textfeed = (TextView) findViewById(R.id.textFeed);
        texttemperature = (TextView) findViewById(R.id.textTemperature);
        textquality = (TextView) findViewById(R.id.textQuality);
        textlight = (TextView)findViewById(R.id.textLight);

        imagefeed = (ImageView)findViewById(R.id.viewFeed);
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
    public void onConditionUpdate(String temperature, String illumination, String turbidity)
    {
        texttemperature.setText(temperature);
        textlight.setText(illumination);
        textquality.setText(turbidity);
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

}
