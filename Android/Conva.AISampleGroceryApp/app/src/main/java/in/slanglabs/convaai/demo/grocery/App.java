package in.slanglabs.convaai.demo.grocery;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import in.slanglabs.convaai.demo.grocery.convaai.ConvaAIInterface;
import in.slanglabs.convaai.demo.grocery.db.AppDatabase;

public class App extends Application {

    private Repository mRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        AppExecutors mAppExecutors = new AppExecutors();
        ConvaAIInterface mConvaAIInterface = new ConvaAIInterface(this);
        SharedPreferences mPrefs = this.getSharedPreferences("slang_grocery",
                Context.MODE_PRIVATE);
        mRepository = new Repository(
                AppDatabase.getInstance(this,
                        mAppExecutors), mAppExecutors, mConvaAIInterface, mPrefs);
    }

    public Repository getRepository() {
        return mRepository;
    }

}
