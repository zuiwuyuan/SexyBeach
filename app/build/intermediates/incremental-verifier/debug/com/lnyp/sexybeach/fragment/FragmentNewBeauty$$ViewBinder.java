// Generated code from Butter Knife. Do not modify!
package com.lnyp.sexybeach.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FragmentNewBeauty$$ViewBinder<T extends com.lnyp.sexybeach.fragment.FragmentNewBeauty> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427453, "field 'listViewNewBeauty'");
    target.listViewNewBeauty = finder.castView(view, 2131427453, "field 'listViewNewBeauty'");
  }

  @Override public void unbind(T target) {
    target.listViewNewBeauty = null;
  }
}
