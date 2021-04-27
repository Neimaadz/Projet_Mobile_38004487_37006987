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

    private OnNoteListener mOnNoteListener;

    // Constructeur
    public MyAdapter(Context ct, String[] nom, String[] filiere, String[] enseignant, String[] salle, String[] horaireDebut, String[] horaireFin, OnNoteListener onNoteListener){
        context = ct;
        dataNom = nom;
        dataFiliere = filiere;
        dataEnseignant = enseignant;
        dataSalle = salle;
        dataHoraireDebut = horaireDebut;
        dataHoraireFin = horaireFin;
        mOnNoteListener = onNoteListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);   // On récupère la vue
        return new MyViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {  // Affichage des infos récupérer dans MyViewHolder (ci-dessous)
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
    }   // Le nombre total d'item


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nom, filiere, enseignant, salle, horaireDebut, horaireFin;
        OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {    // On récupère les id sur la vue
            super(itemView);

            nom = itemView.findViewById(R.id.row_planning_nom);
            filiere = itemView.findViewById(R.id.row_planning_filiere);
            enseignant = itemView.findViewById(R.id.row_planning_enseignant);
            salle = itemView.findViewById(R.id.row_planning_salle);
            horaireDebut = itemView.findViewById(R.id.row_planning_hDebut);
            horaireFin = itemView.findViewById(R.id.row_planning_hFin);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);  // Pour pouvoir cliquer sur une cellule

        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }





}
