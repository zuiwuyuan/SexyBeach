// Generated code from Butter Knife. Do not modify!
package com.lnyp.sexybeach.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BeautyListActivity$$ViewBinder<T extends com.lnyp.sexybeach.activity.BeautyListActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427447, "field 'rViewClassify'");
    target.rViewClassify = finder.castView(view, 2131427447, "field 'rViewClassify'");
  }

  @Override public void unbind(T target) {
    target.rViewClassify = null;
  }
}
