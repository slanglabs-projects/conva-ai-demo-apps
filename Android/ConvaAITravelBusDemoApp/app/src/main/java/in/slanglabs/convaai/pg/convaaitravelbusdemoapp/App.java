package in.slanglabs.convaai.pg.convaaitravelbusdemoapp;

import android.app.Application;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.db.AppDatabase;
import in.slanglabs.convaai.pg.convaaitravelbusdemoapp.convaai.ConvaAIInterface;

public class App extends Application {
    private AppExecutors mAppExecutors;
    private ConvaAIInterface mConvaAIInterface;
    @Override
    public void onCreate() {
        super.onCreate();
        mAppExecutors = new AppExecutors();
        mConvaAIInterface = new ConvaAIInterface(this);
        Repository.getInstance(AppDatabase.getInstance(this,mAppExecutors),mAppExecutors,mConvaAIInterface);
    }

    public Repository getRepository() {
        return Repository.getInstance(
                AppDatabase.getInstance(this,
                        mAppExecutors),mAppExecutors,mConvaAIInterface);
    }
}
