package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class VisiteurAdapter extends BaseAdapter {
    private Context context;
    private List<Visiteur> visiteursList;
    private LayoutInflater inflater;

    public VisiteurAdapter(MainActivity context, List<Visiteur> visiteursList) {
        this.context = context;
        this.visiteursList = visiteursList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return visiteursList.size();
    }

    @Override
    public Visiteur getItem(int position) {
        return visiteursList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.adapter_item, viewGroup, false);
        }
        Visiteur currentVisiteur = getItem(i);
        String numVist = currentVisiteur.getNumVist();
        String nom = currentVisiteur.getNom();
        int nb_jours = currentVisiteur.getNb_jours();
        int tarifJ = currentVisiteur.getTarifJ();
        int tarif = currentVisiteur.getTarif();
        TextView numView = view.findViewById(R.id.num);
        numView.setText(numVist);
        TextView nomView = view.findViewById(R.id.nom);
        nomView.setText(nom);
        TextView nbView = view.findViewById(R.id.nb_jour);
        nbView.setText(String.valueOf(nb_jours));
        TextView tarifJView = view.findViewById(R.id.TJ);
        tarifJView.setText(String.valueOf(tarifJ));
        TextView tarifView = view.findViewById(R.id.T);
        tarifView.setText(String.valueOf(tarif));
        return view;
    }
}
