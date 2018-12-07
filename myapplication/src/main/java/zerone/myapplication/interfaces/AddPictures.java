package zerone.myapplication.interfaces;

import android.view.View;

/**
 * Created by Administrator on 2018-11-26.
 */

public interface AddPictures {
    /**
     * 打开选择图片
     * @param view
     */
    void onOpenPhotos(View view);

    /**
     * 保存图片到服务器
     * @param view
     */
    void onSavePictures(View view);

}
