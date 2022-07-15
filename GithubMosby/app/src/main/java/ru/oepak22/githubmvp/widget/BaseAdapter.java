package ru.oepak22.githubmvp.widget;

import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {

    private final List<T> mItems = new ArrayList<>();

    @Nullable
    private OnItemClickListener<T> mOnItemClickListener;

    private final View.OnClickListener mInternalListener = (view) -> {
        if (mOnItemClickListener != null) {
            int position = (int) view.getTag();
            T item = mItems.get(position);
            mOnItemClickListener.onItemClick(item);
        }
    };

    @Nullable
    private EmptyRecyclerView mRecyclerView;

    public BaseAdapter(@NonNull List<T> items) {
        mItems.addAll(items);
    }

    public void attachToRecyclerView(@NonNull EmptyRecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mRecyclerView.setAdapter(this);
        refreshRecycler();
    }

    public final void add(@NonNull T value) {
        mItems.add(value);
        refreshRecycler();
    }

    public final void changeDataSet(@NonNull List<T> values) {
        mItems.clear();
        mItems.addAll(values);
        refreshRecycler();
    }

    public final void clear() {
        mItems.clear();
        refreshRecycler();
    }

    public void refreshRecycler() {
        notifyDataSetChanged();
        if (mRecyclerView != null) {
            mRecyclerView.checkIfEmpty();
        }
    }

    @CallSuper
    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mInternalListener);
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface OnItemClickListener<T> {

        void onItemClick(@NonNull T item);

    }

}
