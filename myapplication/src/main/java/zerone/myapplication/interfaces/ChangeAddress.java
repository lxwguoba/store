package zerone.myapplication.interfaces;

import android.view.View;

/**
 * Created by Administrator on 2018-11-23.
 */

public interface ChangeAddress {
    /**
     * 保存修改地址
     * @param view
     */
    void onSaveChangeAddress(View view);

    /**
     * 启动地区选择
     * @param view
     */
    void onAreaSelected(View view);
}
