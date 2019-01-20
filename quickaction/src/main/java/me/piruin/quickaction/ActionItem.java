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

package me.piruin.quickaction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;

/**
 * Action item, displayed as menu with icon and text.
 */
public class ActionItem {
  private Bitmap thumb;
  private String title;
  private int icon = -1;
  private Drawable iconDrawable;
  private int actionId = -1;
  private boolean selected;
  private boolean sticky;

  /**
   * Create Action Item without Icon
   *
   * @param actionId Action id of the item
   * @param title Text to show for the item
   */
  public ActionItem(int actionId, String title) {
    this(actionId, title, -1);
  }

  /**
   * Create Action Item with all attribute
   *
   * @param actionId Action id for case statements
   * @param title Title
   * @param icon Icon to use
   */
  public ActionItem(int actionId, String title, @DrawableRes int icon) {
    this.actionId = actionId;
    this.title = title;
    this.icon = icon;
  }

  /**
   * Create Action Item  with only Icon
   *
   * @param icon {@link Drawable} action icon
   */
  public ActionItem(@DrawableRes int icon) {
    this(-1, null, icon);
  }

  /**
   * Create Action Item with only Icon
   *
   * @param actionId Action ID of item
   * @param icon {@link Drawable} action icon
   */
  public ActionItem(int actionId, @DrawableRes int icon) {
    this(actionId, null, icon);
  }

  /**
   * Set action title
   *
   * @param title action title
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Get action title
   *
   * @return action title
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * @return true if title have been set
   */
  public boolean haveTitle() {
    return !TextUtils.isEmpty(title);
  }

  /**
   * Set action icon
   *
   * @param icon {@link Drawable} action icon
   */
  public void setIcon(@DrawableRes int icon) {
    this.icon = icon;
  }

  /**
   * Get action icon
   *
   * @return {@link Drawable} action icon
   */
  @DrawableRes public int getIcon() {
    return this.icon;
  }

  public boolean haveIcon() {
    return icon > 0 || iconDrawable != null;
  }

  /**
   * Set action id
   *
   * @param actionId Action id for this action
   */
  public void setActionId(int actionId) {
    this.actionId = actionId;
  }

  /**
   * @return Our action id
   */
  public int getActionId() {
    return actionId;
  }

  /**
   * Set sticky status of button
   *
   * @param sticky true for sticky, pop up sends event but does not disappear
   */
  public void setSticky(boolean sticky) {
    this.sticky = sticky;
  }

  /**
   * @return true if button is sticky, menu stays visible after press
   */
  public boolean isSticky() {
    return sticky;
  }

  /**
   * Set selected flag;
   *
   * @param selected Flag to indicate the item is selected
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  /**
   * Check if item is selected
   *
   * @return true or false
   */
  public boolean isSelected() {
    return this.selected;
  }

  /**
   * Set thumb
   *
   * @param thumb Thumb image
   */
  public void setThumb(Bitmap thumb) {
    this.thumb = thumb;
  }

  /**
   * Get thumb image
   *
   * @return Thumb image
   */
  public Bitmap getThumb() {
    return this.thumb;
  }

  public Drawable getIconDrawable(Context context) {
    if (iconDrawable == null) iconDrawable = context.getResources().getDrawable(icon);
    return iconDrawable;
  }

  public void setIconDrawable(Drawable iconDrawable) {
    this.iconDrawable = iconDrawable;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ActionItem that = (ActionItem) o;
    return actionId == that.actionId;
  }

  @Override public int hashCode() {
    return actionId;
  }
}
