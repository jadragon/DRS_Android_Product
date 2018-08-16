package library;

import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;

public class ScrollSliderMenu {
    //屏幕宽度值。
    private int screenWidth;

    // menu最多可以滑动到的左边缘。值由menu布局的宽度来定，marginLeft到达此值之后，不能再减少。
    private int leftEdge;

    // menu最多可以滑动到的右边缘。值恒为0，即marginLeft到达0之后，不能增加。
    private int rightEdge = 0;

    // menu的布局。
    private View menu;

    //menu布局的参数，通过此参数来更改leftMargin的值。
    private LinearLayout.LayoutParams menuParams;

    // menu布局的参数，通过此参数来更改leftMargin的值。
    private LinearLayout.LayoutParams contentParams;

    //menu当前是显示还是隐藏。只有完全显示或隐藏menu时才会更改此值，滑动过程中此值无效。
    private boolean isMenuVisible;

    public boolean isMenuVisible() {
        return isMenuVisible;
    }

    public void setMenuVisible(boolean menuVisible) {
        isMenuVisible = menuVisible;
    }

    /**
     * 将屏幕滚动到menu界面，滚动速度设定为30.
     */
    public void scrollToMenu() {
        new ScrollTask().execute(10);
    }

    /**
     * 将屏幕滚动到content界面，滚动速度设定为-30.
     */
    public void scrollToContent() {
        new ScrollTask().execute(-10);
    }

    public ScrollSliderMenu(View content, View menu, int screenWidth) {
        this.screenWidth = screenWidth;
        this.menuParams = (LinearLayout.LayoutParams) menu.getLayoutParams();
        this.contentParams = (LinearLayout.LayoutParams) content.getLayoutParams();
        // 左边缘的值赋值为menu宽度的负数
        this.leftEdge = -menuParams.width;
        // menu的leftMargin设置为左边缘的值，这样初始化时menu就变为不可见
        menuParams.leftMargin = leftEdge;
        // 将content的宽度设置为屏幕宽度
        contentParams.width = screenWidth;
        this.menu = menu;

    }

    public class ScrollTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected Integer doInBackground(Integer... speed) {
            int leftMargin = menuParams.leftMargin;
            // 根据传入的速度来滚动界面，当滚动到达左边界或右边界时，跳出循环。
            while (true) {
                leftMargin = leftMargin + speed[0];
                if (leftMargin > rightEdge) {
                    leftMargin = rightEdge;
                    break;
                }
                if (leftMargin < leftEdge) {
                    leftMargin = leftEdge;
                    break;
                }
                publishProgress(leftMargin);
                // 为了要有滚动效果产生，每次循环使线程睡眠20毫秒，这样肉眼才能够看到滚动动画。
                sleep(20);
            }
            return leftMargin;
        }

        @Override
        protected void onProgressUpdate(Integer... leftMargin) {
            menuParams.leftMargin = leftMargin[0];
            menu.setLayoutParams(menuParams);
            contentParams.width = screenWidth - (menuParams.width + leftMargin[0]);
        }

        @Override
        protected void onPostExecute(Integer leftMargin) {
            menuParams.leftMargin = leftMargin;
            menu.setLayoutParams(menuParams);
            contentParams.width = screenWidth - (menuParams.width + leftMargin);
        }

    }

    /**
     * 使当前线程睡眠指定的毫秒数。
     *
     * @param millis 指定当前线程睡眠多久，以毫秒为单位
     */
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}