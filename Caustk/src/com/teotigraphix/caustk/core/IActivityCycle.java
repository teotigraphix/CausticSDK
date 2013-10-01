
package com.teotigraphix.caustk.core;

public interface IActivityCycle {

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onRestart();

    void dispose();
}
