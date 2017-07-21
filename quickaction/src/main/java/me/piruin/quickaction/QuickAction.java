/*
 *  Copyright 2016 Piruin Panichphol
 *  Copyright 2011 Lorensius W. L. T
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at

 *     http://www.apache.org/licenses/LICENSE-2.0

 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package me.piruin.quickaction;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static me.piruin.quickaction.ArrowDrawable.ARROW_DOWN;
import static me.piruin.quickaction.ArrowDrawable.ARROW_UP;

/**
 * QuickAction popup, shows action list as icon and text in Tooltip popup. Currently supports
 * vertical and horizontal layout.
 */
public class QuickAction extends PopupWindows implements OnDismissListener {

  public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
  public static final int VERTICAL = LinearLayout.VERTICAL;

  private static int defaultColor = Color.WHITE;
  private static int defaultTextColor = Color.BLACK;

  public static void setDefaultTextColor(int defaultTextColor) {
    QuickAction.defaultTextColor = defaultTextColor;
  }

  public static void setDefaultColor(int defaultColor) {
    QuickAction.defaultColor = defaultColor;
  }

  private WindowManager windowManager;
  private View rootView;
  private View arrowUp;
  private View arrowDown;
  private LayoutInflater inflater;
  private Resources resource;
  private LinearLayout track;
  private ViewGroup scroller;

  private OnActionItemClickListener mItemClickListener;
  private OnDismissListener dismissListener;
  private List<ActionItem> actionItems = new ArrayList<>();
  private Animation animation = Animation.AUTO;
  private boolean didAction;
  private int orientation;
  private int rootWidth = 0;
  private int textColor = defaultTextColor;

  private final int stroke;
  private final int shadowColor;

  /**
   * Constructor for default vertical layout
   *
   * @param context Context
   */
  public QuickAction(@NonNull Context context) {
    this(context, VERTICAL);
  }

  /**
   * Constructor allowing orientation override QuickAction.HORIZONTAL or QuickAction.VERTICAL
   *
   * @param context Context
   * @param orientation Layout orientation, can be vartical or horizontal
   */
  public QuickAction(@NonNull Context context, int orientation) {
    super(context);
    this.orientation = orientation;
    inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
    resource = context.getResources();

    stroke = resource.getDimensionPixelSize(R.dimen.stroke);
    shadowColor = resource.getColor(R.color.stroke);

    setRootView(orientation == VERTICAL ? R.layout.popup : R.layout.popup_horizontal);
  }

  private void setRootView(@LayoutRes int id) {
    rootView = inflater.inflate(id, null);
    rootView.setLayoutParams(new LayoutParams(WRAP_CONTENT, WRAP_CONTENT));

    track = (LinearLayout)rootView.findViewById(R.id.tracks);
    track.setOrientation(orientation);

    arrowDown = rootView.findViewById(R.id.arrow_down);
    arrowUp = rootView.findViewById(R.id.arrow_up);

    scroller = (ViewGroup)rootView.findViewById(R.id.scroller);

    setContentView(rootView);
    setColor(defaultColor);
  }

  /**
   * Set color of popup
   *
   * @param popupColor Color to fill popup
   * @see Color
   */
  public void setColor(@ColorInt int popupColor) {
    GradientDrawable drawable = new GradientDrawable();
    drawable.setColor(popupColor);
    drawable.setStroke(stroke, shadowColor);
    drawable.setCornerRadius(resource.getDimension(R.dimen.popup_corner));

    arrowDown.setBackground(new ArrowDrawable(ARROW_DOWN, popupColor, stroke, shadowColor));
    arrowUp.setBackground(new ArrowDrawable(ARROW_UP, popupColor, stroke, shadowColor));
    scroller.setBackground(drawable);
  }

  /**
   * Set color of popup by color define in xml resource
   *
   * @param popupColor Color resource id to fill popup
   */
  public void setColorRes(@ColorRes int popupColor) {
    setColor(resource.getColor(popupColor));
  }

  /**
   * Set color for text of each action item. MUST call this before add action item, sorry I'm just
   * lazy.
   *
   * @param textColorRes Color resource id to use
   */
  public void setTextColorRes(@ColorRes int textColorRes) {
    setTextColor(resource.getColor(textColorRes));
  }

  /**
   * Set color for text of each action item. MUST call this before add action item, sorry I'm just
   * lazy.
   *
   * @param textColor Color to use
   */
  public void setTextColor(@ColorInt int textColor) {
    this.textColor = textColor;
  }

  /**
   * Set animation style
   *
   * @param mAnimStyle animation style, default is set to ANIM_AUTO
   */
  public void setAnimStyle(Animation mAnimStyle) {
    this.animation = mAnimStyle;
  }

  /**
   * Set listener for action item clicked.
   *
   * @param listener Listener
   */
  public void setOnActionItemClickListener(OnActionItemClickListener listener) {
    mItemClickListener = listener;
  }

  /**
   * Add action item
   *
   * @param action {@link ActionItem}
   */
  public void addActionItem(final ActionItem action) {
    int position = actionItems.size();
    actionItems.add(action);
    addActionView(position, createViewFrom(action));
  }

 private void addActionView(int position, View actionView) {
        if (orientation == HORIZONTAL && position != 0) {
            position *= 2;
            int separatorPos = position - 1;
            View separator = new View(getContext());
            separator.setBackgroundColor(Color.argb(32, 0, 0, 0));
            int width = resource.getDimensionPixelOffset(R.dimen.separator_width);
            track.addView(separator, separatorPos, new LayoutParams(width, MATCH_PARENT));
        } else if (orientation == VERTICAL && position != 0) {
            position *= 2;
            int separatorPos = position - 1;
            View separator = new View(getContext());
            separator.setBackgroundColor(Color.argb(32, 0, 0, 0));
            int width = resource.getDimensionPixelOffset(R.dimen.separator_width);
            track.addView(separator, separatorPos, new LayoutParams(MATCH_PARENT, width));
        }
        track.addView(actionView, position);
    }


  /**
   * Add action item at specify position
   *
   * @param position to add ActionItem (zero-base)
   * @param action {@link ActionItem}
   */
  public void addActionItem(int position, final ActionItem action) {
    actionItems.add(position, action);
    addActionView(position, createViewFrom(action));
  }

  @NonNull private View createViewFrom(final ActionItem action) {
    View actionView;
    if (action.haveTitle()) {
      TextView textView = (TextView) inflater.inflate(R.layout.action_item, track, false);
      textView.setTextColor(textColor);
      textView.setText(String.format(" %s ", action.getTitle()));
      if (action.haveIcon()) {
        int iconSize = resource.getDimensionPixelOffset(R.dimen.icon_size);
        Drawable icon = action.getIconDrawable(getContext());
        icon.setBounds(0, 0, iconSize, iconSize);
        if (orientation == HORIZONTAL) {
          textView.setCompoundDrawablesRelative(null, icon, null, null);
        } else {
          textView.setCompoundDrawablesRelative(icon, null, null, null);
        }
      }
      actionView = textView;
    } else {
      ImageView imageView = (ImageView) inflater.inflate(R.layout.image_action_item, track, false);
      imageView.setId(action.getActionId());
      imageView.setImageDrawable(action.getIconDrawable(getContext()));
      actionView = imageView;
    }

    actionView.setId(action.getActionId());
    actionView.setOnClickListener(new OnClickListener() {
      @Override public void onClick(View v) {
        action.setSelected(true);
        if (mItemClickListener != null) {
          mItemClickListener.onItemClick(action);
        }
        if (!action.isSticky()) {
          didAction = true;
          dismiss();
        }
      }
    });
    actionView.setFocusable(true);
    actionView.setClickable(true);
    return actionView;
  }

  /**
   * Get action item at an index
   *
   * @param index Index of item (position from callback)
   * @return Action Item at the position
   */
  public ActionItem getActionItem(int index) {
    return actionItems.get(index);
  }

  /**
   * Get action item by Action's ID
   *
   * @param actionId Id of item
   * @return Action Item with same id
   */
  @Nullable
  public ActionItem getActionItemById(int actionId){
    for (ActionItem action : actionItems){
      if (action.getActionId() == actionId)
        return action;
    }
    return null;
  }

  /**
   * remove action item
   *
   * @param actionId Id of action to remove
   * @return removed item
   */
  public ActionItem remove(int actionId) {
    return remove(getActionItemById(actionId));
  }

  /**
   * remove action item
   *
   * @param action action to remove
   * @return removed item
   */
  public ActionItem remove(ActionItem action) {
    int index = actionItems.indexOf(action);
    if (index == -1) throw new RuntimeException("Now found action");

    if (orientation == VERTICAL) {
      track.removeViewAt(index);
    } else {
      int viewPos = index * 2;
      track.removeViewAt(viewPos);
      track.removeViewAt(viewPos - 1); //remove separator
    }
    return actionItems.remove(index);
  }

  /**
   * Show quickaction popup. Popup is automatically positioned, on top or bottom of anchor view.
   *
   * @param activity contain view to be anchor
   * @param anchorId id of view to use as anchor of QuickAction's popup
   */
  public void show(@NonNull Activity activity, @IdRes int anchorId) {
    show(activity.findViewById(anchorId));
  }

  /**
   * Show quickaction popup. Popup is automatically positioned, on top or bottom of anchor view.
   *
   * @param anchor view to use as anchor of QuickAction's popup
   */
  public void show(View anchor) {
    if (getContext() == null)
      throw new IllegalStateException("Why context is null? It shouldn't be.");

    preShow();

    int xPos, yPos, arrowPos;

    didAction = false;

    int[] location = new int[2];
    anchor.getLocationOnScreen(location);
    Rect anchorRect = new Rect(location[0], location[1], location[0]+anchor.getWidth(),
                               location[1]+anchor.getHeight());

    rootView.measure(WRAP_CONTENT, WRAP_CONTENT);

    int rootHeight = rootView.getMeasuredHeight();

    if (rootWidth == 0) {
      rootWidth = rootView.getMeasuredWidth();
    }

    DisplayMetrics displaymetrics = new DisplayMetrics();
    windowManager.getDefaultDisplay().getMetrics(displaymetrics);
    int screenWidth = displaymetrics.widthPixels;
    int screenHeight = displaymetrics.heightPixels;

    // automatically get X coord of popup (top left)
    if ((anchorRect.left+rootWidth) > screenWidth) {
      xPos = anchorRect.left-(rootWidth-anchor.getWidth());
      xPos = (xPos < 0) ? 0 : xPos;

      arrowPos = anchorRect.centerX()-xPos;
    } else {
      if (anchor.getWidth() > rootWidth) {
        xPos = anchorRect.centerX()-(rootWidth/2);
      } else {
        xPos = anchorRect.left;
      }

      arrowPos = anchorRect.centerX()-xPos;
    }

    int dyTop = anchorRect.top;
    int dyBottom = screenHeight-anchorRect.bottom;

    boolean onTop = dyTop > dyBottom;

    if (onTop) {
      if (rootHeight > dyTop) {
        yPos = 15;
        LayoutParams l = scroller.getLayoutParams();
        l.height = dyTop-anchor.getHeight();
      } else {
        yPos = anchorRect.top-rootHeight;
      }
    } else {
      yPos = anchorRect.bottom;

      if (rootHeight > dyBottom) {
        LayoutParams l = scroller.getLayoutParams();
        l.height = dyBottom;
      }
    }

    showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), arrowPos);

    setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

    mWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
  }

  /**
   * Set animation style
   *
   * @param screenWidth screen width
   * @param requestedX distance from left edge
   * @param onTop flag to indicate where the popup should be displayed. Set TRUE if displayed on top
   * of anchor view and vice versa
   */
  private void setAnimationStyle(
    int screenWidth, int requestedX, boolean onTop)
  {
    int arrowPos = requestedX-arrowUp.getMeasuredWidth()/2;
    switch (animation) {
      case AUTO:
        if (arrowPos <= screenWidth/4)
          mWindow.setAnimationStyle(Animation.GROW_FROM_LEFT.get(onTop));
        else if (arrowPos > screenWidth/4 && arrowPos < 3*(screenWidth/4))
          mWindow.setAnimationStyle(Animation.GROW_FROM_CENTER.get(onTop));
        else
          mWindow.setAnimationStyle(Animation.GROW_FROM_RIGHT.get(onTop));
        break;
      default:
        mWindow.setAnimationStyle(animation.get(onTop));
    }
  }

  /**
   * Show arrow
   *
   * @param whichArrow arrow type resource id
   * @param requestedX distance from left screen
   */
  private void showArrow(int whichArrow, int requestedX) {
    final View showArrow = (whichArrow == R.id.arrow_up) ? arrowUp : arrowDown;
    final View hideArrow = (whichArrow == R.id.arrow_up) ? arrowDown : arrowUp;

    final int arrowWidth = arrowUp.getMeasuredWidth();

    showArrow.setVisibility(View.VISIBLE);

    ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams)showArrow.getLayoutParams();

    param.leftMargin = requestedX-arrowWidth/2;

    hideArrow.setVisibility(View.GONE);
  }

  /**
   * Set listener for window dismissed. This listener will only be fired if the quicakction dialog
   * is dismissed by clicking outside the dialog or clicking on sticky item.
   *
   * @param listener will fire when QuickaAtion dismiss
   */
  public void setOnDismissListener(QuickAction.OnDismissListener listener) {
    setOnDismissListener(this);

    dismissListener = listener;
  }

  @Override public void onDismiss() {
    if (!didAction && dismissListener != null) {
      dismissListener.onDismiss();
    }
  }

  public enum Animation {
    GROW_FROM_LEFT {
      @Override int get(boolean onTop) {
        return (onTop) ? R.style.Animation_PopUpMenu_Left : R.style.Animation_PopDownMenu_Left;
      }
    },
    GROW_FROM_RIGHT {
      @Override int get(boolean onTop) {
        return (onTop) ? R.style.Animation_PopUpMenu_Right : R.style.Animation_PopDownMenu_Right;
      }
    },
    GROW_FROM_CENTER {
      @Override int get(boolean onTop) {
        return (onTop) ? R.style.Animation_PopUpMenu_Center : R.style.Animation_PopDownMenu_Center;
      }
    },
    REFLECT {
      @Override int get(boolean onTop) {
        return (onTop) ? R.style.Animation_PopUpMenu_Reflect
                       : R.style.Animation_PopDownMenu_Reflect;
      }
    },
    AUTO {
      @Override int get(boolean onTop) {
        throw new UnsupportedOperationException("Can't use this");
      }
    };

    @StyleRes abstract int get(boolean onTop);
  }

  /**
   * Listener for item click
   */
  public interface OnActionItemClickListener {
    void onItemClick(ActionItem item);
  }

  /**
   * Listener for window dismiss
   */
  public interface OnDismissListener {
    void onDismiss();
  }
}
