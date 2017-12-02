package com.istea.agustinlema.todoapp.async;

import android.os.AsyncTask;

/**
 * Created by KyA on 2/12/2017.
 */

public class AsyncTaskRunner extends AsyncTask<Command,Integer,Void> {

    @Override
    protected Void doInBackground(Command... commands) {
        if (commands.length==1)
            commands[0].execute();
        return null;
    }
}
