package vip.frendy.cv

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test.*
import vip.frendy.opencv.OpenCVManager

/**
 * Created by frendy on 2018/7/6.
 */
class TestActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        //测试图片
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        //显示原图
        image.setImageBitmap(bitmap)

        bw.setOnClickListener {
            //转换
            val bwBitmap = OpenCVManager.getInstance().toBW(bitmap)
            //显示图片
            image.setImageBitmap(bwBitmap)
        }
    }
}