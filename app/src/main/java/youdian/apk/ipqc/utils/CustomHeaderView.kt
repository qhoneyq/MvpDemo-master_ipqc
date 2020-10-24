package youdian.ipqc.ipqc.utils

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import youdian.apk.ipqc.R

/**
 * 作者 create by H7111906 on 2020/4/23
 */
open class CustomHeaderView : RelativeLayout {
    private var mContext: Context
    private var showLeftIcon : Boolean = true
    private lateinit var imgLeft: ImageView
    private lateinit var imgRight: ImageView
    private lateinit var tvCenter: TextView
    private lateinit var headerView: View

    constructor(context: Context) : super(context) {
        mContext = context
        initView(mContext)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
        initView(mContext)
        initAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defAttributeSet: Int) : super(context, attrs, defAttributeSet) {
        mContext = context
        initView(mContext)
        initAttrs(context, attrs)
    }

    /**
     *初始化属性
     * @param context Context
     * @param attrs AttributeSet
     */
    private fun initAttrs(context: Context, attrs: AttributeSet) {
        val mTypedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomHeaderView)
        val titleBackground = mTypedArray.getResourceId(R.styleable.CustomHeaderView_title_background, R.color.colorPrimary)
        val leftIcon = mTypedArray.getResourceId(R.styleable.CustomHeaderView_left_icon, R.mipmap.home_icon_return)
        val rightIcon = mTypedArray.getResourceId(R.styleable.CustomHeaderView_left_icon, R.mipmap.icon_scan)
        showLeftIcon = mTypedArray.getBoolean(R.styleable.CustomHeaderView_showLeftIcon, true)
        val titleText = mTypedArray.getString(R.styleable.CustomHeaderView_title_text)
        val titleTextSize = mTypedArray.getDimension(R.styleable.CustomHeaderView_title_text_size, resources.getDimension(R.dimen.sp_8))
        val titleTextColor = mTypedArray.getColor(R.styleable.CustomHeaderView_title_text_color, Color.WHITE)
        mTypedArray.recycle()
        headerView.setBackgroundResource(titleBackground)
        imgLeft.apply {
            setImageResource(leftIcon)
            visibility = if (showLeftIcon) View.VISIBLE else View.GONE
        }
        tvCenter.apply {
            text = titleText
            textSize = titleTextSize
            setTextColor(titleTextColor)
        }
    }

    /**
     * 绑定控件
     * @param context Context
     */
    private fun initView(context: Context) {
        headerView = LayoutInflater.from(context).inflate(R.layout.custom_header_view, this, true)
        imgLeft = findViewById(R.id.iv_left)
        imgRight = findViewById(R.id.img_right)
        tvCenter = findViewById(R.id.tv_center)
    }

    /**
     * 左侧按钮点击事件
     * @param onClickListener OnClickListener
     */
    open fun setLeftClick(onClickListener: OnClickListener) {
        imgLeft.setOnClickListener(onClickListener)
    }

    /**
     * 设置左侧按钮Icon
     * @param resId Int
     */
    open fun setLeftIcon(resId: Int){
        imgLeft.setImageResource(resId)
    }
 /**
     * 左侧按钮点击事件
     * @param onClickListener OnClickListener
     */
    open fun setRightClick(onClickListener: OnClickListener) {
        imgRight.setOnClickListener(onClickListener)
    }

    /**
     * 设置左侧按钮Icon
     * @param resId Int
     */
    open fun setRightIcon(resId: Int){
        imgRight.setImageResource(resId)
    }

    /**
     * 设置左侧按钮Icon下移
     * @param bias Int
     */
    open fun setIconBias(bias: Int){
        imgLeft.setPaddingRelative(0,bias,0,0);
    }

    /**
     * 设置Header标题
     * @param title String
     */
    open fun setTitleText(title: String) {
            tvCenter.text = title
    }

    /**
     * 是否显示左侧按钮
     * @param isShow Boolean
     */
    open fun showIcon(isShow: Boolean) {
        imgLeft.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    /**
     * 是否显示左侧按钮
     * @param isShow Boolean
     */
    open fun showRightIcon(isShow: Boolean) {
        imgRight.visibility = if (isShow) View.VISIBLE else View.GONE
    }

}