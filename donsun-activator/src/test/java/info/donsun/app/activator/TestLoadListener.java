package info.donsun.app.activator;

import info.donsun.activator.LoadEvent;
import info.donsun.activator.LoadListener;

public class TestLoadListener implements LoadListener{

    @Override
    public void onLoad(LoadEvent e) {
        System.out.println("----------------" + e.getMessage());
    }

}
