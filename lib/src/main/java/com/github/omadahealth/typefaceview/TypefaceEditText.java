package com.github.omadahealth.typefaceview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.Hashtable;

/**
 * Created by stoyand & oliviergoutay on 4/7/14.
 */
public class TypefaceEditText extends EditText {
    /**
     * A hash we use to hold multiple typefaces for views
     */
    private static final Hashtable<String, Typeface> cache = new Hashtable<>();

    /**
     * The current typeface that the font is set to
     */
    private TypefaceType mCurrentTypeface = TypefaceType.ROBOTO_REGULAR;

    /**
     * The default typeface
     */
    public static final int DEFAULT_TYPEFACE = TypefaceType.ROBOTO_REGULAR.getValue();

    public TypefaceEditText(Context context) {
        super(context);
        setCustomTypeface(context, null);
    }

    public TypefaceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomTypeface(context, attrs);
    }

    public TypefaceEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomTypeface(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TypefaceEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setCustomTypeface(context, attrs);
    }

    /**
     * Sets the typeface for the view
     * @param context
     * @param attrs
     */
    private void setCustomTypeface(Context context, AttributeSet attrs) {
        //Typeface.createFromAsset doesn't work in the layout editor. Skipping...
        if (isInEditMode() || attrs == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return;
        }

        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TypefaceView);
        Integer fontInt = styledAttrs.getInt(R.styleable.TypefaceView_tv_typeface, DEFAULT_TYPEFACE);
        styledAttrs.recycle();

        mCurrentTypeface = TypefaceType.getTypeface(fontInt);
        Typeface typeface = getFont(context, mCurrentTypeface.getAssetFileName());
        setTypeface(typeface);
    }

    /**
     * Avoid reloading assets every time
     */
    public static Typeface getFont(Context context, String fontName) {
        synchronized (cache) {
            if (!cache.containsKey(fontName)) {
                try {
                    Typeface t = Typeface.createFromAsset(context.getAssets(), fontName);
                    cache.put(fontName, t);
                } catch (Exception e) {
                    return null;
                }
            }
            return cache.get(fontName);
        }
    }

    /**
     * Returns the currently set typeface of this view
     */
    public TypefaceType getCurrentTypeface(){
        return mCurrentTypeface;
    }
}
