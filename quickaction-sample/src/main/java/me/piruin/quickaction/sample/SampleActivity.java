/*
 *  Copyright 2016 Piruin Panichphol
 *  Copyright 2011 Lorensius W. L. T
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.piruin.quickaction.sample;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;
import me.piruin.quickaction.QuickIntentAction;

public class SampleActivity extends AppCompatActivity {

  private static final int ID_UP = 1;
  private static final int ID_DOWN = 2;
  private static final int ID_SEARCH = 3;
  private static final int ID_INFO = 4;
  private static final int ID_ERASE = 5;
  private static final int ID_OK = 6;

  private QuickAction quickAction;
  private QuickAction quickIntent;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sample);
    ButterKnife.bind(this);

    //Config default color
    QuickAction.setDefaultColor(ResourcesCompat.getColor(getResources(), R.color.teal, null));
    QuickAction.setDefaultTextColor(Color.BLACK);

    ActionItem nextItem = new ActionItem(ID_DOWN, "Next", R.drawable.ic_arrow_downward);
    ActionItem prevItem = new ActionItem(ID_UP, "Prev", R.drawable.ic_arrow_upward);
    ActionItem searchItem = new ActionItem(ID_SEARCH, "Find", R.drawable.ic_search);
    ActionItem infoItem = new ActionItem(ID_INFO, "Info", R.drawable.ic_info);
    ActionItem eraseItem = new ActionItem(ID_ERASE, "Clear", R.drawable.ic_clear);
    ActionItem okItem = new ActionItem(ID_OK, "OK", R.drawable.ic_ok);

    //use setSticky(true) to disable QuickAction dialog being dismissed after an item is clicked
    prevItem.setSticky(true);
    nextItem.setSticky(true);

    //create QuickAction. Use QuickAction.VERTICAL or QuickAction.HORIZONTAL param to define layout
    //orientation
    quickAction = new QuickAction(this, QuickAction.VERTICAL);
    quickAction.setColorRes(R.color.pink);
    quickAction.setTextColorRes(R.color.white);

    //set vertical divider with color
    quickAction.setVerticalDivider(ContextCompat.getColor(this,R.color.white));

    //set only vertical divider
    quickAction.setVerticalDivider();

    //add action items into QuickAction
    quickAction.addActionItem(nextItem);
    quickAction.addActionItem(prevItem);
    quickAction.setTextColor(Color.YELLOW);
    quickAction.addActionItem(searchItem);
    quickAction.addActionItem(infoItem);
    quickAction.addActionItem(eraseItem);
    quickAction.addActionItem(okItem);

    //Set listener for action item clicked
    quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
      @Override public void onItemClick(ActionItem item) {
        //here we can filter which action item was clicked with pos or actionId parameter
        String title = item.getTitle();
        Toast.makeText(SampleActivity.this, title+" selected", Toast.LENGTH_SHORT).show();
        if (!item.isSticky()) quickAction.remove(item);
      }
    });

    //set listnener for on dismiss event, this listener will be called only if QuickAction dialog
    // was dismissed
    //by clicking the area outside the dialog.
    quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
      @Override public void onDismiss() {
        Toast.makeText(SampleActivity.this, "Dismissed", Toast.LENGTH_SHORT).show();
      }
    });

    //Quick and Easy intent selector in tooltip styles
    Intent sendIntent = new Intent();
    sendIntent.setAction(Intent.ACTION_SEND);
    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
    sendIntent.setType("text/plain");

    quickIntent = new QuickIntentAction(this)
      .setActivityIntent(sendIntent)
      .create();
    quickIntent.setAnimStyle(QuickAction.Animation.REFLECT);
  }

  @OnClick( { R.id.button1, R.id.button2 }) void onShow(View view) {
    quickAction.show(view);
  }

  @OnClick(R.id.intent) void onShowQuickIntent(View view) {
    quickIntent.show(view);
  }

  boolean red = false;

  @OnClick(R.id.replace) void onReplaceActionItem(View view) {
    quickAction.remove(ID_ERASE);
    quickAction.addActionItem(4,
        new ActionItem(ID_ERASE, "Erase", red ? R.drawable.ic_clear : R.drawable.ic_clear_red));
    red = !red;
    Toast.makeText(SampleActivity.this, "Replaced", Toast.LENGTH_SHORT).show();
  }

  boolean removed = false;

  @OnClick(R.id.remove) void onRemoveItem(View view) {
    if (removed) return;

    quickAction.remove(quickAction.getActionItemById(ID_OK));
    removed = true;
    Toast.makeText(SampleActivity.this, "Removed OK Action!", Toast.LENGTH_SHORT).show();
  }
}
