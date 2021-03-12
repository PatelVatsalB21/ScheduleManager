//package com.example.schedulemanager.Task;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//
//public class SlideButton extends androidx.appcompat.widget.AppCompatSeekBar {
//
//    private Drawable thumb;
//    private SlideButtonListener listener;
//
//    public SlideButton(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    public void setThumb(Drawable thumb) {
//        super.setThumb(thumb);
//        this.thumb = thumb;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (thumb.getBounds().contains((int) event.getX(), (int) event.getY())) {
//                super.onTouchEvent(event);
//            } else
//                return false;
//        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (getProgress() > 70)
//                handleSlide();
//
//            setProgress(50);
//        } else
//            super.onTouchEvent(event);
//
//        return true;
//    }
//
//
//    private void handleSlide() {
//        listener.handleSlide();
//    }
//
//    public void setSlideButtonListener(SlideButtonListener listener) {
//        this.listener = listener;
//    }
//
//    public interface SlideButtonListener {
//        void handleSlide();
//    }
//}
//
