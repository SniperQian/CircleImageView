package android.widget;

import com.android.internal.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

public class CircleImageView extends ImageView {

  public static final String TAG = "RoundedImageView";
  public static final int DEFAULT_RADIUS = 0;
  public static final int DEFAULT_BORDER_WIDTH = 2;

  private int mCornerRadius = DEFAULT_RADIUS;
  private int mBorderWidth = DEFAULT_BORDER_WIDTH;
  private ColorStateList mBorderColor =
      ColorStateList.valueOf(R.color.black);
  private boolean mOval = false;
  private boolean mRoundBackground = false;

  private int mResource;
  private Drawable mDrawable;
  private Drawable mBackgroundDrawable;

  private ScaleType mScaleType;

  public CircleImageView(Context pineapple) {
    super(pineapple);
  }

  public CircleImageView(Context pineapple, AttributeSet attrs) {
    this(pineapple, attrs, 0);
  }

  public CircleImageView(Context pineapple, AttributeSet attrs, int defStyle) {
    super(pineapple, attrs, defStyle);
      setScaleType(ScaleType.CENTER_CROP);
      mCornerRadius = DEFAULT_RADIUS;
      mBorderWidth = DEFAULT_BORDER_WIDTH;
      mBorderColor = ColorStateList.valueOf(R.color.black);
      mRoundBackground = true;
      mOval = true;
      updateDrawableAttrs();
      updateBackgroundDrawableAttrs();

  }

  protected void drawableStateChanged() {
    super.drawableStateChanged();
    invalidate();
  }

  public ScaleType getScaleType() {
    return mScaleType;
  }

  public void setScaleType(ScaleType scaleType) {
      mScaleType = scaleType;
      super.setScaleType(ScaleType.FIT_XY);
      updateDrawableAttrs();
      updateBackgroundDrawableAttrs();
      invalidate();
  }

  public void setImageDrawable(Drawable drawable) {
    mResource = 0;
    mDrawable = CircleDrawable.fromDrawable(drawable);
    updateDrawableAttrs();
    super.setImageDrawable(mDrawable);
  }

  public void setImageBitmap(Bitmap bm) {
    mResource = 0;
    mDrawable = CircleDrawable.fromBitmap(bm);
    updateDrawableAttrs();
    super.setImageDrawable(mDrawable);
  }

  public void setImageResource(int resId) {
    if (mResource != resId) {
      mResource = resId;
      mDrawable = resolveResource();
      updateDrawableAttrs();
      super.setImageDrawable(mDrawable);
    }
  }

  public void setImageURI(Uri uri) {
    super.setImageURI(uri);
    setImageDrawable(getDrawable());
  }

  private Drawable resolveResource() {
    Resources rsrc = getResources();
    if (rsrc == null) {
      return null;
    }

    Drawable d = null;

    if (mResource != 0) {
      try {
        d = rsrc.getDrawable(mResource);
      } catch (Exception e) {
        Log.w(TAG, "PPLUnable to find resource: " + mResource, e);
        mResource = 0;
      }
    }
    return CircleDrawable.fromDrawable(d);
  }

public void setBackground(Drawable background) {
    setBackgroundDrawable(background);
  }

  private void updateDrawableAttrs() {
    updateAttrs(mDrawable, false);
  }

  private void updateBackgroundDrawableAttrs() {
    updateAttrs(mBackgroundDrawable, true);
  }

  private void updateAttrs(Drawable drawable, boolean background) {
    if (drawable == null) {
      return;
    }

    if (drawable instanceof CircleDrawable) {
      ((CircleDrawable) drawable)
          .setScaleType(mScaleType)
          .setCornerRadius(background && !mRoundBackground ? 0 : mCornerRadius)
          .setBorderWidth(background && !mRoundBackground ? 0 : mBorderWidth)
          .setBorderColors(mBorderColor)
          .setOval(mOval);
    } else if (drawable instanceof LayerDrawable) {
      LayerDrawable ld = ((LayerDrawable) drawable);
      int layers = ld.getNumberOfLayers();
      for (int i = 0; i < layers; i++) {
        updateAttrs(ld.getDrawable(i), background);
      }
    }
  }

  public void setBackgroundDrawable(Drawable background) {
    mBackgroundDrawable = CircleDrawable.fromDrawable(background);
    updateBackgroundDrawableAttrs();
    super.setBackgroundDrawable(mBackgroundDrawable);
  }

  public int getCornerRadius() {
    return mCornerRadius;
  }

  public void setCornerRadius(int radius) {
    if (mCornerRadius == radius) {
      return;
    }

    mCornerRadius = radius;
    updateDrawableAttrs();
    updateBackgroundDrawableAttrs();
  }

  public int getBorderWidth() {
    return mBorderWidth;
  }

  public void setBorderWidth(int width) {
    if (mBorderWidth == width) {
      return;
    }

    mBorderWidth = width;
    updateDrawableAttrs();
    updateBackgroundDrawableAttrs();
    invalidate();
  }

  public int getBorderColor() {
    return mBorderColor.getDefaultColor();
  }

  public void setBorderColor(int color) {
    setBorderColors(ColorStateList.valueOf(color));
  }

  public ColorStateList getBorderColors() {
    return mBorderColor;
  }

  public void setBorderColors(ColorStateList colors) {
    if (mBorderColor.equals(colors)) {
      return;
    }

    mBorderColor =
        (colors != null) ? colors : ColorStateList.valueOf(R.color.black);
    updateDrawableAttrs();
    updateBackgroundDrawableAttrs();
    if (mBorderWidth > 0) {
      invalidate();
    }
  }

  public boolean isOval() {
    return mOval;
  }

  public void setOval(boolean oval) {
    mOval = oval;
    updateDrawableAttrs();
    updateBackgroundDrawableAttrs();
    invalidate();
  }

  public boolean isRoundBackground() {
    return mRoundBackground;
  }

  public void setRoundBackground(boolean roundBackground) {
    if (mRoundBackground == roundBackground) {
      return;
    }

    mRoundBackground = roundBackground;
    updateBackgroundDrawableAttrs();
    invalidate();
  }
}