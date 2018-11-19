package com.example.tangel.vladychatting.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public abstract class MyListAdapter<T> extends BaseAdapter {

    private ArrayList<T> mMessageData = new ArrayList<>();
    private Class<T> mModelClass;
    protected Activity mActivity;
    protected int mLayoutId;

    public MyListAdapter(Activity activity,
                        Class<T> modelClass,
                        @LayoutRes int modelLayout,
                        DataSnapshot dbRef) {
        mActivity = activity;
        mModelClass = modelClass;
        mLayoutId = modelLayout;
        mMessageData.clear();
        if (dbRef != null) {
            for (DataSnapshot ds : dbRef.getChildren()) {
                mMessageData.add(ds.getValue(modelClass));
            }
        }
    }

    @Override
    public int getCount() {
        return mMessageData.size();
    }

    @Override
    public T getItem(int position) {
        return mMessageData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mActivity.getLayoutInflater().inflate(mLayoutId, parent, false);
        }

        T model = getItem(position);

        // Call out to subclass to marshall this model into the provided view
        populateView(convertView, model, position);

        return convertView;
    }

    /**
     * Each time the data at the given Firebase location changes,
     * this method will be called for each item that needs to be displayed.
     * The first two arguments correspond to the mLayoutId and mModelClass given to the constructor of
     * this class. The third argument is the item's position in the list.
     * <p>
     * Your implementation should populate the view using the data contained in the model.
     *
     * @param v        The view to populate
     * @param model    The object containing the data used to populate the view
     * @param position The position in the list of the view being populated
     */
    protected abstract  void populateView(View v, T model, int position);
}
