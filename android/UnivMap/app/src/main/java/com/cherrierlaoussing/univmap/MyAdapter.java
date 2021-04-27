package com.cherrierlaoussing.univmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    String[] dataNom; String[] dataFiliere; String[] dataEnseignant  ; String[] dataSalle; String[] dataHoraireDebut; String[] dataHoraireFin;
    Context context;

    public MyAdapter(Context ct, String[] nom, String[] filiere, String[] enseignant, String[] salle, String[] horaireDebut, String[] horaireFin){
        context = ct;
        dataNom = nom;
        dataFiliere = filiere;
        dataEnseignant = enseignant;
        dataSalle = salle;
        dataHoraireDebut = horaireDebut;
        dataHoraireFin = horaireFin;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nom.setText(dataNom[position]);
        holder.filiere.setText(dataFiliere[position]);
        holder.enseignant.setText(dataEnseignant[position]);
        holder.salle.setText(dataSalle[position]);
        holder.horaireDebut.setText(dataHoraireDebut[position]);
        holder.horaireFin.setText(dataHoraireFin[position]);

    }

    @Override
    public int getItemCount() {
        return dataNom.length;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nom, filiere, enseignant, salle, horaireDebut, horaireFin;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nom = itemView.findViewById(R.id.row_planning_nom);
            filiere = itemView.findViewById(R.id.row_planning_filiere);
            enseignant = itemView.findViewById(R.id.row_planning_enseignant);
            salle = itemView.findViewById(R.id.row_planning_salle);
            horaireDebut = itemView.findViewById(R.id.row_planning_hDebut);
            horaireFin = itemView.findViewById(R.id.row_planning_hFin);

        }
    }





}
