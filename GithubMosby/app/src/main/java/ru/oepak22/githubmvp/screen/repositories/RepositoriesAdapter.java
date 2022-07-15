package ru.oepak22.githubmvp.screen.repositories;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import java.util.List;

import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.Repository;
import ru.oepak22.githubmvp.widget.BaseAdapter;

public class RepositoriesAdapter extends BaseAdapter<RepositoryHolder, Repository> {

    public RepositoriesAdapter(@NonNull List<Repository> items) {
        super(items);
    }

    @Override
    public RepositoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RepositoryHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repository, parent, false));
    }

    @Override
    public void onBindViewHolder(RepositoryHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Repository repository = getItem(position);
        holder.bind(repository);
    }
}
