package ru.oepak22.githubmvp.screen.repositories;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.oepak22.githubmvp.R;
import ru.oepak22.githubmvp.content.Repository;

public class RepositoryHolder extends RecyclerView.ViewHolder {

    TextView mName;
    TextView mLanguage;

    public RepositoryHolder(View itemView) {
        super(itemView);

        mName = itemView.findViewById(R.id.repositoryName);
        mLanguage = itemView.findViewById(R.id.repositoryLanguage);
    }

    public void bind(@NonNull Repository repository) {
        mName.setText(repository.getName());
        mLanguage.setText(repository.getPrivate());
    }
}
