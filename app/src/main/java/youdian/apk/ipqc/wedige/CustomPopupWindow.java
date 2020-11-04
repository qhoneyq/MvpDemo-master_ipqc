package youdian.apk.ipqc.wedige;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import youdian.apk.ipqc.R;


public class CustomPopupWindow extends PopupWindow {
    private PopupWindow mPopupWindow;
    private View contentView;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private boolean isShowing;

    private CustomPopupWindow(Builder builder) {
        contentView = LayoutInflater.from(mContext).inflate(builder.contentViewId, null);
        mPopupWindow = new PopupWindow(contentView, builder.width, builder.height);
        mPopupWindow.setOutsideTouchable(false);
//        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_popwindow));
        mPopupWindow.setAnimationStyle(builder.animStyle);
    }



        @Override
    public boolean isShowing() {
        return isShowing;
    }

    /**
     * popup 消失
     */
    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
            isShowing = false;
        }
    }


    /**
     * 相对窗体显示位置
     *
     * @param view    弹窗依附view
     * @param gravity 显示位置
     * @param x       x轴偏移量
     * @param y       y轴偏移量
     */
    public void showAtLocation(View view, int gravity, int x, int y) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(view, gravity, x, y);
            isShowing = true;
        }
    }


    /**
     * 显示在anchor控件的正下方，或者相对这个控件的位置
     *
     * @param anchor 依附控件
     * @param xoff 相对控件x轴偏移量
     * @param yoff 相对控件y轴偏移量
     * @param gravity 显示位置
     */
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (mPopupWindow != null) {
            mPopupWindow.showAsDropDown(anchor, xoff, yoff,gravity);
            isShowing = true;
        }
    }

    /**
     * 根据id获取view
     *
     * @param viewId 子viewID
     * @return 子view
     */
    public View getItemView(int viewId) {
        if (mPopupWindow != null) {
            return contentView.findViewById(viewId);
        }
        return null;
    }

    /**
     * 根据id设置pop内部的控件的点击事件的监听
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getItemView(viewId);
        view.setOnClickListener(listener);
    }
   public void setOnClickListener( View.OnClickListener listener) {
       contentView.setOnClickListener(listener);
    }


    public static class Builder {
        private int contentViewId; //pop的布局文件
        private int width; //pop的宽度
        private int height;  //pop的高度
        private int animStyle; //动画效果
        private boolean isCancleable;//是否点击取消

        public Builder(Context context) {
            mContext = context;
        }

        public Builder setContentView(int contentViewId) {
            this.contentViewId = contentViewId;
            return this;
        }

        public Builder setwidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setheight(int height) {
            this.height = height;
            return this;
        }


        public Builder setCancleClickOutSide(boolean isCancleable) {
            this.isCancleable = isCancleable;
            return this;
        }


        public Builder setAnimationStyle(int animStyle) {
            this.animStyle = animStyle;
            return this;
        }

        public CustomPopupWindow build() {
            return new CustomPopupWindow(this);
        }
    }

}
