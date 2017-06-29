package fylder.upload.demo.app;

import android.app.Application;

import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * Created by fylder on 2017/6/28.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        HermesEventBus.getDefault().init(this);

    }
}
