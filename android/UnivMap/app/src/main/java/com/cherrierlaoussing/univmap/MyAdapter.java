package com.cherrierlaoussing.univmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.model.univmap.ColorCustomPopUp;


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
        TextView nom, filiere, enseignant, salle, horaireDebut, horaireFin, date;
        OnNoteListener onNoteListener;
        ConstraintLayout constraint;

        /// =============================== Data Persist =============================
        private SharedPreferences preferences;
        private int r,g,b;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {    // On récupère les id sur la vue
            super(itemView);

            nom = itemView.findViewById(R.id.row_planning_nom);
            filiere = itemView.findViewById(R.id.row_planning_filiere);
            enseignant = itemView.findViewById(R.id.row_planning_enseignant);
            salle = itemView.findViewById(R.id.row_planning_salle);
            horaireDebut = itemView.findViewById(R.id.row_planning_hDebut);
            horaireFin = itemView.findViewById(R.id.row_planning_hFin);
            date = itemView.findViewById(R.id.row_planning_date);
            this.onNoteListener = onNoteListener;

            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            r = preferences.getInt("red",91);
            g = preferences.getInt("green",107);
            b = preferences.getInt("blue",128);
            constraint = itemView.findViewById(R.id.viewCalendar);
            constraint.setBackgroundColor(Color.argb(255,r,g,b));

            nom.setTextColor(ColorCustomPopUp.adaptativeColor(r,g,b));
            filiere.setTextColor(ColorCustomPopUp.adaptativeColor(r,g,b));
            enseignant.setTextColor(ColorCustomPopUp.adaptativeColor(r,g,b));
            salle.setTextColor(ColorCustomPopUp.adaptativeColor(r,g,b));
            horaireFin.setTextColor(ColorCustomPopUp.adaptativeColor(r,g,b));
            horaireDebut.setTextColor(ColorCustomPopUp.adaptativeColor(r,g,b));
            date.setTextColor(ColorCustomPopUp.adaptativeColor(r,g,b));

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
