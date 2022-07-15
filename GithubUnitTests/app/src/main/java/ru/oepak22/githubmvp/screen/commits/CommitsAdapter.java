package ru.oepak22.githubmvp.screen.commits;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.CommitResponse;
import ru.oepak22.githubmvp.widget.BaseAdapter;

public class CommitsAdapter extends BaseAdapter<CommitHolder, CommitResponse> {

    public CommitsAdapter(@NonNull List<CommitResponse> items) { super(items); }

    @Override
    public CommitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommitHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_commit, parent, false));
    }

    @Override
    public void onBindViewHolder(CommitHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        CommitResponse commit = getItem(position);
        holder.bind(commit);
    }
}
