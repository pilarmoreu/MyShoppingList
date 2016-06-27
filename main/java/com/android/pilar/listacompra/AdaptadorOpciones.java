package com.android.pilar.listacompra;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AdaptadorOpciones extends ArrayAdapter<Articulo> {

    private Activity                contexto;
    private ArrayList<Articulo>     datos;

    // Constructor que indica las opciones de nuestro producto
    public AdaptadorOpciones(Activity contexto, ArrayList<Articulo> datos){
        super(contexto, R.layout.lista, datos);
        this.contexto = contexto;
        this.datos = datos;
    }

    // Método que me dibuja el layout
    public View getView(final int position, View convertView, ViewGroup parent){

        View item = convertView;
        VistaTag vt;

        if(item == null){
            LayoutInflater inflater = contexto.getLayoutInflater();
            item = inflater.inflate(R.layout.lista, null);

            vt = new VistaTag();
            vt.item_listView = (TextView)item.findViewById(R.id.textView1);
            item.setTag(vt);
        }
        else {
            vt = (VistaTag)item.getTag();
        }

        vt.item_listView.setText(datos.get(position).getNombre());
        if (datos.get(position).isComprado()){
            vt.item_listView.setPaintFlags(vt.item_listView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            vt.item_listView.setTextColor(Color.parseColor("#CC0000"));
        }
        else{
            vt.item_listView.setPaintFlags(vt.item_listView.getPaintFlags() &~Paint.STRIKE_THRU_TEXT_FLAG);
            vt.item_listView.setTextColor(Color.parseColor("#000000"));
        }
        return(item);
    }

    public class VistaTag {
        TextView item_listView;
    }
}
