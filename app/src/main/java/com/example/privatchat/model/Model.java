package com.example.privatchat.model;
import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import com.example.privatchat.mainclass.Dialog;
import com.example.privatchat.mainclass.Message;
import com.example.privatchat.mainclass.User;
import com.example.privatchat.model.bd.AppDatabas;
import com.example.privatchat.model.internet.API;
import com.example.privatchat.model.internet.Client;
import com.example.privatchat.model.singlton.App;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class Model {
    private API api  = Client.getInstance().getApi();
    private AppDatabas bd  = App.getInstance().getDatabase();
    private Executor mExecutor = Executors.newSingleThreadExecutor();



    public void adduser(User user){
        mExecutor.execute(()-> bd.userDao().insert(user));
    }

    public  MutableLiveData<User> getUser(String ldap) {
        MutableLiveData<User> Mainuser = new MutableLiveData<>();
        api.hello(ldap)
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("Completed!!!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("Error!!!");
                        Mainuser.postValue(new User("0","Denis","","dn280790bds"));
                    }

                    @Override
                    public void onNext(User user) {
                        Mainuser.postValue(new User("0","Denis","","dn280790bds"));
                    }
                });
        return Mainuser;
    }


    @SuppressLint("CheckResult")
    public MutableLiveData<List<Dialog>> getDialogsUser(User userLiveDate) {
        MutableLiveData<List<Dialog>>MLDdialogsAdapter = new MutableLiveData<>();

        mExecutor.execute(()->{
            List<Dialog> dialogs = bd.dialogDao().getUserDialog(userLiveDate);
            if (dialogs.size()!=0){
                MLDdialogsAdapter.postValue(dialogs);
            }else {
                api.getDialog(userLiveDate)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Dialog>>() {

                            @Override
                            public void onCompleted() {

                            }
                            @Override
                            public void onError(Throwable e) {
                                System.out.println("Error!!!");
                                ArrayList<User> users  = new ArrayList<>();
                                List<Dialog> dialogs = new ArrayList<>();

                                User Denis = new User("0","Denis","","dn280790bds");
                                User Andru = new User("1","Andru","","xxxxxxxxxxx");
                                users.add(Denis);
                                users.add(Andru);
                                Dialog dialog = new Dialog("0","Den and Andru","",users,new Message("4","Hello Andru",Andru,new Date()),1);

                                users  = new ArrayList<>();
                                users.add(Andru);
                                users.add(Denis);
                                Dialog dialog2 = new Dialog("1","Andru to Den","",users,new Message("4","Hello Den",Andru,new Date()),1);

                                dialogs.add(dialog);
                                dialogs.add(dialog2);


                                mExecutor.execute(()->{
                                    for (Dialog d:dialogs) {
                                        bd.dialogDao().insert(d);
                                    }
                                });

                                MLDdialogsAdapter.postValue(dialogs);
                            }

                            @Override
                            public void onNext(List<Dialog> dialogs) {
                                System.out.println("All OK!!!");


                                ArrayList<User> users  = new ArrayList<>();
                                User Denis = new User("0","Denis","","dn280790bds");
                                User Andru = new User("1","Andru","","xxxxxxxxxxx");
                                users.add(Denis);
                                users.add(Andru);
                                Dialog dialog = new Dialog("0","Den and Andru","",users,new Message("4","Hello Andru",Andru,new Date()),1);

                                users  = new ArrayList<>();
                                users.add(Andru);
                                users.add(Denis);
                                Dialog dialog2 = new Dialog("1","Andru to Den","",users,new Message("4","Hello Den",Andru,new Date()),1);


                                dialogs.add(dialog);
                                dialogs.add(dialog2);

                                mExecutor.execute(()->{
                                    for (Dialog d:dialogs) {
                                        bd.dialogDao().insert(d);
                                    }
                                });
                                MLDdialogsAdapter.postValue(dialogs);
                            }
                        });

            }
        });
        return MLDdialogsAdapter;
    }


    @SuppressLint("CheckResult")
    public MutableLiveData<ArrayList<Message>> getMessageDialog(User user) {
        MutableLiveData<ArrayList<Message>> arrayListMutableLiveData  =  new MutableLiveData<>();
        mExecutor.execute(()->{
           List<Message> messages =  bd.messageDao().getUserMessage(user);
           if(messages.size()!=0){
               arrayListMutableLiveData.postValue((ArrayList<Message>) messages);
           }else{
               api.getMessage(user)
                       .subscribe(new Observer<List<Message>>() {

                           @Override
                           public void onCompleted() {

                           }

                           @Override
                           public void onError(Throwable e) {
                               System.out.println("Error!!!");
                               ArrayList<Message> messages = new ArrayList<>();
                               Message messagefromDenis = new Message("1","Hello Andru",new User("0","Denis","","dn280790bds"),new Date());
                               Message messagefromAndru = new Message("2","Hello Den",new User("1","Andru","","dn111111fai"),new Date());
                               messages.add(messagefromDenis);
                               messages.add(messagefromAndru);
                               arrayListMutableLiveData.postValue((ArrayList<Message>) messages);
                           }

                           @Override
                           public void onNext(List<Message> messages) {
                               System.out.println("All OK!!!");
                               Message messagefromDenis = new Message("1","Hello Andru",new User("0","Denis","","dn280790bds"),new Date());
                               Message messagefromAndru = new Message("2","Hello Den",new User("1","Andru","","dn111111fai"),new Date());
                               messages.add(messagefromDenis);
                               messages.add(messagefromAndru);

                               arrayListMutableLiveData.postValue((ArrayList<Message>) messages);
                           }
                       });
           }
        });

        return arrayListMutableLiveData;
    }




}
