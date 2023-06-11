package com.example.mobile_app.model.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobile_app.R;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.item_user.ItemUser;
import com.example.mobile_app.model.item_user.ItemUserAdapter;

import java.util.ArrayList;
import java.util.List;

public class ModerationUsersFragment extends Fragment implements RecyclerViewInterface {
    private List<ItemUser> users = new ArrayList<ItemUser>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_moderation_users, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.modusers_recyclerview_users);

        users.add(new ItemUser(0,"Neymar", "Jean", "jean.neymar@uha.fr", 0));
        users.add(new ItemUser(1,"Génieur", "Alain", "alain.genieur@uha.fr", 0));
        users.add(new ItemUser(2,"Pildéboitt", "Jean", "jean.pildeeboitt@uha.fr", 1));
        users.add(new ItemUser(3,"Provist", "Alain", "alain.provist@uha.fr", 1));
        users.add(new ItemUser(4,"Nom", "Prénom1", "nom.prenom1@uha.fr", 2));
        users.add(new ItemUser(5,"Sériens", "Jean", "jean.seriens@uha.fr", 0));
        users.add(new ItemUser(6,"Sapin", "Etienne", "etienne.sapin@uha.fr", 1));
        users.add(new ItemUser(7,"Delta", "Alexandre", "alexandre.delta@uha.fr", 2));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ItemUserAdapter(users, getActivity(), this));

        return rootView;
    }

    @Override
    public void onItemClick(int position) {

    }
}