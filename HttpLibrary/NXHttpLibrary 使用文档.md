版本：1.0.9

主要功能：

1. 基本的get、post、put等网络请求
2. 文件下载及下载进度回调
3. 文件上传及上传进度回调
4. 支持自定义缓存模式
5. 支持自定义超时时间及自动重连次数
6. 支持链式调用
7. 支持https访问及自定义ssl证书
8. 支持根据tag取消请求
9. 支持自定义callback，自动解析网络数据

主要类说明：

* **NXHttpManager**：网络请求管理入口类。此类可设置网络请求、缓存策略、通用请求参数、通用请求头等配置，触发网络请求。
* **HttpOptions**:配置相关类。为NXHttpManager提供默认配置，也可自行修改配置。
* **CallBack**：网络请求结果回调接口。请求成功回调顺序为：onStart -\>onSuccess -\>onFinish ，其中onSucces中提供isFromCache字段标明缓存返回或网络返回。 请求失败回调顺序为：onStart -\>onError -\>onFinish
* **NXDeprecateCallback:**CallBack实现类，为诺信项目特殊设计。包含对NXResponse类的封装以及BaseBean封装
* **BaseBean 包含**网络请求通用返回参数 resCode、resMsg



使用方式：

1.Project根目录的build.gradle中配置maven地址：

    allprojects {
        repositories {
            maven { url "http://192.168.1.135:8081/repository/NXRepositories/" }
            jcenter()
        }

2.要使用的moudle的build.gradle中配置：

    compile 'com.android.nx:http-library:1.0.9'

3.初始化本网络框架：（使用前必须初始化，建议在application中）

    //初始化http框架
    HttpOptions options = new HttpOptions.Builder().setCommonParams("appid", "1")
            .setCommonParams("phone\_system", "1")
            .setCommonParams("version\_code", "v1.2")//通用参数设置，每次网络请求会携带该参数
     .setConnectTimeout(1000) //链接超时时间
     .setRetryCount(0) //重试次数
     .setCacheMode(CacheMode.NO\_CACHE) //设置缓存模式，默认为如果网络获取失败使用缓存。
     .setNeedResponseTest(true) //使用自定义服务器返回测试模式 ，测试数据在assets目录下存放自定义的json文本。默认不使用 。
     .build();
    NXHttpManager.getInstance().init(this, options);

4.项目中新建NTCommonCallBack继承NXDeprecateCallback,下面是牛谈项目的示例：

    /\*\*
     \* @类描述： TODO
     \* @创建人：王成丞
     \* @创建时间：2017/8/23 10:41
     \*/
    public abstract class NTCommonCallBack<T> extends NXDeprecateCallback<T> {

        private IBaseView view;
     private boolean isShowDialog = true;

     public NTCommonCallBack(IBaseView view, boolean isShowDialog) {

            this.view = view;
     this.isShowDialog = isShowDialog;
     }

        public NTCommonCallBack(IBaseView view) {

            this.view = view;
     }

        @Override
     public void onStart(Request request) {
            if (view != null && isShowDialog)
                view.showLoadingDialog("正在加载...");
     super.onStart(request);
     }

        @Override
     public void onSuccess(Response<T> nxResponse) {

            if (nxResponse == null || nxResponse.body() == null) {

                view.showToastMessage("网络异常");
     onError(nxResponse);
     return;
     }

            T bodyT = nxResponse.body();
     if (parameterType == NXResponse.class) {

                NXResponse body = (NXResponse) bodyT;
     if (nxResponse != null && ConstantValue.OGTOLOGINCODE.equals(body.getResCode())) {
                    logOut(body.getResCode());
     return;
     }
            } else if (parameterType.getSuperclass() == BaseBean.class || parameterType == BaseBean.class) {
                //解析json获取recognizeResult对象
     BaseBean body = (BaseBean) bodyT;
     if (nxResponse != null && ConstantValue.OGTOLOGINCODE.equals(body.getResCode())) {
                    logOut(body.getResCode());
     return;
     }

            }

            onNiuTanSuccess(nxResponse);

     }

        protected abstract void onNiuTanSuccess(Response<T> nxResponse);

     @Override
     public void onFinish() {
            if (view != null && isShowDialog)
                view.cancelLoadingDialog();
     super.onFinish();
     }

        public void logOut(String status) {
            if (ConstantValue.OGTOLOGINCODE.equals(status)) {
                String mess = "发现您的帐号在其它设备上登录,请重新登录";
     String title = "异地登录";
     Intent intent = new Intent();
     intent.setAction(ConstantValue.goToLogin);
     intent.putExtra("message", mess);
     intent.putExtra("msgType", status);
     BaseToken.getContext().sendBroadcast(intent);
     }
        }
    }

5.项目中使用：

    /\*\*
     \* 获取竞猜活动信息详情
     \*
     \* @param MID 活动MID
     \* @param isRefreshList
     \*/
    public void getGuessInfo(String MID, final boolean isRefreshList) {

        HttpParams params = Token.createRequest();
     params.put("route", ConstantValue.GUESS\_INFO\_URL);
     params.put("MID", MID);
     NXHttpManager.<GuessInfoBean>post(ConstantValue.ServerURL + "/index.php").tag(this).params(params).execute(new NTCommonCallBack<GuessInfoBean>(guessView, false) {

            @Override
     protected void onNiuTanSuccess(Response<GuessInfoBean> nxResponse) {
                GuessInfoBean response = nxResponse.body();

     if (response.getData() == null) {
                    guessView.showEmptyData();

     guessView.showToastMessage(response.getResMsg());
     return;
     }

                guessView.setData(response,isRefreshList);
     }

            @Override
     public void onError(Response<GuessInfoBean> response) {

                guessView.showEmptyData();
     super.onError(response);
     }
        });
    }

6.取消网络请求：

    NXHttpManager.getInstance().cancelTag(this);