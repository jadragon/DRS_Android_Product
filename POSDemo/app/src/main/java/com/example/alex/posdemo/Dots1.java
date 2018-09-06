package com.example.alex.posdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import library.Component.DotsSurfaceView;

public class Dots1 extends Activity {
    // USER INTERFACE
    static int screen_selected=0;
    private static final int MENU_SIMPLE_UI = 1;     // SIMPLE UI
    private Button button1;
    private static final int MENU_DOTS = 2;          // RANDOM CIRCLES "dots"
    DotsSurfaceView dots_screen_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        set_dots();
    }
/*
    @Override
    public void onPause(){
        if (screen_selected==2){
            dots_screen_view.surfaceDestroyed(dots_screen_view.getHolder());
        }
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        switch (screen_selected) {
            case 0:
                screen_selected=MENU_SIMPLE_UI;
            case MENU_SIMPLE_UI:
                screen_selected=1;
                set_simple_UI();
                return;
            case MENU_DOTS:
                screen_selected=2;
                set_dots();
                return;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Invoked during init to give the Activity a chance to set up its Menu.
     *
     * @param menu the Menu to which entries may be added
     * @return true
     */
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_SIMPLE_UI, 0, R.string.menu_simple_ui);
        menu.add(0, MENU_DOTS, 0, R.string.menu_dots);
        return true;
    }
*/
    /**
     * Invoked when the user selects an item from the Menu.
     *
     * @param item the Menu entry which was selected
     * @return true if the Menu item was legit (and we consumed it), false
     *     otherwise
     */
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (screen_selected) {
            case MENU_SIMPLE_UI:
                break;
            case MENU_DOTS:
                dots_screen_view.surfaceDestroyed(dots_screen_view.getHolder());
                break;
        }
        switch (item.getItemId()) {
            case MENU_SIMPLE_UI:
                screen_selected=1;
                set_simple_UI();
                return true;
            case MENU_DOTS:
                screen_selected=2;
                set_dots();
                return true;
        }
        return false;
    }

    void set_simple_UI(){
        setContentView(R.layout.activity_main);
        button1=(Button)this.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //~Activity;
            }
        });
    }
*/
    void set_dots(){
        dots_screen_view=new DotsSurfaceView(this);
        setContentView(dots_screen_view);
    }
}
