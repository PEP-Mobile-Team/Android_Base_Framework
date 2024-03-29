package com.pep.core.libbase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pep.core.uibase.PEPProgressView;


public abstract class PEPBaseFragment extends Fragment implements View.OnClickListener {

    public LinearLayout rootView;
    public View contentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = View.inflate(getActivity(), getLayoutId(), null);
        rootView = (LinearLayout) findViewById(R.id.rootView);


        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (DebugUtil.BASE_IS_DEBUG){
            initView();
            initData();
            initListener();
        }else{
            try {
                initView();
                initData();
                initListener();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addContentView(View contentView) {
        rootView.removeAllViews();
        rootView.addView(contentView);
    }


    public View findViewById(int id) {
        return contentView.findViewById(id);
    }


    public int getLayoutId() {
        return R.layout.activity_root;
    }

    public abstract void initView();

    public abstract void initData();

    public void initListener() {
    }

    public void showProgress() {
        if (getActivity() != null) {
            PEPProgressView.show(getActivity());
        }


    }

    public void dismissProgress() {
        PEPProgressView.dismiss();

    }

    @Override
    public void onClick(View view) {

    }
}
