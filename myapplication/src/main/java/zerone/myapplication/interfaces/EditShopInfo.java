package zerone.myapplication.interfaces;

import android.view.View;

/**
 * Created by Administrator on 2018-11-23.
 */

public interface EditShopInfo {
    /**
     *  修改地址的方法
     * @param view
     */
    public void onAddressUpData(View view);

    /**
     * 保存修改信息的方法
     * @param view
     */
    public void onSaveChangeInfo(View view);

    /**
     * 修改店铺LOGO的方法
     * @param view
     */
    public void onChangeLOGO(View view);

    /**
     * 修改门店照的方法
     * @param view
     */
    public void onChangeShopPhoto(View view);
}
