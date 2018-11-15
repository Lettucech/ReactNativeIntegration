package com.github.brianbridge.reactnativeintegration.view.drawer;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

/**
 * Created by Brian Ho on 15/11/2018.
 */
public class SlideAnimationDrawerListener extends DrawerLayout.SimpleDrawerListener {
    public static final String TAG = SlideAnimationDrawerListener.class.getSimpleName();

    public enum Preset {
        SLIDE_END(0f, 0f, 0f, 0f, 1f, 1f),
        SLIDE_END_SCALE_DOWN(0f, 0f, 0f, 0f, 0.75f, 0.75f),
        SLIDE_END_SCALE_DOWN_ROTATE_Y(0f, 0f, 0f, -5f, 0.75f, 0.75f);

        private float offsetX = 0f;
        private float offsetY = 0f;
        private float rotationX = 0f;
        private float rotationY = 0f;
        private float scaleX = 0f;
        private float scaleY = 0f;

        Preset(float offsetX, float offsetY, float rotationX, float rotationY, float scaleX, float scaleY) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.rotationX = rotationX;
            this.rotationY = rotationY;
            this.scaleX = scaleX;
            this.scaleY = scaleY;
        }
    }

    private float mOffsetX = 0f;
    private float mOffsetY = 0f;
    private float mRotationX = 0f;
    private float mRotationY = 0f;
    private float mScaleX = 0f;
    private float mScaleY = 0f;

    private final View mContentLayout;

    public static Builder builder(View contentLayout) {
        return new Builder(contentLayout);
    }

    public static Builder builder(View contentLayout, Preset preset) {
        return new Builder(contentLayout, preset);
    }

    private SlideAnimationDrawerListener(View contentLayout) {
        this.mContentLayout = contentLayout;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        // Scale
        final float scaleValueX = slideOffset * (1 - mScaleX);
        final float scaleValueY = slideOffset * (1 - mScaleY);
        mContentLayout.setScaleX(1 - scaleValueX);
        mContentLayout.setScaleY(1 - scaleValueY);

        // Translation
        final float nextDrawerWidth = drawerView.getWidth() * slideOffset;
        final float scaledWidth = mContentLayout.getWidth() * scaleValueX / 2; // need one side only, divided by 2
        final float translationX = nextDrawerWidth - scaledWidth;
        final float offsetX = mOffsetX * slideOffset;
        final float offsetY = mOffsetY * slideOffset;
        mContentLayout.setTranslationX(translationX + offsetX);
        mContentLayout.setTranslationY(offsetY);

        // Rotate
        mContentLayout.setRotationX(mRotationX * slideOffset);
        mContentLayout.setRotationY(mRotationY * slideOffset);
    }

    public void setOffset(float offsetX, float offsetY) {
        mOffsetX = offsetX;
        mOffsetY = offsetY;
    }

    public void setRotation(float rotateX, float rotateY) {
        mRotationX = rotateX;
        mRotationY = rotateY;
    }

    public void setScale(float scale) {
        setScale(scale, scale);
    }

    public void setScale(float scaleX, float scaleY) {
        mScaleX = scaleX;
        mScaleY = scaleY;
    }

    public static class Builder {
        private View contentView;
        private float offsetX = 0f;
        private float offsetY = 0f;
        private float rotationX = 0f;
        private float rotationY = 0f;
        private float scaleX = 0f;
        private float scaleY = 0f;

        private Builder(View contentView) {
            this.contentView = contentView;
        }

        private Builder(View contentView, Preset preset) {
            this.contentView = contentView;
            offsetX = preset.offsetX;
            offsetY = preset.offsetY;
            rotationX = preset.rotationX;
            rotationY = preset.rotationY;
            scaleX = preset.scaleX;
            scaleY = preset.scaleY;
        }

        public Builder setOffsetX(float offsetX) {
            this.offsetX = offsetX;
            return this;
        }

        public Builder setOffsetY(float offsetY) {
            this.offsetY = offsetY;
            return this;
        }

        public Builder setRotationX(float rotationX) {
            this.rotationX = rotationX;
            return this;
        }

        public Builder setRotationY(float rotationY) {
            this.rotationY = rotationY;
            return this;
        }

        public Builder setScaleX(float scaleX) {
            this.scaleX = scaleX;
            return this;
        }

        public Builder setScaleY(float scaleY) {
            this.scaleY = scaleY;
            return this;
        }

        public SlideAnimationDrawerListener build() {
            SlideAnimationDrawerListener listener = new SlideAnimationDrawerListener(contentView);
            listener.setOffset(offsetX, offsetY);
            listener.setRotation(rotationX, rotationY);
            listener.setScale(scaleX, scaleY);
            return listener;
        }
    }
}