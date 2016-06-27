package com.android.pilar.listacompra;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener{

    private ArrayList<Articulo>         datos;
    private Bundle                      argDialogo;
    private AdaptadorOpciones           adaptador;
    private View                        inflador;
    private AlertDialog.Builder         ventana;

    private static final int            DIALOGO_PERSONALIZADO = 1;
    private static final int            MENU_CONTEXTUAL_OP1 = 2;
    private static final int            MENU_CONTEXTUAL_OP2 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lista con los datos (lista de artículos)
        datos = new ArrayList<Articulo>();
        argDialogo = new Bundle();
        adaptador = new AdaptadorOpciones(this, datos);

        final ListView lv = (ListView)this.findViewById(R.id.listView1);
        lv.setAdapter(adaptador);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id){
                if (datos.get(position).isComprado()){
                    datos.get(position).setComprado(false);
                    Toast.makeText(getApplicationContext(), "No has comprado '"  +
                            datos.get(position).getNombre() + "'", Toast.LENGTH_SHORT).show();
                }
                else{
                    datos.get(position).setComprado(true);
                    Toast.makeText(getApplicationContext(), "Has comprado '"  +
                            datos.get(position).getNombre() + "'", Toast.LENGTH_SHORT).show();
                }
                adaptador.notifyDataSetChanged();
            }
        });

        //registro el menu contextual para el listView
        this.registerForContextMenu(lv);
    }

    // Creamos las opciones del menú
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // Indica que se hace cuando se pulsa cada una de las opciones del menú
    public boolean onOptionsItemSelected(MenuItem item){

        if (item.getItemId() == R.id.item1){
            argDialogo.putCharSequence("TituloDialogo", item.getTitle());
            argDialogo.putCharSequence("Accion", "Crear");
            //Lanzo la ventana de dialogo
            this.showDialog(DIALOGO_PERSONALIZADO);
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    // Crea el menú
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listView1){
            menu.add(Menu.NONE, MENU_CONTEXTUAL_OP1, Menu.NONE, "Editar artículo");
            menu.add(Menu.NONE, MENU_CONTEXTUAL_OP2, Menu.NONE, "Borrar artículo");
            menu.setHeaderTitle("Operaciones");
        }
    }

    // Indica que se hace cuando se elige una de las opciones del menú
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){

            case MENU_CONTEXTUAL_OP1:
                argDialogo.putCharSequence("TituloDialogo", item.getTitle());
                argDialogo.putInt("IndiceArticulo", info.position);
                argDialogo.putCharSequence("Accion", "Editar");
                this.showDialog(DIALOGO_PERSONALIZADO);
                return true;

            case MENU_CONTEXTUAL_OP2:
                // Borro el elemento del ArrayList
                datos.remove(info.position);

                // Actualizo el listVIew
                adaptador.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Artículo borrado", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    // Crea el dialogo
    protected Dialog onCreateDialog(int id){
        switch(id){
            case DIALOGO_PERSONALIZADO:
                String infService = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater li =(LayoutInflater)getApplicationContext().getSystemService(infService);
                inflador = li.inflate(R.layout.dialogo, null);

                ventana = new AlertDialog.Builder(this);
                ventana.setTitle(argDialogo.getCharSequence("TituloDialogo"));
                ventana.setView(inflador);
                ventana.setPositiveButton("Aceptar", this);
                ventana.setNegativeButton("Cancelar", this);
                return ventana.create();
        }
        return null;
    }

    //Actualiza el diálogo antes de mostrarlo según se vaya a crear/editar un artículo.
    protected void onPrepareDialog (int id, Dialog dialog){
        super.onPrepareDialog(id, dialog);
        switch(id){

            case DIALOGO_PERSONALIZADO:
                final TextView nombreEdit = (TextView)dialog.findViewById(R.id.editText1);

                //Si estoy editando un articulo
                if (argDialogo.getCharSequence("Accion").toString().equals("Editar")){
                    //Pongo el nombre del artículo sobre el que se ha desplegado el menu contextual
                    nombreEdit.setText(datos.get(argDialogo.getInt("IndiceArticulo")).getNombre());
                }
                else{
                    nombreEdit.setText("");
                }

                //Actualizo el titulo del dialogo
                dialog.setTitle(argDialogo.getCharSequence("TituloDialogo"));
        }
    }

    // Metodo que indica lo que ocurre cuando se pulsa el boton
    public void onClick(DialogInterface dialog, int botonPulsado) {
        if (botonPulsado == DialogInterface.BUTTON_POSITIVE){
            TextView nombreEdit = (TextView)inflador.findViewById(R.id.editText1);

            //Si estoy creando un articulo
            if (argDialogo.getCharSequence("Accion").toString().equals("Crear")){

                //creo el objeto articulo y lo añado a la lista
                datos.add(datos.size(), new Articulo(nombreEdit.getText().toString(), false));
                //Actualizo el listview
                adaptador.notifyDataSetChanged();
                //Muestro un mensaje informativo
                Toast.makeText(getApplicationContext(), "Articulo creado con exito", Toast.LENGTH_SHORT).show();
            }

            //Si estoy editando un articulo
            else{
                //edito el articulo
                datos.get(argDialogo.getInt("IndiceArticulo")).setNombre(nombreEdit.getText().toString());
                //Actualizo el listview
                adaptador.notifyDataSetChanged();
                //Muestro un mensaje informativo
                Toast.makeText(getApplicationContext(), "Articulo editado con éxito", Toast.LENGTH_SHORT).show();
            }
        }
        else if (botonPulsado == DialogInterface.BUTTON_NEGATIVE){
            Toast.makeText(getApplicationContext(), "Operacion cancelada", Toast.LENGTH_SHORT).show();
        }
    }
}
