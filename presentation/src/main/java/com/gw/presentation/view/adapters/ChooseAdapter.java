package com.gw.presentation.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import com.gw.presentation.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class ChooseAdapter extends ArrayAdapter<String> {
    public interface IProcessFilter {
        void setPosition(int position);
        int getPosition();
    }

    private int mResourceId = 0;
    private LayoutInflater mLayoutInflater;
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;
    private ArrayList<String> mItems;
    private IProcessFilter mCallback;
    private boolean mIsNew = true;

    public ChooseAdapter(Context context, int resource, ArrayList<String> objects, IProcessFilter callback) {
        super(context, resource, objects);
        mCallback = callback;
        if (callback.getPosition() != -1) {
            mSelectedPosition = callback.getPosition();
        }
        mItems = objects;
        mResourceId = resource;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NotNull
    @Override
    public View getView(final int position, View convertView, @NotNull final ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {

            view = mLayoutInflater.inflate(mResourceId, parent, false);
            holder = new ViewHolder();

            holder.radioBtn = (RadioButton) view.findViewById(R.id.selection_radioButton);
            holder.radioBtn.setText(mItems.get(position));

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.radioBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (position != mSelectedPosition && mSelectedRB != null) {
                    mSelectedRB.setChecked(false);
                }

                mSelectedPosition = position;
                mSelectedRB = (RadioButton) v;
                mCallback.setPosition(mSelectedPosition);
            }
        });

        if (mIsNew && mSelectedPosition != -1 && position == mSelectedPosition) {
            mIsNew = false;
            holder.radioBtn.callOnClick();
        }

        if (mSelectedPosition != position) {
            holder.radioBtn.setChecked(false);
        } else {
            holder.radioBtn.setChecked(true);
            if (mSelectedRB != null && holder.radioBtn != mSelectedRB) {
                mSelectedRB = holder.radioBtn;
            }
        }

        return view;
    }

    private class ViewHolder {
        RadioButton radioBtn;
    }
}
