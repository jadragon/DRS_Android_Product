package com.example.alex.ordersystemdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ToolbarAcitvity extends AppCompatActivity {
    boolean menuVisible = false;
    boolean navigationVisible = false;
    protected void initToolbar(String toolbarTitle, boolean navigationVisible,boolean menuVisible) {
        this.menuVisible = menuVisible;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        ((TextView) findViewById(R.id.toolbar_title)).setText(toolbarTitle);
        if(navigationVisible) {
            toolbar.setNavigationIcon(R.drawable.ic_action_name);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menuVisible) {
            getMenuInflater().inflate(R.menu.searchmenu, menu);
            MenuItem menuItem = menu.findItem(R.id.menu_send);
            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    startActivity(new Intent(ToolbarAcitvity.this, StudentAcitvity.class));
                    return true;
                }
            });
        }
        /*
          menuItem = menu.findItem(R.id.menu_search);
        //通过MenuItem得到SearchView
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("輸入需要搜尋的店家");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                ((InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ToolbarAcitvity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        */
        return super.onCreateOptionsMenu(menu);

    }

}
