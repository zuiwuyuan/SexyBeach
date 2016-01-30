// Generated code from Butter Knife. Do not modify!
package com.lnyp.sexybeach.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BeautyDetailActivity$$ViewBinder<T extends com.lnyp.sexybeach.activity.BeautyDetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427445, "field 'imgCover'");
    target.imgCover = finder.castView(view, 2131427445, "field 'imgCover'");
    view = finder.findRequiredView(source, 2131427446, "field 'textCount'");
    target.textCount = finder.castView(view, 2131427446, "field 'textCount'");
    view = finder.findRequiredView(source, 2131427443, "field 'textTitle'");
    target.textTitle = finder.castView(view, 2131427443, "field 'textTitle'");
    view = finder.findRequiredView(source, 2131427444, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131427441, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131427442, "method 'onClick'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.imgCover = null;
    target.textCount = null;
    target.textTitle = null;
  }
}
