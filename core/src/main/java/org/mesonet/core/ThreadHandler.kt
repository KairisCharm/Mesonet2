package org.mesonet.core

import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import javax.inject.Inject
import javax.inject.Singleton


@PerActivity
class ThreadHandler @Inject constructor()
{
    private val mThreads = HashMap<String, Pair<HandlerThread, Boolean>>()


    fun Run(inThreadBaseName: String, inRunnable: Runnable, inCallback: Runnable? = null)
    {
        var indexToTry = 0
        while(true)
        {
            val nameToTry = inThreadBaseName + indexToTry

            if(!mThreads.containsKey(nameToTry))
                mThreads.put(nameToTry, Pair(HandlerThread(nameToTry), false))

            if(mThreads[nameToTry] != null && (mThreads[nameToTry]?.second == null || !mThreads[nameToTry]?.second!!))
            {
                RunOnSpecificThread(nameToTry, inRunnable, inCallback)
                break
            }


            indexToTry++
        }
    }


    fun RunOnSpecificThread(inThreadName: String, inRunnable: Runnable, inCallback: Runnable? = null)
    {
        synchronized(this)
        {
            val callingLooper = Looper.myLooper()

            if (!mThreads.containsKey(inThreadName))
                mThreads.put(inThreadName, Pair(HandlerThread(inThreadName), false))

            mThreads[inThreadName] = mThreads[inThreadName]?.copy(second = true)!!

            if (mThreads[inThreadName] != null && mThreads[inThreadName]?.first != null && mThreads[inThreadName]?.first?.isAlive != null && !mThreads[inThreadName]?.first?.isAlive!!)
                mThreads[inThreadName]?.first?.start()

            Handler(mThreads[inThreadName]?.first?.looper).post({
                inRunnable.run()

                if (inCallback != null) {
                    Handler(callingLooper).post({
                        inCallback.run()
                        synchronized(this@ThreadHandler)
                        {
                            mThreads[inThreadName] = mThreads[inThreadName]?.copy(second = false)!!
                        }
                    })
                } else
                    synchronized(this@ThreadHandler)
                    {
                        mThreads[inThreadName] = mThreads[inThreadName]?.copy(second = false)!!
                    }
            })
        }
    }



    fun CloseThreads()
    {
        for (threadPair in mThreads.values)
            threadPair.first.quit()
    }
}