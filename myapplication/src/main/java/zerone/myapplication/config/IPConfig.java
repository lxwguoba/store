package zerone.myapplication.config;

/**
 * Created by 刘兴文 on 2018-12-01.]
 * ctrl+shift+u （大小写切换）
 */

public interface IPConfig {
    //登录接口
    public static String LOGIN = "http://zerone.01nnt.com/api/store/login";
    //添加分类
    public static String ADDCAT = "http://zerone.01nnt.com/api/store/category_add";
    //编辑分类
    public static String EDITCAT = "http://zerone.01nnt.com/api/store/category_edit";
    //分类列表
    public static String CATLIST = "http://zerone.01nnt.com/api/store/category_list";
    //移除分类
    public static String DELETECAT = "http://zerone.01nnt.com/api/store/category_delete";
    //添加商品
    public static String ADDPRODUCT = "http://zerone.01nnt.com/api/store/goods_add";
    //添加图片
    public static String ADD_PRODUCT_PICTURE = "http://zerone.01nnt.com/api/store/goods_image";
    //商品列表
    public static String PRODUCT_LIST = "http://zerone.01nnt.com/api/store/goods_list";
    //商品编辑
    public static String PRODUCT_EDIT = "http://zerone.01nnt.com/api/store/goods_edit";
    //商品图片列表
    public static String PRODUCT_IMG = "http://zerone.01nnt.com/api/store/goods_image_list";
    //删除图片中
    public static String PICTURE_DELETE = "http://zerone.01nnt.com/api/store/goods_image_delete";
    //首页订单信息
    public static String PAY_ORDER_INFO = "http://zerone.01nnt.com/api/store/display";
    //修改密码
    public static String CHANGE_PASSWORD = "http://zerone.01nnt.com/api/store/password_edit";
    //修改安全密码
    public static String SAFE_PASSWORD_EDIT = "http://zerone.01nnt.com/api/store/safe_password_edit";
    //登录日记
    public static String LOGIN_LOG = "http://zerone.01nnt.com/api/store/login_log";
    //操作日志
    public static String OPERATION_LOG = "http://zerone.01nnt.com/api/store/operation_log";
    //店铺信息
    public static String SHOPINFO = "http://zerone.01nnt.com/api/store/store_info";
    //修改店铺信息
    public static String STORE_EDIT = "http://zerone.01nnt.com/api/store/store_edit";
    //会员列表
    public static String USER_LIST = "http://zerone.01nnt.com/api/store/user_list";
    //店铺动态
    public static String DYNAMIC_LIST = "http://zerone.01nnt.com/api/store/dynamic_list";
    //发布动态
    public static String DYNAMIC_ADD = "http://zerone.01nnt.com/api/store/dynamic_add";
    //新增优惠卷
    public static String CUOPON_ADD = "http://zerone.01nnt.com/api/store/cuopon_add";
    //优惠卷编辑
    public static String CUOPON_EDIT = "http://zerone.01nnt.com/api/store/cuopon_edit";
    //优惠卷列表
    public static String CUOPON_LIST = "http://zerone.01nnt.com/api/store/cuopon_list";
    //获取海报分类
    public static String POSTER = "http://zerone.01nnt.com/api/store/poster";
    //添加海报
    public static String POSTER_ADD = "http://zerone.01nnt.com/api/store/poster_add";
    //海报列表
    public static String POSTER_LIST = "http://zerone.01nnt.com/api/store/poster_list";
    //钱包流水
    public static String STREAM_LOG = "http://zerone.01nnt.com/api/store/stream_log";
    //收款二维码
    public static String RECEIPT_CODE = "http://zerone.01nnt.com/api/store/receipt_code";
    //创建收款二维码
    public static String RECEIPT_CODE_ADD = "http://zerone.01nnt.com/api/store/receipt_code_add";
    //添加小助手的店员
    public static String CLERK_ADD = "http://zerone.01nnt.com/api/store/clerk_add";
    //店员列表
    public static String CLERK_LIST = "http://zerone.01nnt.com/api/store/clerk_list";
    //编辑店员
    public static String CLERK_EDIT = "http://zerone.01nnt.com/api/store/clerk_edit";
    //移除店员
    public static String CLERK_DELETE = "http://zerone.01nnt.com/api/store/clerk_delete";
    //获取省份
    public static String PROVINCE = "http://zerone.01nnt.com/api/store/province";
    //获取市
    public static String CITY = "http://zerone.01nnt.com/api/store/city";
    //获取区
    public static String AREA = "http://zerone.01nnt.com/api/store/area";
    //获取收款资料
    public static String APPLY_DATA = "http://zerone.01nnt.com/api/store/apply_data";
    //提交或者编辑收款资料
    public static String APPLY_DATA_CHECK = "http://zerone.01nnt.com/api/store/apply_data_check";
    //收款资料-图片集
    public static  String APPLY_IMG="http://zerone.01nnt.com/api/store/apply_img";
    //上传收款资料的图片
    public  static String APPLY_IMG_CHECK="http://zerone.01nnt.com/api/store/apply_img_check";
}
