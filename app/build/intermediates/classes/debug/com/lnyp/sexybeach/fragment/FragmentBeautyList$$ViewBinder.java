// Generated code from Butter Knife. Do not modify!
package com.lnyp.sexybeach.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FragmentBeautyList$$ViewBinder<T extends com.lnyp.sexybeach.fragment.FragmentBeautyList> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427452, "field 'listViewBeauties'");
    target.listViewBeauties = finder.castView(view, 2131427452, "field 'listViewBeauties'");
  }

  @Override public void unbind(T target) {
    target.listViewBeauties = null;
  }
}
